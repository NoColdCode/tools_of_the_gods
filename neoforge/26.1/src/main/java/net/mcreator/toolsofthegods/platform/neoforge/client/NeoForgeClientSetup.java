package net.mcreator.toolsofthegods.platform.neoforge.client;



import net.neoforged.api.distmarker.Dist;

import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;



import net.mcreator.toolsofthegods.TogModConstants;

import net.mcreator.toolsofthegods.client.DynamicTextureHandler;

import net.mcreator.toolsofthegods.client.DynamicTextureHandler.ToolTierProperty;

import net.mcreator.toolsofthegods.client.TraitSmithingTableScreen;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;



@EventBusSubscriber(modid = TogModConstants.MODID, value = Dist.CLIENT)

public final class NeoForgeClientSetup {

	private NeoForgeClientSetup() {

	}



	@SubscribeEvent

	public static void onClientSetup(FMLClientSetupEvent event) {

		event.enqueueWork(DynamicTextureHandler::init);

	}



	@SubscribeEvent

	public static void onRegisterRangeProperties(RegisterRangeSelectItemModelPropertyEvent event) {

		event.register(DynamicTextureHandler.TIER_PROPERTY_ID, ToolTierProperty.MAP_CODEC);

	}



	@SubscribeEvent

	public static void onRegisterScreens(RegisterMenuScreensEvent event) {

		event.register(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), TraitSmithingTableScreen::new);

	}

}

