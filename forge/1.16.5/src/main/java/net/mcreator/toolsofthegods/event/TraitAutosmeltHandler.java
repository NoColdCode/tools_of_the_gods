package net.mcreator.toolsofthegods.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.TraitAutosmeltLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class TraitAutosmeltHandler {
	private TraitAutosmeltHandler() {
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		var ctx = ForgeEventAdapters.blockDrops(event);
		TraitAutosmeltLogic.onBlockDrops(ctx);
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		TraitAutosmeltLogic.onLivingDrops(ForgeEventAdapters.livingDrops(event));
	}

}
