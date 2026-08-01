package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitVitalityLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitVitalityHandler {
	private TraitVitalityHandler() {
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		TraitVitalityLogic.onLivingDeath(NeoForgeEventAdapters.livingDeath(event));
	}

}
