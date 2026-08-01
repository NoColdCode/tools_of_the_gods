package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.UltimateAdaptiveModeLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class UltimateAdaptiveModeHandler {
	private UltimateAdaptiveModeHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		UltimateAdaptiveModeLogic.onPlayerTick(event.player);
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		UltimateAdaptiveModeLogic.onLeftClickBlock(ForgeEventAdapters.leftClickBlock(event));
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {
		UltimateAdaptiveModeLogic.onBreak(ForgeEventAdapters.blockBreak(event));
	}

}
