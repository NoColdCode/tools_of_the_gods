package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitMomentumLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitMomentumHandler {
	private TraitMomentumHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitMomentumLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onPlayerTick(net.minecraftforge.event.tick.PlayerTickEvent.Post event) {
		TraitMomentumLogic.onPlayerTick(event.getEntity());
	}

}
