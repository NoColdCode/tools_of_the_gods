package net.mcreator.toolsofthegods.platform.neoforge.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.client.DynamicTextureHandler;
import net.mcreator.toolsofthegods.client.TraitSmithingTableScreen;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;

/**
 * Client bootstrap for NeoForge when common is embedded as a GAMELIBRARY jar.
 * {@code @EventBusSubscriber} classes inside the library jar are not scanned automatically.
 */
@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class NeoForgeClientSetup {
	private NeoForgeClientSetup() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(DynamicTextureHandler::init);
	}

	@SubscribeEvent
	public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
		for (PlayerSkin.Model skin : event.getSkins()) {
			PlayerRenderer renderer = event.getSkin(skin);
			if (renderer != null) {
				renderer.addLayer(new WingsOfTheGodsElytraLayer(renderer, event.getEntityModels()));
			}
		}
	}

	@SubscribeEvent
	public static void onRegisterScreens(RegisterMenuScreensEvent event) {
		event.register(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), TraitSmithingTableScreen::new);
	}
}
