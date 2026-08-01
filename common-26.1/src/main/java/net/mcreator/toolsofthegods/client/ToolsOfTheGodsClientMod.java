package net.mcreator.toolsofthegods.client;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;

@EventBusSubscriber(modid = TogModConstants.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ToolsOfTheGodsClientMod {
	private ToolsOfTheGodsClientMod() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			DynamicTextureHandler.init();
			ModLoadingContext.get().getActiveContainer().registerExtensionPoint(
				net.neoforged.neoforge.client.gui.IConfigScreenFactory.class,
				(modContainer, parentScreen) -> new net.neoforged.neoforge.client.gui.ConfigurationScreen(modContainer, parentScreen)
			);
		});
	}

	@SubscribeEvent
	public static void onRegisterScreens(RegisterMenuScreensEvent event) {
		event.register(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), TraitSmithingTableScreen::new);
	}
}
