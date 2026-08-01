package net.mcreator.toolsofthegods.command;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.PickaxeCommandsLogic;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class PickaxeCommands {
	private PickaxeCommands() {
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		PickaxeCommandsLogic.register(event.getDispatcher());
	}
}
