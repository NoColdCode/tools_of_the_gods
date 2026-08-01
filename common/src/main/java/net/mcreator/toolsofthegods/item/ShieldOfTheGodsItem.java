package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.Level;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.logic.TraitRepulseLogic;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import java.util.List;

public class ShieldOfTheGodsItem extends ShieldItem {

	public ShieldOfTheGodsItem() {
		super(new Item.Properties().fireResistant());
	}


	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!world.isClientSide()) {
			TraitRepulseLogic.tryRepulse(player, itemstack);
		}
		return super.use(world, player, hand);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player player) {
		super.onCraftedBy(itemstack, world, player);
		TogItemUtils.onTogCrafted(itemstack, ToolProgressionHelper.ToolType.SHIELD);
	}

	@Override
	public Component getName(ItemStack itemstack) {
		return TogItemUtils.togDisplayName(itemstack);
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		TogItemUtils.appendTogTooltip(itemstack, context, list, flag);
	}
}
