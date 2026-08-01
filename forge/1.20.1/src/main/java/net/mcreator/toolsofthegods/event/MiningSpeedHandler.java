package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.MiningSpeedLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class MiningSpeedHandler {
	private MiningSpeedHandler() {
	}

	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		var ctx = ForgeEventAdapters.breakSpeed(event);
		MiningSpeedLogic.onBreakSpeed(ctx);
		ForgeEventAdapters.applyBreakSpeed(event, ctx);
	}

}
