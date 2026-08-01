package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.ArmorProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingIncomingDamageEvent;
import net.minecraftforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class ArmorProgressionHandler {
	private ArmorProgressionHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		ArmorProgressionLogic.onPlayerTick(event.getEntity());
	}

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		ArmorProgressionLogic.onIncomingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
