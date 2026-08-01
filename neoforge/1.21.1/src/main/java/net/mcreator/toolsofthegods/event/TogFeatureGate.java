package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TogFeatureGateLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TogFeatureGate {
	private TogFeatureGate() {
	}

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		var ctx = NeoForgeEventAdapters.rightClickItem(event);
		TogFeatureGateLogic.onRightClickItem(ctx);
		NeoForgeEventAdapters.applyRightClickItem(event, ctx);
	}

}
