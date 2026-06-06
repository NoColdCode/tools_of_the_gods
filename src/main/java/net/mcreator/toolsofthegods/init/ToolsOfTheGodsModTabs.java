/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.item.TogGuideBookItem;
import net.mcreator.toolsofthegods.util.TogFeatures;

@EventBusSubscriber
public class ToolsOfTheGodsModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ToolsOfTheGodsMod.MODID);
	public static final net.neoforged.neoforge.registries.DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_OF_THE_GODS_TAB = REGISTRY.register("tools_of_the_gods_tab",
			() -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.tools_of_the_gods.special_tab"))
					.icon(() -> new ItemStack(TogFeatures.extendedToolsEnabled()
						? ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get()
						: ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get()))
					.displayItems(TogCreativeTabHelper::populateMainTab)
					.build());

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
if (TogFeatures.extendedToolsEnabled()) {
tabData.accept(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get());
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
