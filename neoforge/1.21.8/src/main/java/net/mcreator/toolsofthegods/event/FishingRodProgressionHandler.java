package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.FishingRodProgressionLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class FishingRodProgressionHandler {
	private FishingRodProgressionHandler() {
	}

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		FishingRodProgressionLogic.onItemFished(NeoForgeEventAdapters.itemFished(event));
	}

}
