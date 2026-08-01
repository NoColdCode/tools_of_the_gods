package net.mcreator.toolsofthegods.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

public final class TogFeatures {
	private TogFeatures() {
	}

	public static boolean extendedToolsEnabled() {
		return ToolsOfTheGodsCommonConfig.EXTENDED_TOOLS_ENABLED.get();
	}

	public static boolean creativeTierPreviewsEnabled() {
		return ToolsOfTheGodsCommonConfig.CREATIVE_TIER_PREVIEWS_ENABLED.get();
	}

	public static boolean isExtendedTool(ItemStack stack) {
		return stack != null && isExtendedTool(stack.getItem());
	}

	public static boolean isExtendedTool(Item item) {
		if (item == null) {
			return false;
		}
		return item == ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get()
			|| item == ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get()
			|| item == ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get()
			|| item == ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get()
			|| item == ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get()
			|| item == ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get()
			|| item == ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get();
	}
}
