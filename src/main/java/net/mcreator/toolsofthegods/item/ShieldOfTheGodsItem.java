package net.mcreator.toolsofthegods.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

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

import net.mcreator.toolsofthegods.event.TraitRepulseHandler;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import java.util.List;

public class ShieldOfTheGodsItem extends ShieldItem {

	public ShieldOfTheGodsItem() {
		super(new Item.Properties().fireResistant());
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		if (!world.isClientSide()) {
			TraitRepulseHandler.tryRepulse(player, itemstack);
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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		TogItemUtils.appendTogTooltip(itemstack, context, list, flag);
		float blockChance = ToolProgressionHelper.getShieldBlockChance(itemstack) * 100f;
		list.add(Component.literal("§7Block chance: §b" + String.format("%.0f%%", blockChance)));
		list.add(Component.literal("§8Blocks melee and arrows while raised"));
		if (TraitSystem.hasTrait(itemstack, TraitSystem.Trait.REPULSE_I)) {
			list.add(Component.literal("§8Right-click: Repulse nearby foes"));
		}
	}
}
