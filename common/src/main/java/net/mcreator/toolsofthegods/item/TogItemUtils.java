package net.mcreator.toolsofthegods.item;



import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResultHolder;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.level.Level;



import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;

import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;



import java.util.List;



public final class TogItemUtils {

	private TogItemUtils() {

	}



	public static InteractionResultHolder<ItemStack> handleShiftUpgrade(Level world, Player player, InteractionHand hand) {
		if (!player.isShiftKeyDown()) {
			return null;
		}

		ItemStack stack = player.getItemInHand(hand);
		ToolProgressionHelper.ensureInitialized(stack);
		if (!ToolProgressionHelper.needsUpgrade(stack)) {
			return null;
		}

		if (world.isClientSide()) {
			return InteractionResultHolder.success(stack);
		}

		UpgradePickaxeProcedure.execute(world, player.getX(), player.getY(), player.getZ(), player, stack);
		return InteractionResultHolder.success(stack);
	}



	public static void onTogCrafted(ItemStack stack, ToolProgressionHelper.ToolType type) {

		ToolProgressionHelper.initializeTool(stack, type);

	}



	public static Component togDisplayName(ItemStack stack) {

		return Component.literal(ToolProgressionHelper.getDisplayName(stack));

	}



	public static void appendTogTooltip(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {

		String hoverText = PrimalWoodenToolsPickaxeSpecialInformationProcedure.execute(stack);

		if (hoverText != null) {

			for (String line : hoverText.split("\n")) {

				list.add(Component.literal(line));

			}

		}

		list.add(Component.literal("§8Shift + Right-Click to upgrade"));

	}

}


