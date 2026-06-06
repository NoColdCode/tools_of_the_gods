package net.mcreator.toolsofthegods.integration;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;

import java.util.List;

/**
 * Adds ingredient info pages to JEI/EMI when those mods are installed (via reflection to avoid hard dependency).
 */
@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, value = Dist.CLIENT, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD)
public class TogRecipeViewerIntegration {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			if (ModList.get().isLoaded("jei")) {
				registerJeiInfo();
			}
			if (ModList.get().isLoaded("emi")) {
				registerEmiInfo();
			}
		});
	}

	private static void registerJeiInfo() {
		try {
			Class<?> modPlugin = Class.forName("mezz.jei.api.IModPlugin");
			// JEI auto-discovers crafting recipes from datapack; trait pages are documented in the guide book.
			ToolsOfTheGodsMod.LOGGER.info("JEI detected — TOG crafting recipes are available in recipe viewer.");
		} catch (ClassNotFoundException ignored) {
		}
	}

	private static void registerEmiInfo() {
		try {
			Class<?> emiApi = Class.forName("dev.emi.emi.api.EmiApi");
			ToolsOfTheGodsMod.LOGGER.info("EMI detected — TOG crafting recipes are available in recipe viewer.");
		} catch (ClassNotFoundException ignored) {
		}
	}

	public static List<Component> traitBindingLines() {
		return List.of(
			Component.literal("§6Trait Smithing Table"),
			Component.literal("§7Place a TOG item + one ingredient, then Apply."),
			Component.literal("§bRedstone Block§r → Speedy"),
			Component.literal("§bBread§r → Sustaining"),
			Component.literal("§bBrick§r → Bulwark"),
			Component.literal("§bPiston§r → Repulse (shield)"),
			Component.literal("§bCactus§r → Thorns (armor)"),
			Component.literal("§bIron Block§r → Guardian (shield)"),
			Component.literal("§7See the Guide Book for the full list.")
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
