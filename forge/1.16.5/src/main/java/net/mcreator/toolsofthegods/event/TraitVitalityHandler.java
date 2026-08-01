package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitVitalityLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitVitalityHandler {
	private TraitVitalityHandler() {
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		TraitVitalityLogic.onLivingDeath(ForgeEventAdapters.livingDeath(event));
	}

}
