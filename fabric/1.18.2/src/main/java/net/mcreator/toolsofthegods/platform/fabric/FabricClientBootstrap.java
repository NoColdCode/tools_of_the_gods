package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.api.ClientModInitializer;

import net.mcreator.toolsofthegods.platform.fabric.client.FabricDynamicTextureHandler;

public final class FabricClientBootstrap implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FabricDynamicTextureHandler.init();
	}
}
