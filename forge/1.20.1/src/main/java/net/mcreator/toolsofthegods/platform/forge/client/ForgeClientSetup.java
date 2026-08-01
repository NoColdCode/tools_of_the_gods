package net.mcreator.toolsofthegods.platform.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraft.client.gui.screens.MenuScreens;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.client.TraitSmithingTableScreen;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;

@Mod.EventBusSubscriber(modid = TogModConstants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ForgeClientSetup {
	private ForgeClientSetup() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ForgeDynamicTextureHandler.init();
			MenuScreens.register(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), TraitSmithingTableScreen::new);
		});
	}
}
