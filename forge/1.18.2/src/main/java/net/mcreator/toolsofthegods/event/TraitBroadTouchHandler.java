package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitBroadTouchLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.eventbus.api.EventPriority;
import net.mcreator.toolsofthegods.platform.forge.ForgeBlockDropsEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitBroadTouchHandler {
	private TraitBroadTouchHandler() {
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onBlockDrops(ForgeBlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitBroadTouchLogic.onBlockDrops(ctx);
	}

}
