package net.mcreator.toolsofthegods.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class TraitSmithingTableScreenRegister {
	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), TraitSmithingTableScreen::new);
	}
}