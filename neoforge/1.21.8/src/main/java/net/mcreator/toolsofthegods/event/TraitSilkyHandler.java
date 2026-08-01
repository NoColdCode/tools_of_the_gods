package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSilkyLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSilkyHandler {
	private TraitSilkyHandler() {
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = NeoForgeEventAdapters.blockDrops(event);
		TraitSilkyLogic.onBlockDrops(ctx);
	}

}
