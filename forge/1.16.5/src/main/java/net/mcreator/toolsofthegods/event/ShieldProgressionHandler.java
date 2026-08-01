package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.ShieldProgressionLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingShieldBlockEvent;
import net.minecraftforge.event.tick.PlayerTickEvent;

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
		var ctx = ForgeEventAdapters.shieldBlock(event);
		ShieldProgressionLogic.onShieldBlock(ctx);
		ForgeEventAdapters.applyShieldBlock(event, ctx);
	}

	@SubscribeEvent
	public static void onDamagePost(LivingDamageEvent.Post event) {
		ShieldProgressionLogic.onDamagePost(ForgeEventAdapters.livingDamagePost(event));
	}

}
