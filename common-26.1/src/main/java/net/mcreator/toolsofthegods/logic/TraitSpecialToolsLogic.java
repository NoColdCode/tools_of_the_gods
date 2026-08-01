package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogItemFishedContext;
import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.lang.reflect.Field;
import java.util.List;

public final class TraitSpecialToolsLogic {
	private static Field fishingNibbleField;

	public static void onItemFished(TogItemFishedContext ctx) {
		Player player = ctx.player();
		if (player.level().isClientSide()) {
			return;
		}
		ItemStack rod = findFishingRod(player);
		if (rod.isEmpty()) {
			return;
		}

		float treasureChance = TraitSystem.getAnglerTreasureChance(rod);
		if (treasureChance > 0.0f && player.level().getRandom().nextFloat() <= treasureChance) {
			ItemStack bonus = switch (player.level().getRandom().nextInt(4)) {
				case 0 -> new ItemStack(Items.EMERALD, 1 + player.level().getRandom().nextInt(2));
				case 1 -> new ItemStack(Items.LAPIS_LAZULI, 2 + player.level().getRandom().nextInt(4));
				case 2 -> new ItemStack(Items.NAME_TAG);
				default -> new ItemStack(Items.ENCHANTED_BOOK);
			};
			ItemEntity drop = new ItemEntity(player.level(), player.getX(),
				player.getY(), player.getZ(), bonus);
			player.level().addFreshEntity(drop);
		}

		if (TraitSystem.hasTrait(rod, TraitSystem.Trait.BOUNTIFUL_I)) {
			for (ItemStack stack : ctx.drops()) {
				if (!stack.isEmpty() && player.level().getRandom().nextFloat() <= 0.10f) {
					ItemEntity extra = new ItemEntity(player.level(), player.getX(),
						player.getY(), player.getZ(), stack.copy());
					player.level().addFreshEntity(extra);
				}
			}
		}

		if (TraitSystem.hasTrait(rod, TraitSystem.Trait.SCAVENGER_I)) {
			for (ItemStack stack : ctx.drops()) {
				if (!stack.isEmpty()) {
					ItemStack copy = stack.copy();
					if (!player.getInventory().add(copy)) {
						player.drop(copy, false);
					}
				}
			}
			ctx.drops().clear();
		}
	}

	public static void onPlayerTick(Player player) {
		WingsFlightLogic.onPlayerTick(player);

		if (player.level().isClientSide()) {
			return;
		}

		ItemStack rod = findFishingRod(player);
		if (player.fishing != null && !rod.isEmpty()) {
			handleFishingHook(player.fishing, rod);
		}

		for (Projectile projectile : player.level().getEntities(EntityType.TRIDENT, player.getBoundingBox().inflate(64.0d),
			e -> e instanceof Projectile p && p.getOwner() == player)) {
			handleReturningTrident(projectile);
		}
	}

	public static void onLivingIncomingDamage(TogIncomingDamageContext ctx) {
		if (ctx.entity().level().isClientSide()) {
			return;
		}

		if (ctx.entity() instanceof Player player) {
			ItemStack wings = TogEquipmentHelper.getTogWings(player);
			if (!wings.isEmpty()) {
				if (ctx.source().is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
					ctx.setAmount(ctx.amount()
						* WingsFlightLogic.getFallDamageMultiplier(wings)
						* TraitSystem.getFeatherfallDamageMultiplier(wings));
					return;
				}
				if (ctx.source().is(net.minecraft.world.damagesource.DamageTypes.FLY_INTO_WALL)) {
					ctx.setAmount(ctx.amount() * WingsFlightLogic.getKineticDamageMultiplier(wings));
					return;
				}
			}
		}

		Entity sourceEntity = ctx.source().getEntity();
		if (!(sourceEntity instanceof Player player)) {
			return;
		}

		Entity direct = ctx.source().getDirectEntity();
		float bonus = 0.0f;
		ItemStack weapon = ItemStack.EMPTY;
		boolean riptideBonus = false;

		if (direct instanceof AbstractArrow) {
			weapon = findBow(player);
			if (weapon.isEmpty()) {
				weapon = findCrossbow(player);
			}
			if (!weapon.isEmpty()) {
				bonus = TraitSystem.getMarksmanDamageBonus(weapon);
			}
		} else if (direct != null && direct.getType() == EntityType.TRIDENT) {
			weapon = findTrident(player);
			if (ToolProgressionHelper.getToolType(weapon) == ToolProgressionHelper.ToolType.TRIDENT) {
				bonus = TraitSystem.getImpalerDamageBonus(weapon);
				riptideBonus = TraitSystem.hasRiptide(weapon) && isWetEnvironment(player);
			}
		} else if (direct instanceof SmallFireball) {
			weapon = findStaff(player);
			if (!weapon.isEmpty()) {
				bonus = (TraitSystem.getArcaneBoltDamageMultiplier(weapon) - 1.0f) * 3.0f;
				if (TraitSystem.hasChanneling(weapon) && player.level() instanceof ServerLevel serverLevel
					&& player.level().isThundering() && player.level().canSeeSky(BlockPos.containing(player.position()))) {
					LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.TRIGGERED);
					if (bolt != null) {
						bolt.setPos(ctx.entity().getX(), ctx.entity().getY(), ctx.entity().getZ());
						serverLevel.addFreshEntity(bolt);
					}
				}
			}
		} else if (direct == player) {
			weapon = player.getMainHandItem();
			ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(weapon);
			if (type == ToolProgressionHelper.ToolType.TRIDENT) {
				bonus = TraitSystem.getImpalerDamageBonus(weapon);
			}
		}

		if (bonus > 0.0f) {
			ctx.setAmount(ctx.amount() + bonus);
		}
		if (riptideBonus) {
			ctx.setAmount(ctx.amount() * 1.5f);
		}

		if (ctx.entity() instanceof LivingEntity target && !weapon.isEmpty()) {
			if (target.getRemainingFireTicks() > 0) {
				ctx.setAmount(ctx.amount() * TraitSystem.getRimeBurningDamageMultiplier(weapon));
			}
			float threshold = TraitSystem.getExecutionerHealthThreshold(weapon);
			if (threshold > 0.0f && target.getMaxHealth() > 0.0f
				&& (target.getHealth() / target.getMaxHealth()) <= threshold) {
				ctx.setAmount(ctx.amount() * TraitSystem.getExecutionerDamageMultiplier(weapon));
			}
			if (direct instanceof AbstractArrow) {
				ctx.setAmount(ctx.amount() * TraitSystem.getStalkerProjectileDamageMultiplier(weapon, player.isShiftKeyDown()));
			}
		}

		if (direct instanceof AbstractArrow && !weapon.isEmpty()) {
			TraitPoisonHelper.applyMeleePoison(weapon, ctx.entity());
			TraitFreezyHelper.applyMeleeFreeze(weapon, ctx.entity());
			TraitExtendedCombatHelper.applyMeleeEffects(weapon, ctx.entity());
		}
	}

	private static void handleFishingHook(FishingHook hook, ItemStack rod) {
		if (!TraitSystem.hasReel(rod)) {
			return;
		}
		if (hook.tickCount % 2 != 0) {
			return;
		}
		try {
			Field nibble = getFishingNibbleField();
			int value = nibble.getInt(hook);
			if (value > 0) {
				nibble.setInt(hook, value - 1);
			}
		} catch (ReflectiveOperationException ignored) {
		}
	}

	private static void handleReturningTrident(Projectile trident) {
		if (!(trident.getOwner() instanceof Player owner)) {
			return;
		}
		ItemStack stack = findTrident(owner);
		if (stack.isEmpty() || !TraitSystem.hasReturning(stack)) {
			return;
		}
		if (trident.onGround()) {
			return;
		}
		Vec3 toOwner = owner.position().subtract(trident.position());
		if (toOwner.lengthSqr() < 1.0d) {
			return;
		}
		trident.setDeltaMovement(trident.getDeltaMovement().add(toOwner.normalize().scale(0.15d)));
	}

	private static Field getFishingNibbleField() throws NoSuchFieldException {
		if (fishingNibbleField == null) {
			fishingNibbleField = FishingHook.class.getDeclaredField("nibble");
			fishingNibbleField.setAccessible(true);
		}
		return fishingNibbleField;
	}

	private static boolean isWetEnvironment(Player player) {
		Level level = player.level();
		return level.isRainingAt(player.blockPosition()) || player.isInWaterOrRain();
	}

	public static ItemStack findFishingRod(Player player) {
		ItemStack main = player.getMainHandItem();
		if (ToolProgressionHelper.getToolType(main) == ToolProgressionHelper.ToolType.FISHING_ROD) {
			return main;
		}
		ItemStack off = player.getOffhandItem();
		if (ToolProgressionHelper.getToolType(off) == ToolProgressionHelper.ToolType.FISHING_ROD) {
			return off;
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findBow(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findCrossbow(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findStaff(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findTrident(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}
