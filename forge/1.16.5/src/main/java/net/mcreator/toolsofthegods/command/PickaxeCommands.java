package net.mcreator.toolsofthegods.command;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;
import net.minecraftforge.event.RegisterCommandsEvent;

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
