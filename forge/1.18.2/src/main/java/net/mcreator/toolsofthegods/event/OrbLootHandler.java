package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.OrbLootLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class OrbLootHandler {
	private OrbLootHandler() {
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		OrbLootLogic.onLivingDrops(ForgeEventAdapters.livingDrops(event));
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		OrbLootLogic.onBlockBreak(ForgeEventAdapters.blockBreak(event));
	}

}
