package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.ShieldProgressionLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class ShieldProgressionHandler {
	private ShieldProgressionHandler() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		ShieldProgressionLogic.onPlayerTick(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		var ctx = NeoForgeEventAdapters.shieldBlock(event);
		ShieldProgressionLogic.onShieldBlock(ctx);
		NeoForgeEventAdapters.applyShieldBlock(event, ctx);
	}

	@SubscribeEvent
	public static void onDamagePost(LivingDamageEvent.Post event) {
		ShieldProgressionLogic.onDamagePost(NeoForgeEventAdapters.livingDamagePost(event));
	}

}
