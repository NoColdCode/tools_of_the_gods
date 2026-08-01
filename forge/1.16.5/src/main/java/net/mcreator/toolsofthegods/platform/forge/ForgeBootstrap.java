package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.platform.TogPlatforms;

/**
 * NeoForge 1.21.1 entry point. Shared mod logic lives in the {@code :common} project.
 */
@Mod(ToolsOfTheGodsMod.MODID)
public final class ForgeBootstrap {
	public ForgeBootstrap(IEventBus modEventBus) {
		TogPlatforms.init(new ForgeTogPlatform());
		new ToolsOfTheGodsMod(modEventBus);
		ForgeModContent.register(modEventBus);
	}
}
