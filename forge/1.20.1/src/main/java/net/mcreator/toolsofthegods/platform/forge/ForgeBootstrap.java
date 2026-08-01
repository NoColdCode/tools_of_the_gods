package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.platform.TogPlatforms;

@Mod(ToolsOfTheGodsMod.MODID)
public final class ForgeBootstrap {
	public ForgeBootstrap() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		TogPlatforms.init(new ForgeTogPlatform());
		new ToolsOfTheGodsMod(modEventBus);
		ForgeModContent.register(modEventBus);
	}
}
