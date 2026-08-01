package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitMomentumLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitMomentumHandler {
	private TraitMomentumHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = NeoForgeEventAdapters.blockDrops(event);
		TraitMomentumLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
		TraitMomentumLogic.onPlayerTick(event.getEntity());
	}

}
