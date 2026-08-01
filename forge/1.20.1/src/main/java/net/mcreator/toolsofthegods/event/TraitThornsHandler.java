package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitThornsLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitThornsHandler {
	private TraitThornsHandler() {
	}

	@SubscribeEvent
	public static void onPlayerDamaged(LivingHurtEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		TraitThornsLogic.onPlayerDamaged(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
