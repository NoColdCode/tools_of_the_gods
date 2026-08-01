package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.ShieldProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class ShieldProgressionHandler {
	private ShieldProgressionHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		ShieldProgressionLogic.onPlayerTick(event.player);
	}

	@SubscribeEvent
	public static void onDamagePost(LivingHurtEvent event) {
		ShieldProgressionLogic.onDamagePost(ForgeEventAdapters.livingDamagePost(event));
	}
}
