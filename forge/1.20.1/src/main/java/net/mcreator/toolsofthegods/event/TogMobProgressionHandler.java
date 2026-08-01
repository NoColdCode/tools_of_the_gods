package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TogMobProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.mcreator.toolsofthegods.platform.forge.ForgeBlockDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TogMobProgressionHandler {
	private TogMobProgressionHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(ForgeBlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TogMobProgressionLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingHurtEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		TogMobProgressionLogic.onLivingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
		TogMobProgressionLogic.onEntityTick(event.getEntity());
	}

}
