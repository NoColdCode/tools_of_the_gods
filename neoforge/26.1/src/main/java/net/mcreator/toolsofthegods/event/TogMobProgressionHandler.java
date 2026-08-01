package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TogMobProgressionLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TogMobProgressionHandler {
	private TogMobProgressionHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = NeoForgeEventAdapters.blockDrops(event);
		TogMobProgressionLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingIncomingDamageEvent event) {
		var ctx = NeoForgeEventAdapters.incomingDamage(event);
		TogMobProgressionLogic.onLivingDamage(ctx);
		NeoForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		TogMobProgressionLogic.onEntityTick(event.getEntity());
	}

}
