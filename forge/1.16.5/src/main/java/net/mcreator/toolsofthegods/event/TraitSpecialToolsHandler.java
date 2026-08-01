package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSpecialToolsLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingIncomingDamageEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSpecialToolsHandler {
	private TraitSpecialToolsHandler() {
	}

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		TraitSpecialToolsLogic.onItemFished(ForgeEventAdapters.itemFished(event));
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		TraitSpecialToolsLogic.onPlayerTick(event.getEntity());
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		TraitSpecialToolsLogic.onLivingIncomingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
