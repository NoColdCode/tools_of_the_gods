package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogLeftClickBlockContext;
import net.mcreator.toolsofthegods.logic.context.TogBlockBreakContext;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;

public final class UltimateAdaptiveModeLogic {

	public static void onPlayerTick(Player player) {
		if (player.level().isClientSide() || player.tickCount % 5 != 0) {
			return;
		}
		ItemStack stack = findUltimate(player);
		if (!stack.isEmpty()) {
			UltimateToolModeHelper.tryApplyAdaptiveMode(player, stack);
		}
	}

	public static void onLeftClickBlock(TogLeftClickBlockContext ctx) {
		if (ctx.level().isClientSide()) {
			return;
		}
		ItemStack stack = ctx.itemStack();
		if (stack.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			UltimateToolModeHelper.tryApplyAdaptiveMode(ctx.player(), stack);
		}
	}

	public static void onBreak(TogBlockBreakContext ctx) {
		Player player = ctx.player();
		if (player == null || player.level().isClientSide()) {
			return;
		}
		ItemStack stack = player.getMainHandItem();
		if (stack.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			UltimateToolModeHelper.tryApplyAdaptiveMode(player, stack);
		}
	}

	private static ItemStack findUltimate(Player player) {
		ItemStack main = player.getMainHandItem();
		if (main.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			return main;
		}
		ItemStack off = player.getOffhandItem();
		if (off.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			return off;
		}
		return ItemStack.EMPTY;
	}
}
