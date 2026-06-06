/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;

import net.mcreator.toolsofthegods.network.TraitSmithingTableMenu;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public class ToolsOfTheGodsModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(net.minecraft.core.registries.Registries.MENU, ToolsOfTheGodsMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<TraitSmithingTableMenu>> TRAIT_SMITHING_TABLE_MENU = REGISTRY.register("trait_smithing_table",
			() -> new MenuType<TraitSmithingTableMenu>(TraitSmithingTableMenu::new, FeatureFlags.DEFAULT_FLAGS));
}

