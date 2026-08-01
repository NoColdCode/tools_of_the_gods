package net.mcreator.toolsofthegods.power;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.server.level.ServerPlayer;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.PickaxePowerLogic;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class PickaxePowerManager {
	private PickaxePowerManager() {
	}

	public static void activatePower(net.minecraft.server.level.ServerPlayer player) {
		PickaxePowerLogic.activatePower(player);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		if (event.player instanceof ServerPlayer player) {
			PickaxePowerLogic.onPlayerTick(player);
		}
	}
}
