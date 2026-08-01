package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSustainingLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.mcreator.toolsofthegods.platform.forge.ForgeBlockDropsEvent;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSustainingHandler {
	private TraitSustainingHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		TraitSustainingLogic.onPlayerTick(event.player);
	}

	@SubscribeEvent
	public static void onBlockDrops(ForgeBlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitSustainingLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		TraitSustainingLogic.onLivingDrops(ForgeEventAdapters.livingDrops(event));
	}

}
