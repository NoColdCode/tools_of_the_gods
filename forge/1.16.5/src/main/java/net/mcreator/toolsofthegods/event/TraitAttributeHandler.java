package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitAttributeLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitAttributeHandler {
	private TraitAttributeHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		TraitAttributeLogic.onPlayerTick(event.getEntity());
	}

}
