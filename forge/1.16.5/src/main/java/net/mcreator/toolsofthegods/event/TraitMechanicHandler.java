package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitMechanicLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockDropsEvent;
import net.minecraftforge.event.tick.PlayerTickEvent;

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
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitMechanicLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		TraitMechanicLogic.onLivingDrops(ForgeEventAdapters.livingDrops(event));
	}

}
