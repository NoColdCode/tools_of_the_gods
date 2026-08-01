package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogItemFishedContext;


import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public final class FishingRodProgressionLogic {

	public static void onItemFished(TogItemFishedContext ctx) {
		if (ctx.player().level().isClientSide()) {
			return;
		}
		ItemStack rod = ctx.player().getMainHandItem();
		if (ToolProgressionHelper.getToolType(rod) != ToolProgressionHelper.ToolType.FISHING_ROD) {
			rod = ctx.player().getOffhandItem();
		}
		if (ToolProgressionHelper.getToolType(rod) == ToolProgressionHelper.ToolType.FISHING_ROD) {
			ToolProgressionHelper.gainXp(ctx.player().level(), ctx.player().getX(), ctx.player().getY(),
				ctx.player().getZ(), ctx.player(), rod, 4);
		}
	}
}
