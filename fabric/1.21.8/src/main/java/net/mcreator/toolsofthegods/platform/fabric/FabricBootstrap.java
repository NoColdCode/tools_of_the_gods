package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.api.ModInitializer;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.platform.TogPlatforms;

public final class FabricBootstrap implements ModInitializer {
	@Override
	public void onInitialize() {
		TogPlatforms.init(new FabricTogPlatform());
		FabricModContent.register();
		FabricTogItemComponents.register();
		FabricGameplayEvents.register();
		TogModConstants.LOGGER.info("Tools of the Gods {} loaded on Fabric {}", TogModConstants.VERSION, TogModConstants.MODID);
	}
}
