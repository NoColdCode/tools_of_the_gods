package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.Level;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.logic.TraitRepulseLogic;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class ShieldOfTheGodsItem extends ShieldItem {

	public ShieldOfTheGodsItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).fireResistant());
	}


	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide()) {
			TraitRepulseLogic.tryRepulse(player, player.getItemInHand(hand));
		}
		return super.use(world, player, hand);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Player player) {
		super.onCraftedBy(itemstack, player);
		TogItemUtils.onTogCrafted(itemstack, ToolProgressionHelper.ToolType.SHIELD);
	}

	@Override
	public Component getName(ItemStack itemstack) {
		return TogItemUtils.togDisplayName(itemstack);
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, display, tooltipAdder, flag);
		TogItemUtils.appendTogTooltip(itemstack, context, display, tooltipAdder, flag);
	}
}
