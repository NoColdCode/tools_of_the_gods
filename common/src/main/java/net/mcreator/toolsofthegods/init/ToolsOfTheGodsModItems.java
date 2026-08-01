/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.toolsofthegods.init;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

public class ToolsOfTheGodsModItems {
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_PICKAXE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_HAMMER = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_AXE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_SWORD = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_SHOVEL = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_HOE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PRIMAL_WOODEN_TOOLS_BOW = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ULTIMATE_TOOL_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> COMPRESSEDCOBBLE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> TRAIT_SMITHING_TABLE = new TogRegistryEntry<>();

	// Start of user code block custom items
	public static final TogRegistryEntry<Item> ARMOR_OF_THE_GODS_HELMET = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ARMOR_OF_THE_GODS_CHESTPLATE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ARMOR_OF_THE_GODS_LEGGINGS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ARMOR_OF_THE_GODS_BOOTS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> SHIELD_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> FISHING_ROD_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> CROSSBOW_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> TRIDENT_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> SPEAR_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> FLAIL_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> STAFF_OF_THE_GODS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> WINGS_OF_THE_GODS = new TogRegistryEntry<>();

	public static boolean isArmorOfTheGods(Item item) {
		return item == ARMOR_OF_THE_GODS_HELMET.get()
			|| item == ARMOR_OF_THE_GODS_CHESTPLATE.get()
			|| item == ARMOR_OF_THE_GODS_LEGGINGS.get()
			|| item == ARMOR_OF_THE_GODS_BOOTS.get();
	}

	public static Item getArmorOfTheGods(ArmorItem.Type type) {
		return switch (type) {
			case HELMET -> ARMOR_OF_THE_GODS_HELMET.get();
			case CHESTPLATE -> ARMOR_OF_THE_GODS_CHESTPLATE.get();
			case LEGGINGS -> ARMOR_OF_THE_GODS_LEGGINGS.get();
			case BOOTS -> ARMOR_OF_THE_GODS_BOOTS.get();
			default -> null;
		};
	}
	// End of user code block custom items
}
