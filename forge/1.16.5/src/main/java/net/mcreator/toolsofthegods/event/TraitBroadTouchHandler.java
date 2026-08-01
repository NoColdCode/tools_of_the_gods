package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitBroadTouchLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitBroadTouchHandler {
	private TraitBroadTouchHandler() {
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitBroadTouchLogic.onBlockDrops(ctx);
	}

}
