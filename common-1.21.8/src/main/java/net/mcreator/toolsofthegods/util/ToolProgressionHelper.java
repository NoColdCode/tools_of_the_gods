package net.mcreator.toolsofthegods.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mcreator.toolsofthegods.item.ArmorOfTheGodsItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TogArmorPiece;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.XpBoostHelper;
import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;

public class ToolProgressionHelper {
	public enum ToolType {
		PICKAXE,
		HAMMER,
		AXE,
		SHOVEL,
		HOE,
		SWORD,
		BOW,
		ARMOR,
		SHIELD,
		FISHING_ROD,
		CROSSBOW,
		TRIDENT,
		SPEAR,
		FLAIL,
		STAFF,
		WINGS,
		ULTIMATE,
		NONE
	}

	private static final String NBT_XP = "xp";
	private static final String NBT_LEVEL = "level";

	/** Share of full-set armor per piece (helmet, chest, legs, boots) ? vanilla leather ratios. */
	private static final float[] ARMOR_PIECE_SHARE = {1f / 7f, 3f / 7f, 2f / 7f, 1f / 7f};
	private static final float ARMOR_FULL_SET_MIN = 0.5f;
	private static final float ARMOR_FULL_SET_MAX = 35.0f;
	private static final String NBT_TIER = "tier";
	private static final String NBT_NEXT = "nexttier";
	private static final String NBT_NEEDS_UPGRADE = "needsUpgrade";
	private static final String NBT_COOLDOWN = "togPowerCooldownEnd";
	private static final String NBT_TOOL_TYPE = "togToolType";
	/** Fractional XP remainder so ?1.5 averages correctly instead of rounding 1?2. */
	private static final String NBT_XP_FRAC = "togXpFrac";

	public static ToolType getToolType(ItemStack stack) {
		Item item = stack.getItem();
		if (item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get()) {
			return ToolType.PICKAXE;
		}
		if (item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get()) {
			return ToolType.HAMMER;
		}
		if (item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get()) {
			return ToolType.AXE;
		}
		if (item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get()) {
			return ToolType.SHOVEL;
		}
		if (item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get()) {
			return ToolType.HOE;
		}
		if (item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get()) {
			return ToolType.SWORD;
		}
		if (ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW != null && item == ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get()) {
			return ToolType.BOW;
		}
		if (item == ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get()) {
			return ToolType.ULTIMATE;
		}
		if (ToolsOfTheGodsModItems.isArmorOfTheGods(item)) {
			return ToolType.ARMOR;
		}
		if (item == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return ToolType.SHIELD;
		}
		if (item == ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get()) {
			return ToolType.FISHING_ROD;
		}
		if (item == ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get()) {
			return ToolType.CROSSBOW;
		}
		if (item == ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get()) {
			return ToolType.TRIDENT;
		}
		if (item == ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get()) {
			return ToolType.SPEAR;
		}
		if (item == ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get()) {
			return ToolType.FLAIL;
		}
		if (item == ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get()) {
			return ToolType.STAFF;
		}
		if (item == ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get()) {
			return ToolType.WINGS;
		}
		return ToolType.NONE;
	}

	public static boolean isTogTool(ItemStack stack) {
		return getToolType(stack) != ToolType.NONE;
	}

	public static boolean usesArmorProgression(ToolType type) {
		return type == ToolType.ARMOR || type == ToolType.SHIELD;
	}

	public static int getMaxLevel(ToolType type) {
		if (type == ToolType.BOW || type == ToolType.CROSSBOW) {
			return 50;
		}
		return 100;
	}

	public static int getMaxTier(ToolType type) {
		if (type == ToolType.BOW || type == ToolType.CROSSBOW) {
			return 4;
		}
		return 9;
	}

	public static int getTier(ToolType type, int level) {
		int maxLevel = getMaxLevel(type);
		int maxTier = getMaxTier(type);
		if (level >= maxLevel) {
			return maxTier;
		}
		return Math.max(0, Math.min(maxTier, level / 10));
	}

	public static int getStoredTier(ItemStack stack) {
		ToolType type = getToolType(stack);
		if (type == ToolType.NONE) {
			return 0;
		}
		int stored = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_TIER, 0.0);
		if (stored > 0 || stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(NBT_TIER)) {
			return stored;
		}
		int level = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		return getTier(type, level);
	}

	public static boolean needsUpgrade(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBT_NEEDS_UPGRADE, false);
	}

	public static int getEffectiveTierForStats(ItemStack stack) {
		ToolType type = getToolType(stack);
		if (type == ToolType.NONE) {
			return 0;
		}

		int level = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		if (level >= getMaxLevel(type)) {
			return getMaxTier(type);
		}

		if (needsUpgrade(stack) && isBoundary(type, level)) {
			return getStoredTier(stack);
		}

		return getTier(type, level);
	}

	public static int getEffectiveLevelInTier(ItemStack stack) {
		ToolType type = getToolType(stack);
		if (type == ToolType.NONE) {
			return 0;
		}

		int level = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		if (level >= getMaxLevel(type)) {
			return 10;
		}

		if (needsUpgrade(stack) && isBoundary(type, level)) {
			return 10;
		}

		return Math.max(0, level % 10);
	}

	public static float getEffectiveMiningSpeed(ItemStack stack) {
		ToolType type = getToolType(stack);
		int tier = getEffectiveTierForStats(stack);
		int levelInTier = getEffectiveLevelInTier(stack);

		float base = switch (type) {
			case PICKAXE, HAMMER -> TierSystem.getMiningSpeedBonus(tier, levelInTier);
			case AXE, SHOVEL, HOE -> 0.5f + tier + (levelInTier * 0.2f);
			default -> 0.0f;
		};

		return base * TraitSystem.getMiningSpeedMultiplier(stack);
	}

	public static int getXpForNextLevel(ToolType type, int level) {
		int maxLevel = getMaxLevel(type);
		if (level >= maxLevel) {
			return 0;
		}
		double exponent = ToolsOfTheGodsCommonConfig.XP_EXPONENT_BASE.get() + (ToolsOfTheGodsCommonConfig.XP_EXPONENT_PER_LEVEL.get() * level);
		double baseXp = ToolsOfTheGodsCommonConfig.XP_CURVE_BASE.get() * Math.pow(level, exponent);
		baseXp *= ToolsOfTheGodsCommonConfig.XP_REQUIRED_MULTIPLIER.get();
		double paceDivisor = getToolXpPaceDivisor(type);
		if (paceDivisor > 1.0d) {
			baseXp /= paceDivisor;
		}
		return Math.max(1, (int) Math.floor(baseXp));
	}

	/**
	 * Tool-specific XP multiplier coefficients.
	 * Reflects tool usage rates and balance.
	 */
	public static double getToolXpMultiplier(ToolType type) {
		return switch (type) {
			case PICKAXE -> 1.0;
			case HAMMER -> 1.2;
			case AXE -> 0.8;
			case SWORD -> 0.9;
			case SHOVEL -> 0.8;
			case HOE -> 0.5;
			case BOW, CROSSBOW -> 2.0;
			case ARMOR -> 0.35;
			case SHIELD -> 1.0;
			case FISHING_ROD -> 1.2;
			case TRIDENT -> 1.1;
			case SPEAR -> 0.95;
			case FLAIL -> 0.92;
			case STAFF -> 0.9;
			case WINGS -> 0.4;
			default -> 1.0;
		};
	}

	/**
	 * Per-tool XP pace divisor applied to XP required per level (higher = fewer levels to max).
	 */
	public static double getToolXpPaceDivisor(ToolType type) {
		return switch (type) {
			case SWORD -> ToolsOfTheGodsCommonConfig.SWORD_XP_PACE_DIVISOR.get();
			case HOE -> ToolsOfTheGodsCommonConfig.HOE_XP_PACE_DIVISOR.get();
			case BOW -> ToolsOfTheGodsCommonConfig.BOW_XP_PACE_DIVISOR.get();
			case SHIELD -> ToolsOfTheGodsCommonConfig.SHIELD_XP_PACE_DIVISOR.get();
			default -> 1.0d;
		};
	}

	public static boolean isBoundary(ToolType type, int level) {
		return level > 0 && level % 10 == 0 && level < getMaxLevel(type);
	}

	public static void initializeTool(ItemStack stack, ToolType type) {
		initializeToolAtLevel(stack, type, 0);
	}

	public static void initializeToolAtLevel(ItemStack stack, ToolType type, int level) {
		int maxLevel = getMaxLevel(type);
		int clamped = Math.max(0, Math.min(maxLevel, level));
		int tier = getTier(type, clamped);
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			tag.putDouble(NBT_XP, 0);
			tag.putDouble(NBT_LEVEL, clamped);
			tag.putDouble(NBT_TIER, tier);
			tag.putDouble(NBT_NEXT, getXpForNextLevel(type, clamped));
			tag.putBoolean(NBT_NEEDS_UPGRADE, false);
			tag.putDouble(NBT_COOLDOWN, 0);
			tag.putString(NBT_TOOL_TYPE, type.name());
		});
	}

	public static boolean isInitialized(ItemStack stack) {
		if (!isTogTool(stack)) {
			return false;
		}
		var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		return tag.contains(NBT_LEVEL) && tag.contains(NBT_NEXT);
	}

	/** Max tier and max level, with no pending tier upgrade ? required for Ultimate Tool fusion. */
	public static boolean isFullyProgressed(ItemStack stack) {
		if (!isTogTool(stack)) {
			return false;
		}
		ensureInitialized(stack);
		ToolType type = getToolType(stack);
		if (needsUpgrade(stack)) {
			return false;
		}
		int level = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		return level >= getMaxLevel(type) && getStoredTier(stack) >= getMaxTier(type);
	}

	public static void ensureInitialized(ItemStack stack) {
		if (isTogTool(stack) && !isInitialized(stack)) {
			initializeTool(stack, getToolType(stack));
		}
	}

	/** Sync stored XP-to-next with the current curve. Call from gainXp only ? never from tooltips. */
	public static void syncXpRequirement(ItemStack stack) {
		if (!isTogTool(stack) || !isInitialized(stack)) {
			return;
		}
		ToolType type = getToolType(stack);
		int level = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		int expectedNext = getXpForNextLevel(type, level);
		int storedNext = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_NEXT, 0.0);
		if (storedNext != expectedNext) {
			CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble(NBT_NEXT, expectedNext));
		}
	}

	public static void gainXp(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack stack, int gainedXp) {
		if (entity == null) {
			return;
		}
		ToolType type = getToolType(stack);
		if (type == ToolType.NONE) {
			return;
		}

		ensureInitialized(stack);
		syncXpRequirement(stack);

		int currentXp = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_XP, 0.0);
		int currentLevel = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		int currentTier = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_TIER, 0.0);
		int needed = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_NEXT, 0.0);
		boolean needsUpgrade = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBT_NEEDS_UPGRADE, false);

		if (needsUpgrade) {
			return;
		}

		int maxLevel = getMaxLevel(type);
		if (currentLevel >= maxLevel) {
			return;
		}

		int effectiveXp = Math.max(1, gainedXp);
		double scaled = effectiveXp;
		if (type != ToolType.SWORD && type != ToolType.BOW && type != ToolType.CROSSBOW && type != ToolType.TRIDENT
			&& type != ToolType.SPEAR && type != ToolType.FLAIL && type != ToolType.STAFF && type != ToolType.ARMOR
			&& type != ToolType.SHIELD && type != ToolType.WINGS) {
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockState state = world.getBlockState(pos);
			scaled *= getBlockXpMultiplier(type, state);
		}

		scaled *= TraitSystem.getXpMultiplier(stack);
		scaled *= XpBoostHelper.getActiveMultiplier(entity);
		double gainMultiplier = ToolsOfTheGodsCommonConfig.GLOBAL_XP_GAIN_MULTIPLIER.get();
		if (type == ToolType.SWORD || type == ToolType.BOW || type == ToolType.CROSSBOW || type == ToolType.TRIDENT
			|| type == ToolType.SPEAR || type == ToolType.FLAIL || type == ToolType.STAFF || type == ToolType.ARMOR
			|| type == ToolType.SHIELD || type == ToolType.FISHING_ROD) {
			gainMultiplier *= ToolsOfTheGodsCommonConfig.COMBAT_XP_GAIN_MULTIPLIER.get();
		} else {
			gainMultiplier *= ToolsOfTheGodsCommonConfig.MINING_XP_GAIN_MULTIPLIER.get();
		}
		scaled *= gainMultiplier;

		// Bank fractional XP so Insight I (?1.5) is not rounded up to ?2 on every 1-XP tick.
		scaled += getXpFraction(stack);
		int grant = (int) Math.floor(scaled + 1.0e-9d);
		setXpFraction(stack, Math.max(0.0d, scaled - grant));
		if (grant <= 0) {
			return;
		}

		applyXpGain(world, x, y, z, entity, stack, type, currentXp, currentLevel, currentTier, needed, maxLevel, grant);
	}

	/** Flat XP for consumables - ignores block/trait/elixir/config multipliers. */
	public static void gainRawXp(Entity entity, ItemStack stack, int gainedXp) {
		if (entity == null) {
			return;
		}
		ToolType type = getToolType(stack);
		if (type == ToolType.NONE) {
			return;
		}
		ensureInitialized(stack);
		syncXpRequirement(stack);

		int currentXp = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_XP, 0.0);
		int currentLevel = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		int currentTier = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_TIER, 0.0);
		int needed = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_NEXT, 0.0);
		boolean needsUpgrade = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBT_NEEDS_UPGRADE, false);
		if (needsUpgrade) {
			return;
		}
		int maxLevel = getMaxLevel(type);
		if (currentLevel >= maxLevel) {
			return;
		}
		applyXpGain(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity, stack, type,
			currentXp, currentLevel, currentTier, needed, maxLevel, Math.max(1, gainedXp));
	}

	private static double getXpFraction(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_XP_FRAC, 0.0);
	}

	private static void setXpFraction(ItemStack stack, double value) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			if (value <= 1.0e-9d) {
				tag.remove(NBT_XP_FRAC);
			} else {
				tag.putDouble(NBT_XP_FRAC, value);
			}
		});
	}

	private static void applyXpGain(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack stack,
		ToolType type, int currentXp, int currentLevel, int currentTier, int needed, int maxLevel, int effectiveXp) {
		final int newXp = currentXp + effectiveXp;
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble(NBT_XP, newXp));

		if (newXp < needed) {
			return;
		}

		final int newLevel = Math.min(maxLevel, currentLevel + 1);

		if (isBoundary(type, newLevel)) {
			CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
				tag.putDouble(NBT_LEVEL, newLevel);
				tag.putDouble(NBT_XP, 0);
				tag.putDouble(NBT_NEXT, getXpForNextLevel(type, newLevel));
				tag.putBoolean(NBT_NEEDS_UPGRADE, true);
				tag.putString(NBT_TOOL_TYPE, type.name());
				tag.remove(NBT_XP_FRAC);
			});
			if (entity instanceof Player player) {
				player.displayClientMessage(Component.literal("§6§lLevel " + newLevel + " reached! §eRequires tier upgrade."), false);
				player.displayClientMessage(Component.literal("§e"
					+ TierSystem.getUpgradeMaterialMessage(currentTier + 1, usesArmorProgression(type))), false);
			}
		} else {
			CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
				tag.putDouble(NBT_LEVEL, newLevel);
				tag.putDouble(NBT_XP, 0);
				tag.putDouble(NBT_TIER, getTier(type, newLevel));
				tag.putDouble(NBT_NEXT, getXpForNextLevel(type, newLevel));
				tag.putBoolean(NBT_NEEDS_UPGRADE, false);
				tag.putString(NBT_TOOL_TYPE, type.name());
				tag.remove(NBT_XP_FRAC);
			});
			if (world instanceof Level level && !level.isClientSide()) {
				level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.player.levelup")), SoundSource.PLAYERS, 0.5f, 1.0f);
			}
			if (entity instanceof Player player) {
				player.displayClientMessage(Component.literal("§a§lLevel Up! §7" + newLevel), true);
			}
		}
	}

	/**
	 * Block-based XP weighting for mining progression.
	 * Rewards are based on rarity and tool type.
	 */
	public static float getBlockXpMultiplier(ToolType type, BlockState state) {
		if (state.isAir()) {
			return 1.0f;
		}

		float multiplier = 1.0f;

		// Rarity tag boosts - rare/valuable blocks give more XP
		if (state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.EMERALD_ORES)) {
			multiplier *= 15.0f;
		} else if (state.is(Blocks.ANCIENT_DEBRIS)) {
			multiplier *= 50.0f;
		} else if (state.is(BlockTags.REDSTONE_ORES) || state.is(BlockTags.LAPIS_ORES)) {
			multiplier *= 8.0f;
		} else if (state.is(BlockTags.GOLD_ORES)) {
			multiplier *= 5.0f;
		} else if (state.is(BlockTags.IRON_ORES)) {
			multiplier *= 2.5f;
		} else if (state.is(BlockTags.COAL_ORES)) {
			multiplier *= 1.5f;
		} else if (state.is(BlockTags.COPPER_ORES)) {
			multiplier *= 1.2f;
		} else if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN)) {
			multiplier *= 5.0f;
		}

		// Tool-specific penalties for bad usage
		if (type == ToolType.PICKAXE && state.is(BlockTags.LOGS)) {
			multiplier *= 0.2f; // Pickaxe is inefficient on wood
		} else if (type == ToolType.AXE && state.is(BlockTags.LEAVES)) {
			multiplier *= 0.2f; // Lower reward for trivial blocks
		}

		return Math.max(0.5f, multiplier);
	}

	/**
	 * XP for sword hits based on mob properties.
	 * Rewards scale with mob health and difficulty tier.
	 * Formula: base * health influence * difficulty multiplier
	 */
	public static int getSwordHitXp(LivingEntity target) {
		if (target == null) {
			return 1;
		}

		// Base multiplier from mob type
		float typeMultiplier = 1.0f;

		// Boss/Special mob categories (8-10?)
		if (target.getType() == net.minecraft.world.entity.EntityType.WITHER
				|| target.getType() == net.minecraft.world.entity.EntityType.ENDER_DRAGON
				|| target.getType() == net.minecraft.world.entity.EntityType.WARDEN) {
			typeMultiplier = 10.0f;
		}
		// Dangerous/Large hostile mobs (3-5?)
		else if (target instanceof Monster) {
			if (target.getType() == net.minecraft.world.entity.EntityType.CREEPER) {
				typeMultiplier = 3.0f;
			} else if (target.getType() == net.minecraft.world.entity.EntityType.ENDERMAN
					|| target.getType() == net.minecraft.world.entity.EntityType.BLAZE
					|| target.getType() == net.minecraft.world.entity.EntityType.GHAST
					|| target.getType() == net.minecraft.world.entity.EntityType.PIGLIN_BRUTE
					|| target.getType() == net.minecraft.world.entity.EntityType.RAVAGER) {
				typeMultiplier = 5.0f;
			} else {
				typeMultiplier = 2.0f; // Standard monsters
			}
		}
		// Passive animals (0.5-1?)
		else if (target instanceof Animal) {
			typeMultiplier = 1.0f;
		}
		// Other living entities
		else {
			typeMultiplier = 1.5f;
		}

		// Health influence: mobs with more health = more XP
		// High-health mobs (20+ health): ~2-3? boost
		// Med-health mobs (10-20): ~1.2-1.6? boost
		// Low-health mobs (<10): ~0.8-1.0? boost
		float maxHealth = target.getMaxHealth();
		float healthBoost = 1.0f;
		if (maxHealth > 20) {
			healthBoost = 1.0f + (float) Math.log(maxHealth * 0.1f) * 0.4f;
		} else if (maxHealth > 10) {
			healthBoost = 1.0f + (maxHealth - 10) * 0.04f;
		} else {
			healthBoost = 0.8f + (maxHealth * 0.02f);
		}

		int baseXp = (int) Math.ceil(typeMultiplier * healthBoost);
		return Math.max(1, baseXp);
	}

	/**
	 * Static weapon contribution from item constructor attributes.
	 */
	public static float getBaseWeaponDamage(ToolType type) {
		return switch (type) {
			case HAMMER -> 1.0f;
			case PICKAXE, AXE, SHOVEL, HOE, SWORD, BOW, CROSSBOW, TRIDENT, SPEAR, FLAIL, STAFF, FISHING_ROD, ARMOR, SHIELD, WINGS, ULTIMATE, NONE -> 0.0f;
		};
	}

	/**
	 * Progression bonus damage applied on hit for TOG tools.
	 */
	public static float getProgressionCombatBonus(ItemStack stack, ToolType type) {
		if (type == ToolType.NONE || type == ToolType.BOW) {
			return 0.0f;
		}

		int tier = getEffectiveTierForStats(stack);
		int levelInTier = getEffectiveLevelInTier(stack);

		float tierBonus = switch (type) {
			case SWORD -> tier * 1.5f;
			case FLAIL -> tier * 1.65f;
			case SPEAR -> tier * 1.0f;
			case HAMMER -> tier * 1.2f;
			case AXE -> tier * 1.1f;
			case PICKAXE -> tier * 0.8f;
			case SHOVEL -> tier * 0.7f;
			case HOE -> tier * 0.6f;
			default -> 0.0f;
		};

		float levelBonus = switch (type) {
			case SWORD -> levelInTier * 0.15f;
			case FLAIL -> levelInTier * 0.17f;
			case SPEAR -> levelInTier * 0.10f;
			case HAMMER -> levelInTier * 0.12f;
			case AXE -> levelInTier * 0.11f;
			case PICKAXE -> levelInTier * 0.08f;
			case SHOVEL -> levelInTier * 0.07f;
			case HOE -> levelInTier * 0.06f;
			default -> 0.0f;
		};

		return tierBonus + levelBonus;
	}

	/**
	 * Estimated pre-trait attack damage shown to players.
	 */
	public static float getEstimatedAttackDamage(ItemStack stack) {
		ToolType type = getToolType(stack);
		float rawDamage = 1.0f + getBaseWeaponDamage(type) + getProgressionCombatBonus(stack, type);
		return rawDamage * TraitSystem.getAttackDamageMultiplier(stack);
	}

	public static String getDisplayName(ItemStack stack) {
		ToolType type = getToolType(stack);
		if (type == ToolType.NONE) {
			return "Tool";
		}
		int tier = getStoredTier(stack);
		return switch (type) {
			case PICKAXE -> tier >= 9 ? "Pickaxe of the Gods" : TierSystem.getTierName(tier) + " Pickaxe";
			case HAMMER -> tier >= 9 ? "Hammer of the Gods" : TierSystem.getTierName(tier) + " Hammer";
			case AXE -> tier >= 9 ? "Axe of the Gods" : TierSystem.getTierName(tier) + " Axe";
			case SHOVEL -> tier >= 9 ? "Shovel of the Gods" : TierSystem.getTierName(tier) + " Shovel";
			case HOE -> tier >= 9 ? "Hoe of the Gods" : TierSystem.getTierName(tier) + " Hoe";
			case SWORD -> tier >= 9 ? "Sword of the Gods" : TierSystem.getTierName(tier) + " Sword";
			case BOW -> tier >= 4 ? "Bow of the Gods" : TierSystem.getTierName(tier) + " Bow";
			case CROSSBOW -> tier >= 4 ? "Crossbow of the Gods" : TierSystem.getTierName(tier) + " Crossbow";
			case FISHING_ROD -> tier >= 9 ? "Rod of the Gods" : TierSystem.getTierName(tier) + " Fishing Rod";
			case TRIDENT -> tier >= 9 ? "Trident of the Gods" : TierSystem.getTierName(tier) + " Trident";
			case SPEAR -> tier >= 9 ? "Spear of the Gods" : TierSystem.getTierName(tier) + " Spear";
			case FLAIL -> tier >= 9 ? "Flail of the Gods" : TierSystem.getTierName(tier) + " Flail";
			case STAFF -> tier >= 9 ? "Staff of the Gods" : TierSystem.getTierName(tier) + " Staff";
			case WINGS -> TierSystem.getWingsDisplayName(tier);
			case ARMOR -> getArmorDisplayName(stack, tier);
			case SHIELD -> tier >= 9 ? "Shield of the Gods" : TierSystem.getArmorTierName(tier) + " Shield";
			default -> "Tool";
		};
	}

	public static String getArmorSlotLabel(ItemStack stack) {
		var item = stack.getItem();
		if (item == ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get()) {
			return "Helmet";
		}
		if (item == ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get()) {
			return "Chestplate";
		}
		if (item == ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get()) {
			return "Leggings";
		}
		if (item == ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get()) {
			return "Boots";
		}
		return "Armor";
	}

	private static String getArmorDisplayName(ItemStack stack, int tier) {
		String slot = getArmorSlotLabel(stack);
		if (tier >= 9) {
			return slot + " of the Gods";
		}
		return TierSystem.getArmorTierName(tier) + " " + slot;
	}

	/**
	 * Chance to fully block melee or arrow damage while the shield is raised (0?1).
	 * Linear: 50% at level 0 ? 99% at level 100 (+ Guardian trait bonus).
	 */
	public static float getShieldBlockChance(ItemStack stack) {
		if (getToolType(stack) != ToolType.SHIELD) {
			return 0f;
		}
		int level = clampShieldLevel(stack);
		float base = 0.50f + (level / 100f) * 0.49f;
		base += TraitSystem.getGuardianBlockChanceBonus(stack);
		return Math.min(1.0f, base);
	}

	/**
	 * Fraction of incoming damage negated on a successful block.
	 * Linear: 60% at level 1 ? 100% at level 100 (+ Guardian trait bonus).
	 */
	public static float getShieldBlockReduction(ItemStack stack) {
		if (getToolType(stack) != ToolType.SHIELD) {
			return 0f;
		}
		int level = clampShieldLevel(stack);
		int scaled = Math.max(1, level);
		float base = 0.60f + ((scaled - 1) / 99f) * 0.40f;
		base += TraitSystem.getGuardianBlockReductionBonus(stack);
		return Math.min(1.0f, base);
	}

	public static int getShieldLevel(ItemStack stack) {
		if (getToolType(stack) != ToolType.SHIELD) {
			return 0;
		}
		return (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
	}

	private static int clampShieldLevel(ItemStack stack) {
		return Math.max(0, Math.min(getMaxLevel(ToolType.SHIELD), getShieldLevel(stack)));
	}

	/**
	 * Armor points for this piece ? fractional, scales smoothly with level (0.5 total set at level 0 ? 20 at 100).
	 */
	public static float getArmorDefensePoints(ItemStack stack, TogArmorPiece pieceType) {
		if (getToolType(stack) != ToolType.ARMOR) {
			return 0f;
		}
		int level = getArmorLevelForStats(stack);
		float fullSet = ARMOR_FULL_SET_MIN + (level / 100f) * (ARMOR_FULL_SET_MAX - ARMOR_FULL_SET_MIN);
		return fullSet * ARMOR_PIECE_SHARE[armorPieceIndex(pieceType)];
	}

	private static int getArmorLevelForStats(ItemStack stack) {
		int level = (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(NBT_LEVEL, 0.0);
		return Math.max(0, Math.min(getMaxLevel(ToolType.ARMOR), level));
	}

	public static float getArmorToughnessValue(ItemStack stack) {
		if (getToolType(stack) != ToolType.ARMOR) {
			return 0f;
		}
		int level = getArmorLevelForStats(stack);
		if (level < 70) {
			return 0f;
		}
		if (level < 80) {
			return 0.5f;
		}
		if (level < 90) {
			return 1.0f;
		}
		return 1.0f + ((level - 90) / 10f) * 2.0f;
	}

	public static float getArmorKnockbackResistance(ItemStack stack) {
		if (getToolType(stack) != ToolType.ARMOR) {
			return 0f;
		}
		int level = getArmorLevelForStats(stack);
		if (level >= 90) {
			return 0.10f;
		}
		if (level >= 80) {
			return 0.05f;
		}
		return 0f;
	}

	private static int armorPieceIndex(TogArmorPiece pieceType) {
		return switch (pieceType) {
			case HELMET -> 0;
			case CHESTPLATE -> 1;
			case LEGGINGS -> 2;
			case BOOTS -> 3;
		};
	}
}
