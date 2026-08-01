package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.FishingRodProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.player.ItemFishedEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class FishingRodProgressionHandler {
	private FishingRodProgressionHandler() {
	}

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		FishingRodProgressionLogic.onItemFished(ForgeEventAdapters.itemFished(event));
	}

}
