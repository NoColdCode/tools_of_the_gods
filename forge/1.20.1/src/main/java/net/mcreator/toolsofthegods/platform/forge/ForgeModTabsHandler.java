package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.item.CreativeModeTabs;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.item.TogGuideBookItem;
import net.mcreator.toolsofthegods.util.TogFeatures;

@Mod.EventBusSubscriber(modid = TogModConstants.MODID)
public final class ForgeModTabsHandler {
	private ForgeModTabsHandler() {
	}

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(TogGuideBookItem.createPopulatedStack());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
			if (TogFeatures.extendedToolsEnabled()) {
				tabData.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
			}
		} else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get());
			tabData.accept(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
			tabData.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get());
			tabData.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get());
			tabData.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get());
			tabData.accept(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get());
			tabData.accept(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get());
			if (TogFeatures.extendedToolsEnabled()) {
				tabData.accept(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get());
				tabData.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
			}
		} else if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			tabData.accept(ToolsOfTheGodsOrbItems.WHITE_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.YELLOW_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.PURPLE_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.RED_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.BLACK_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.GREEN_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.BLUE_GEM.get());
			tabData.accept(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get());
		}
	}
}
