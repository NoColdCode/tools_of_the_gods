package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.GuideBookOpenLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class GuideBookOpenHandler {
	private GuideBookOpenHandler() {
	}

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		var ctx = ForgeEventAdapters.rightClickItem(event);
		GuideBookOpenLogic.onRightClickItem(ctx);
		ForgeEventAdapters.applyRightClickItem(event, ctx);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		var ctx = ForgeEventAdapters.rightClickBlock(event);
		GuideBookOpenLogic.onRightClickBlock(ctx);
		ForgeEventAdapters.applyRightClickBlock(event, ctx);
	}

}
