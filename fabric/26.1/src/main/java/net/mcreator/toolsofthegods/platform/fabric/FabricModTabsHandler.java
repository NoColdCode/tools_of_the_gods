package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.world.item.CreativeModeTabs;

import net.mcreator.toolsofthegods.init.TogCreativeTabHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.item.TogGuideBookItem;
import net.mcreator.toolsofthegods.util.TogFeatures;

public final class FabricModTabsHandler {
	private FabricModTabsHandler() {
	}

	public static void register() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
			output.accept(TogGuideBookItem.createPopulatedStack());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
			if (TogFeatures.extendedToolsEnabled()) {
				output.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
			}
		});
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get());
			output.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
			output.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get());
			output.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get());
			output.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get());
			output.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get());
			output.accept(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get());
			TogCreativeTabHelper.acceptWings(output);
			if (TogFeatures.extendedToolsEnabled()) {
				output.accept(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get());
				output.accept(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get());
				output.accept(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get());
				output.accept(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get());
				output.accept(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get());
				output.accept(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get());
				output.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
			}
		});
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
			output.accept(ToolsOfTheGodsOrbItems.WHITE_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.YELLOW_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.PURPLE_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.RED_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.BLACK_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.GREEN_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.BLUE_GEM.get());
			output.accept(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get());
		});
	}
}
