package net.mcreator.toolsofthegods.integration;

import net.minecraft.network.chat.Component;
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
			if (ModList.get().isLoaded("emi")) {
				registerEmiInfo();
			}
		});
	}

	private static void registerJeiInfo() {
		try {
			Class.forName("mezz.jei.api.IModPlugin");
		} catch (ClassNotFoundException ignored) {
		}
	}

	private static void registerEmiInfo() {
		try {
			Class.forName("dev.emi.emi.api.EmiApi");
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
