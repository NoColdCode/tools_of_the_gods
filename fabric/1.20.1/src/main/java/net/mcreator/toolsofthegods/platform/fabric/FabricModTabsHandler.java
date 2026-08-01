package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.world.item.CreativeModeTabs;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.item.TogGuideBookItem;
import net.mcreator.toolsofthegods.util.TogFeatures;

public final class FabricModTabsHandler {
	private FabricModTabsHandler() {
	}

	public static void register() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.accept(TogGuideBookItem.createPopulatedStack());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
			if (TogFeatures.extendedToolsEnabled()) {
				entries.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
			}
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get());
			entries.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
			entries.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get());
			entries.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get());
			entries.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get());
			entries.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get());
			entries.accept(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get());
			if (TogFeatures.extendedToolsEnabled()) {
				entries.accept(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get());
				entries.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
			}
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
			entries.accept(ToolsOfTheGodsOrbItems.WHITE_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.YELLOW_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.PURPLE_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.RED_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.BLACK_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.GREEN_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.BLUE_GEM.get());
			entries.accept(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get());
		});
	}
}
