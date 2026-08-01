package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogRightClickItemContext;


import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.mcreator.toolsofthegods.util.TogFeatures;

public final class TogFeatureGateLogic {

	public static void onRightClickItem(TogRightClickItemContext ctx) {
		if (TogFeatures.extendedToolsEnabled() || !TogFeatures.isExtendedTool(ctx.itemStack())) {
			return;
		}
		ctx.setCanceled(true);
		if (!ctx.level().isClientSide() && ctx.player() instanceof Player player) {
			player.displayClientMessage(Component.literal("§8This item is disabled in this pack."), true);
		}
	}
}
