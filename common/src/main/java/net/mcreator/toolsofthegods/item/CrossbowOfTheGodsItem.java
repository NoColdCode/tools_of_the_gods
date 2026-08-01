package net.mcreator.toolsofthegods.item;


import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

public class CrossbowOfTheGodsItem extends CrossbowItem {
	public CrossbowOfTheGodsItem() {
		super(new Item.Properties().fireResistant());
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return super.use(world, player, hand);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
		super.releaseUsing(stack, world, entity, timeLeft);
		TraitPoisonHelper.applyCrossbowPoison(stack, world, entity);
		TraitFreezyHelper.applyCrossbowFreeze(stack, world, entity);
		TraitExtendedCombatHelper.applyCrossbowEffects(stack, world, entity);
		if (!world.isClientSide() && CrossbowItem.isCharged(stack)) {
			ToolProgressionHelper.gainXp(world, entity.getX(), entity.getY(), entity.getZ(), entity, stack, 2);
		}
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		int base = super.getUseDuration(stack, entity);
		if (TraitSystem.hasQuickLoad(stack)) {
			return Math.max(1, (int) (base * 0.75f));
		}
		return base;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		super.onCraftedBy(stack, world, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.CROSSBOW);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		TogItemUtils.appendTogTooltip(stack, context, list, flag);
	}
}
