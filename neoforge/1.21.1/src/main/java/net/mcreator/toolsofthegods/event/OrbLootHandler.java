package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.OrbLootLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class OrbLootHandler {
	private OrbLootHandler() {
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		OrbLootLogic.onLivingDrops(NeoForgeEventAdapters.livingDrops(event));
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		OrbLootLogic.onBlockBreak(NeoForgeEventAdapters.blockBreak(event));
	}

}
