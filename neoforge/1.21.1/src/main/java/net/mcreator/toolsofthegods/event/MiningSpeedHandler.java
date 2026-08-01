package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.MiningSpeedLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class MiningSpeedHandler {
	private MiningSpeedHandler() {
	}

	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		var ctx = NeoForgeEventAdapters.breakSpeed(event);
		MiningSpeedLogic.onBreakSpeed(ctx);
		NeoForgeEventAdapters.applyBreakSpeed(event, ctx);
	}

}
