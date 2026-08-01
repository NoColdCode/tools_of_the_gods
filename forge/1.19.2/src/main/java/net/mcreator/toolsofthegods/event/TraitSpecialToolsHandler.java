package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitSpecialToolsLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitSpecialToolsHandler {
	private TraitSpecialToolsHandler() {
	}

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		TraitSpecialToolsLogic.onItemFished(ForgeEventAdapters.itemFished(event));
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		TraitSpecialToolsLogic.onPlayerTick(event.player);
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingHurtEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		TraitSpecialToolsLogic.onLivingIncomingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
