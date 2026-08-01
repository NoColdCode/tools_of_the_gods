package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitCombatDamageLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitCombatDamageHandler {
	private TraitCombatDamageHandler() {
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingHurtEvent event) {
		var ctx = ForgeEventAdapters.incomingDamage(event);
		TraitCombatDamageLogic.onLivingIncomingDamage(ctx);
		ForgeEventAdapters.applyIncomingDamage(event, ctx);
	}

}
