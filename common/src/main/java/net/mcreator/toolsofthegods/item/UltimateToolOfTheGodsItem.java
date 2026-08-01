package net.mcreator.toolsofthegods.item;


import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.StaffSpell;
import net.mcreator.toolsofthegods.util.StaffSpellCaster;
import net.mcreator.toolsofthegods.util.StaffSpellHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.UltimateToolMode;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;

import java.util.List;

public class UltimateToolOfTheGodsItem extends PickaxeItem {
	private static final String NBT_POWER_MODE = "togUltimatePowerMode";

	private static final String[] POWER_MODE_NAMES = {"Balanced", "Precision", "Overdrive"};
	private static final float[] MODE_DESTROY_SPEED = {320.0f, 8.0f, 10000.0f};
	private static final float[] BONUS_DAMAGE = {2.0f, 6.0f, 0.5f};
	private static final float[] SWORD_MODE_DAMAGE_MULT = {1.0f, 1.35f, 1.1f};

	private static final Tier TOOL_TIER = new Tier() {
		@Override
		public int getUses() {
			return 0;
		}

		@Override
		public float getSpeed() {
			return 18.0f;
		}

		@Override
		public float getAttackDamageBonus() {
			return 9.0f;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 30;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(Items.NETHER_STAR);
		}
	};

	public UltimateToolOfTheGodsItem() {
		super(TOOL_TIER, new Item.Properties().attributes(DiggerItem.createAttributes(TOOL_TIER, 6.0f, -2.6f)).fireResistant().rarity(Rarity.EPIC));
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!player.isShiftKeyDown()) {
			UltimateToolMode toolMode = UltimateToolModeHelper.getMode(stack);
			if (toolMode == UltimateToolMode.STAFF) {
				if (!world.isClientSide()) {
					StaffSpell spell = StaffSpellHelper.getSelectedSpell(stack);
					StaffSpellCaster.cast(world, player, stack, spell);
					ToolProgressionHelper.gainXp(world, player.getX(), player.getY(), player.getZ(), player, stack, 3);
					player.getCooldowns().addCooldown(this, TraitSystem.getStaffCooldownTicks(stack));
				}
				return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
			}
			if (toolMode == UltimateToolMode.FISHING_ROD && !world.isClientSide() && player.fishing == null) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW,
					SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
				int lure = 0;
				int luck = 0;
				FishingHook hook = new FishingHook(player, world, lure, luck);
				world.addFreshEntity(hook);
				player.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
			}
			return InteractionResultHolder.pass(stack);
		}

		if (ToolProgressionHelper.needsUpgrade(stack)) {
			InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
			if (upgrade != null) {
				return upgrade;
			}
		}

		// Trait toggles take priority over power mode switching
		if (!world.isClientSide()) {
			boolean toggled = false;
			if (TraitSystem.hasTrait(stack, TraitSystem.Trait.SILKY_II)) {
				boolean on = TraitSystem.toggleSilky(stack);
				player.displayClientMessage(Component.literal("\u00a7aSilk Touch: \u00a7f" + (on ? "ON" : "OFF")), true);
				toggled = true;
			}
			if (TraitSystem.hasTrait(stack, TraitSystem.Trait.AUTOSMELT_II)) {
				boolean on = TraitSystem.toggleAutosmelt(stack);
				player.displayClientMessage(Component.literal("\u00a76Autosmelt: \u00a7f" + (on ? "ON" : "OFF")), true);
				toggled = true;
			}
			if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BROAD_TOUCH_II)) {
				TraitSystem.cycleBroadTouchMode(stack, 4);
				int mode = TraitSystem.getBroadTouchMode(stack);
				String label = switch (mode) {
					case 0 -> "3x3";
					case 1 -> "5x5";
					case 2 -> "7x7";
					default -> "9x9";
				};
				player.displayClientMessage(Component.literal("\u00a75Broad Touch: \u00a7f" + label), true);
				toggled = true;
			}
			if (toggled) {
				return InteractionResultHolder.success(stack);
			}
		}

		int newMode = (getPowerMode(stack) + 1) % POWER_MODE_NAMES.length;
		setPowerMode(stack, newMode);
		if (!world.isClientSide()) {
			player.displayClientMessage(Component.literal("\u00a7bPower Mode: \u00a7f" + POWER_MODE_NAMES[newMode]), true);
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		UltimateToolMode toolMode = UltimateToolModeHelper.getMode(stack);
		if (!toolMode.canHarvest(state)) {
			return 1.0f;
		}
		int power = getPowerMode(stack);
		if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_AXE)
			|| state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_HOE)) {
			return MODE_DESTROY_SPEED[power];
		}
		return switch (power) {
			case 0 -> 64.0f;
			case 1 -> 3.0f;
			case 2 -> 10000.0f;
			default -> super.getDestroySpeed(stack, state);
		};
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		return UltimateToolModeHelper.getMode(stack).canHarvest(state)
			|| state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_AXE)
			|| state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_HOE);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		super.onCraftedBy(stack, world, player);
		ToolProgressionHelper.initializeTool(stack, ToolProgressionHelper.ToolType.ULTIMATE);
		UltimateToolModeHelper.setMode(stack, UltimateToolMode.PICKAXE, false);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);
		TraitPoisonHelper.applyMeleePoison(stack, target);
		TraitFreezyHelper.applyMeleeFreeze(stack, target);
		TraitExtendedCombatHelper.applyMeleeEffects(stack, target);
		if (!attacker.level().isClientSide()) {
			float bonus = BONUS_DAMAGE[getPowerMode(stack)];
			if (UltimateToolModeHelper.getMode(stack) == UltimateToolMode.SWORD) {
				bonus *= SWORD_MODE_DAMAGE_MULT[getPowerMode(stack)];
			}
			target.hurt(attacker instanceof Player player ? player.damageSources().playerAttack(player)
				: attacker.damageSources().mobAttack(attacker), bonus);
		}
		return result;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		int power = getPowerMode(stack);
		UltimateToolMode toolMode = UltimateToolModeHelper.getMode(stack);
		list.add(Component.literal("\u00a76All tools fused into one endgame relic"));
		list.add(Component.literal("\u00a7dTool Mode: \u00a7f" + toolMode.displayName()));
		list.add(Component.literal("\u00a7bPower Mode: \u00a7f" + POWER_MODE_NAMES[power]));
		String powerDesc = switch (power) {
			case 0 -> "\u00a7eMining: Near-instant | Damage: +2.0";
			case 1 -> "\u00a7dMining: Diamond-like | Damage: +6.0";
			case 2 -> "\u00a7cMining: One-shot | Damage: +0.5";
			default -> "";
		};
		list.add(Component.literal(powerDesc));
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.ADAPTIVE_I)) {
			list.add(Component.literal("\u00a7aAdaptive: auto-picks tool mode"));
		}
		list.add(Component.literal("\u00a78Hold \u00a7fMode Wheel\u00a78 key for tool modes"));
		list.add(Component.literal("\u00a78Shift + Right-Click: power mode / trait toggles"));
	}

	private static int getPowerMode(ItemStack stack) {
		int mode = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(NBT_POWER_MODE);
		if (mode < 0 || mode >= POWER_MODE_NAMES.length) {
			return 0;
		}
		return mode;
	}

	private static void setPowerMode(ItemStack stack, int mode) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(NBT_POWER_MODE, mode));
	}
}
