package net.mcreator.toolsofthegods.integration;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;

import java.util.List;

@Mod.EventBusSubscriber(modid = TogModConstants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TogRecipeViewerIntegration {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			if (ModList.get().isLoaded("jei")) {
				registerJeiInfo();
			}
		});
	}

	private static void registerJeiInfo() {
		try {
			Class.forName("mezz.jei.api.IModPlugin");
		} catch (ClassNotFoundException ignored) {
		}
	}

	public static List<Component> traitBindingLines() {
		return List.of(
			new TextComponent("§6Trait Smithing Table"),
			new TextComponent("§7Place a TOG item + one ingredient, then Apply."),
			new TextComponent("§bRedstone Block§r → Speedy"),
			new TextComponent("§bBread§r → Sustaining"),
			new TextComponent("§bBrick§r → Bulwark"),
			new TextComponent("§bPiston§r → Repulse (shield)"),
			new TextComponent("§bCactus§r → Thorns (armor)"),
			new TextComponent("§bIron Block§r → Guardian (shield)"),
			new TextComponent("§7See the Guide Book for the full list.")
		);
	}

	public static List<ItemStack> gemStacks() {
		return List.of(
			new ItemStack(ToolsOfTheGodsOrbItems.WHITE_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.YELLOW_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.PURPLE_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.RED_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.BLACK_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.GREEN_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.BLUE_GEM.get()),
			new ItemStack(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get())
		);
	}
}
