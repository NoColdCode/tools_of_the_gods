package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitThornsLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitThornsHandler {
	private TraitThornsHandler() {
	}

	@SubscribeEvent
	public static void onPlayerDamaged(LivingIncomingDamageEvent event) {
		var ctx = NeoForgeEventAdapters.incomingDamage(event);
		TraitThornsLogic.onPlayerDamaged(ctx);
		NeoForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
