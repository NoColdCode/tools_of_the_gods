package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSoulboundLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSoulboundHandler {
	private TraitSoulboundHandler() {
	}

	@SubscribeEvent
	public static void onPlayerDrops(LivingDropsEvent event) {
		TraitSoulboundLogic.onPlayerDrops(ForgeEventAdapters.livingDrops(event));
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		TraitSoulboundLogic.onPlayerClone(ForgeEventAdapters.playerClone(event));
	}

}
