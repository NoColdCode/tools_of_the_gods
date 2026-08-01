package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSpecialToolsLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSpecialToolsHandler {
	private TraitSpecialToolsHandler() {
	}

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		TraitSpecialToolsLogic.onItemFished(NeoForgeEventAdapters.itemFished(event));
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		TraitSpecialToolsLogic.onPlayerTick(event.getEntity());
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		var ctx = NeoForgeEventAdapters.incomingDamage(event);
		TraitSpecialToolsLogic.onLivingIncomingDamage(ctx);
		NeoForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
