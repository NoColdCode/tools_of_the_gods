package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TogToolMaterials;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.SpearThrustHelper;


public class SpearOfTheGodsItem extends Item {
	public SpearOfTheGodsItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).sword(TogToolMaterials.PRIMAL, 0f, -2.6f).fireResistant());
	}


	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		InteractionResult upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		player.startUsingItem(hand);
		return InteractionResult.CONSUME;
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
		int chargeTicks = this.getUseDuration(stack, entity) - timeLeft;
		if (entity instanceof Player player) {
			SpearThrustHelper.performThrust(player, stack, chargeTicks);
		}
		return true;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(stack, target, attacker);
		TraitPoisonHelper.applyMeleePoison(stack, target);
		TraitFreezyHelper.applyMeleeFreeze(stack, target);
		TraitExtendedCombatHelper.applyMeleeEffects(stack, target);
		if (attacker instanceof Player player) {
			ToolProgressionHelper.gainXp(attacker.level(), attacker.getX(), attacker.getY(), attacker.getZ(), player, stack,
				ToolProgressionHelper.getSwordHitXp(target));
		}
	}

	@Override
	public void onCraftedBy(ItemStack stack, Player player) {
		super.onCraftedBy(stack, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.SPEAR);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
		TogItemUtils.appendTogTooltip(stack, context, display, tooltipAdder, flag);
		tooltipAdder.accept(Component.literal("§7Hold §fUse§7 to charge a long thrust"));
		tooltipAdder.accept(Component.literal("§7Charge faster on a horse"));
		tooltipAdder.accept(Component.literal("§7Max reach: §b" + String.format("%.1f", SpearThrustHelper.getDisplayedReach(stack)) + " blocks"));
	}
}
