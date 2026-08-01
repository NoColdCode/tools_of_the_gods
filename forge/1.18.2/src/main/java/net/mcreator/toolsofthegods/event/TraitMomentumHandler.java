package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitMomentumLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.mcreator.toolsofthegods.platform.forge.ForgeBlockDropsEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitMomentumHandler {
	private TraitMomentumHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(ForgeBlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitMomentumLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
		if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) {
			return;
		}
		TraitMomentumLogic.onPlayerTick(event.player);
	}

}
