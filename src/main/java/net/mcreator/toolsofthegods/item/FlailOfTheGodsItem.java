package net.mcreator.toolsofthegods.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.util.FlailCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

public class FlailOfTheGodsItem extends SwordItem {
	private static final Tier TOOL_TIER = new Tier() {
		@Override
		public int getUses() {
			return 250;
		}

		@Override
		public float getSpeed() {
			return 1.0f;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 10;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(Items.CHAIN);
		}
	};

	public FlailOfTheGodsItem() {
		super(TOOL_TIER, new Item.Properties().attributes(SwordItem.createAttributes(TOOL_TIER, 0f, -3.5f)).fireResistant());
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean ret = super.hurtEnemy(stack, target, attacker);
		int level = FlailCombatHelper.getLevel(stack);
		FlailCombatHelper.applyStun(target, level);
		TraitPoisonHelper.applyMeleePoison(stack, target);
		TraitFreezyHelper.applyMeleeFreeze(stack, target);
		if (attacker instanceof Player player) {
			ToolProgressionHelper.gainXp(attacker.level(), attacker.getX(), attacker.getY(), attacker.getZ(), player, stack,
				ToolProgressionHelper.getSwordHitXp(target));
		} else {
			ToolProgressionHelper.gainXp(attacker.level(), attacker.getX(), attacker.getY(), attacker.getZ(), attacker, stack,
				ToolProgressionHelper.getSwordHitXp(target));
		}
		return ret;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		super.onCraftedBy(stack, world, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.FLAIL);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		TogItemUtils.appendTogTooltip(stack, context, list, flag);
		int level = FlailCombatHelper.getLevel(stack);
		list.add(Component.literal("§7Stun: §d" + String.format("%.1f", FlailCombatHelper.getStunSecondsForDisplay(level)) + "s §8(boss ×0.5)"));
		list.add(Component.literal("§7Attack speed: §b" + String.format("%.2f", FlailCombatHelper.getDisplayedAttackSpeed(level))));
	}
}
