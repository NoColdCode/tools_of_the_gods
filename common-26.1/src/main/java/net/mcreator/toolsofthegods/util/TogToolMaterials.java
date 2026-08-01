package net.mcreator.toolsofthegods.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import net.mcreator.toolsofthegods.TogModConstants;

public final class TogToolMaterials {
	private static final TagKey<Item> REPAIRS_PRIMAL = TagKey.create(
		BuiltInRegistries.ITEM.key(),
		Identifier.fromNamespaceAndPath(TogModConstants.MODID, "repairs_primal_tools")
	);

	private static final TagKey<Item> REPAIRS_ULTIMATE = TagKey.create(
		BuiltInRegistries.ITEM.key(),
		Identifier.fromNamespaceAndPath(TogModConstants.MODID, "repairs_ultimate_tool")
	);

	public static final ToolMaterial PRIMAL = new ToolMaterial(
		BlockTags.INCORRECT_FOR_WOODEN_TOOL,
		1,
		0.5F,
		0.0F,
		3,
		REPAIRS_PRIMAL
	);

	public static final ToolMaterial ULTIMATE = new ToolMaterial(
		BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
		30,
		18.0F,
		9.0F,
		30,
		REPAIRS_ULTIMATE
	);

	private TogToolMaterials() {
	}
}
