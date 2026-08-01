package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitMechanicLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitMechanicHandler {
	private TraitMechanicHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		TraitMechanicLogic.onPlayerTick(event.getEntity());
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = NeoForgeEventAdapters.blockDrops(event);
		TraitMechanicLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		TraitMechanicLogic.onLivingDrops(NeoForgeEventAdapters.livingDrops(event));
	}

}
