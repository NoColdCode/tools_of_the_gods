package net.mcreator.toolsofthegods.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.mcreator.toolsofthegods.TogModConstants;

public final class TogRegistryKeys {
	private TogRegistryKeys() {
	}

	public static ResourceKey<Block> block(String path) {
		return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, path));
	}

	public static ResourceKey<Item> item(String path) {
		return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, path));
	}
}
