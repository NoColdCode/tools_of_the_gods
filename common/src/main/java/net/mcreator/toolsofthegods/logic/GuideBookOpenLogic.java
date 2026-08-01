package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogRightClickItemContext;
import net.mcreator.toolsofthegods.logic.context.TogRightClickBlockContext;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.util.GuideBookOpener;

public final class GuideBookOpenLogic {

	public static void onRightClickItem(TogRightClickItemContext ctx) {
		if (ctx.level().isClientSide() || !(ctx.player() instanceof ServerPlayer player)) {
			return;
		}
		if (!ctx.itemStack().is(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK.get())) {
			return;
		}

		GuideBookOpener.open(player);
		ctx.setCancellationResult(InteractionResult.SUCCESS);
		ctx.setCanceled(true);
	}

	public static void onRightClickBlock(TogRightClickBlockContext ctx) {
		if (ctx.level().isClientSide() || !(ctx.player() instanceof ServerPlayer player)) {
			return;
		}
		if (!ctx.itemStack().is(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK.get())) {
			return;
		}

		GuideBookOpener.open(player);
		ctx.setDenyBlockUse(true);
		ctx.setDenyItemUse(true);
		ctx.setCancellationResult(InteractionResult.SUCCESS);
		ctx.setCanceled(true);
	}
}
