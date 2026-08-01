package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.GuideBookOpenLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class GuideBookOpenHandler {
	private GuideBookOpenHandler() {
	}

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		var ctx = NeoForgeEventAdapters.rightClickItem(event);
		GuideBookOpenLogic.onRightClickItem(ctx);
		NeoForgeEventAdapters.applyRightClickItem(event, ctx);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		var ctx = NeoForgeEventAdapters.rightClickBlock(event);
		GuideBookOpenLogic.onRightClickBlock(ctx);
		NeoForgeEventAdapters.applyRightClickBlock(event, ctx);
	}

}
