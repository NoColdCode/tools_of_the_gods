package net.mcreator.toolsofthegods.power;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;
import net.minecraftforge.event.tick.PlayerTickEvent;

import net.minecraft.server.level.ServerPlayer;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.PickaxePowerLogic;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class PickaxePowerManager {
	private PickaxePowerManager() {
	}

	public static void activatePower(net.minecraft.server.level.ServerPlayer player) {
		PickaxePowerLogic.activatePower(player);
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			PickaxePowerLogic.onPlayerTick(player);
		}
	}
}
