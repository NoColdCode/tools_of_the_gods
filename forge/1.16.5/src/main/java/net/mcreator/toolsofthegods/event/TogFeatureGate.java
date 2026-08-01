package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TogFeatureGateLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TogFeatureGate {
	private TogFeatureGate() {
	}

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		var ctx = ForgeEventAdapters.rightClickItem(event);
		TogFeatureGateLogic.onRightClickItem(ctx);
		ForgeEventAdapters.applyRightClickItem(event, ctx);
	}

}
