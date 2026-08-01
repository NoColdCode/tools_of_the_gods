package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.UltimateAdaptiveModeLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class UltimateAdaptiveModeHandler {
	private UltimateAdaptiveModeHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		UltimateAdaptiveModeLogic.onPlayerTick(event.getEntity());
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		UltimateAdaptiveModeLogic.onLeftClickBlock(NeoForgeEventAdapters.leftClickBlock(event));
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {
		UltimateAdaptiveModeLogic.onBreak(NeoForgeEventAdapters.blockBreak(event));
	}

}
