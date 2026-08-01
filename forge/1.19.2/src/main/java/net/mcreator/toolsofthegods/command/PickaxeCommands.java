package net.mcreator.toolsofthegods.command;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.PickaxeCommandsLogic;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class PickaxeCommands {
	private PickaxeCommands() {
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		PickaxeCommandsLogic.register(event.getDispatcher());
	}
}
