package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitBroadTouchLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitBroadTouchHandler {
	private TraitBroadTouchHandler() {
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = NeoForgeEventAdapters.blockDrops(event);
		TraitBroadTouchLogic.onBlockDrops(ctx);
	}

}
