package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.platform.TogPlatforms;

/**
 * NeoForge 1.21.1 entry point. Shared mod logic lives in the {@code :common} project.
 */
@Mod(ToolsOfTheGodsMod.MODID)
public final class NeoForgeBootstrap {
	public NeoForgeBootstrap(IEventBus modEventBus) {
		TogPlatforms.init(new NeoForgeTogPlatform());
		new ToolsOfTheGodsMod(modEventBus);
		NeoForgeModContent.register(modEventBus);
	}
}
