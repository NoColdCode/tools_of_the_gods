package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSoulboundLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSoulboundHandler {
	private TraitSoulboundHandler() {
	}

	@SubscribeEvent
	public static void onPlayerDrops(LivingDropsEvent event) {
		TraitSoulboundLogic.onPlayerDrops(NeoForgeEventAdapters.livingDrops(event));
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		TraitSoulboundLogic.onPlayerClone(NeoForgeEventAdapters.playerClone(event));
	}

}
