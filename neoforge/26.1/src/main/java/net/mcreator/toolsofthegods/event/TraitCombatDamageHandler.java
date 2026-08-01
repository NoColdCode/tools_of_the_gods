package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitCombatDamageLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitCombatDamageHandler {
	private TraitCombatDamageHandler() {
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		var ctx = NeoForgeEventAdapters.incomingDamage(event);
		TraitCombatDamageLogic.onLivingIncomingDamage(ctx);
		NeoForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
