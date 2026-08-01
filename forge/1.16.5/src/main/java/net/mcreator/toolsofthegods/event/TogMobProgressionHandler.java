package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TogMobProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingIncomingDamageEvent;
import net.minecraftforge.event.level.BlockDropsEvent;
import net.minecraftforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TogMobProgressionHandler {
	private TogMobProgressionHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TogMobProgressionLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingIncomingDamageEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		TogMobProgressionLogic.onLivingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		TogMobProgressionLogic.onEntityTick(event.getEntity());
	}

}
