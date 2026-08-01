package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.ArmorProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class ArmorProgressionHandler {
	private ArmorProgressionHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		ArmorProgressionLogic.onPlayerTick(event.player);
	}

	@SubscribeEvent
	public static void onIncomingDamage(LivingHurtEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		ArmorProgressionLogic.onIncomingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
