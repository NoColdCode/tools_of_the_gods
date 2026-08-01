package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

import net.mcreator.toolsofthegods.client.TraitSmithingTableScreen;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;
import net.mcreator.toolsofthegods.platform.fabric.client.FabricDynamicTextureHandler;

public final class FabricClientBootstrap implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FabricDynamicTextureHandler.init();
		MenuScreens.register(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), TraitSmithingTableScreen::new);
	}
}
