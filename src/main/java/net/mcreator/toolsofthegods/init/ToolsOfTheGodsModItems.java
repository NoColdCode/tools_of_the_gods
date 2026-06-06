/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.toolsofthegods.item.*;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public class ToolsOfTheGodsModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(ToolsOfTheGodsMod.MODID);
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_PICKAXE;
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_HAMMER;
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_AXE;
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_SWORD;
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_SHOVEL;
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_HOE;
	public static final DeferredItem<Item> PRIMAL_WOODEN_TOOLS_BOW;
	public static final DeferredItem<Item> ULTIMATE_TOOL_OF_THE_GODS;
	public static final DeferredItem<Item> COMPRESSEDCOBBLE;
	public static final DeferredItem<Item> TRAIT_SMITHING_TABLE;
	
	static {
		PRIMAL_WOODEN_TOOLS_PICKAXE = REGISTRY.register("pickaxe_of_the_gods", PrimalWoodenToolsPickaxeItem::new);
		PRIMAL_WOODEN_TOOLS_HAMMER = REGISTRY.register("hammer_of_the_gods", PrimalWoodenToolsHammerItem::new);
		PRIMAL_WOODEN_TOOLS_AXE = REGISTRY.register("axe_of_the_gods", PrimalWoodenToolsAxeItem::new);
		PRIMAL_WOODEN_TOOLS_SWORD = REGISTRY.register("sword_of_the_gods", PrimalWoodenToolsSwordItem::new);
		PRIMAL_WOODEN_TOOLS_SHOVEL = REGISTRY.register("shovel_of_the_gods", PrimalWoodenToolsShovelItem::new);
		PRIMAL_WOODEN_TOOLS_HOE = REGISTRY.register("hoe_of_the_gods", PrimalWoodenToolsHoeItem::new);
		PRIMAL_WOODEN_TOOLS_BOW = REGISTRY.register("bow_of_the_gods", PrimalWoodenToolsBowItem::new);
		ULTIMATE_TOOL_OF_THE_GODS = REGISTRY.register("ultimate_tool_of_the_gods", UltimateToolOfTheGodsItem::new);
		COMPRESSEDCOBBLE = block(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE);
		TRAIT_SMITHING_TABLE = block(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE);
	}

	// Start of user code block custom items
	public static final DeferredItem<Item> ARMOR_OF_THE_GODS_HELMET;
	public static final DeferredItem<Item> ARMOR_OF_THE_GODS_CHESTPLATE;
	public static final DeferredItem<Item> ARMOR_OF_THE_GODS_LEGGINGS;
	public static final DeferredItem<Item> ARMOR_OF_THE_GODS_BOOTS;
	public static final DeferredItem<Item> SHIELD_OF_THE_GODS;
	public static final DeferredItem<Item> FISHING_ROD_OF_THE_GODS;
	public static final DeferredItem<Item> CROSSBOW_OF_THE_GODS;
	public static final DeferredItem<Item> TRIDENT_OF_THE_GODS;
	public static final DeferredItem<Item> SPEAR_OF_THE_GODS;
	public static final DeferredItem<Item> FLAIL_OF_THE_GODS;
	public static final DeferredItem<Item> STAFF_OF_THE_GODS;
	public static final DeferredItem<Item> WINGS_OF_THE_GODS;

	static {
		ARMOR_OF_THE_GODS_HELMET = REGISTRY.register("helmet_of_the_gods", () -> new ArmorOfTheGodsItem(ArmorItem.Type.HELMET));
		ARMOR_OF_THE_GODS_CHESTPLATE = REGISTRY.register("chestplate_of_the_gods", () -> new ArmorOfTheGodsItem(ArmorItem.Type.CHESTPLATE));
		ARMOR_OF_THE_GODS_LEGGINGS = REGISTRY.register("leggings_of_the_gods", () -> new ArmorOfTheGodsItem(ArmorItem.Type.LEGGINGS));
		ARMOR_OF_THE_GODS_BOOTS = REGISTRY.register("boots_of_the_gods", () -> new ArmorOfTheGodsItem(ArmorItem.Type.BOOTS));
		SHIELD_OF_THE_GODS = REGISTRY.register("shield_of_the_gods", ShieldOfTheGodsItem::new);
		FISHING_ROD_OF_THE_GODS = REGISTRY.register("fishing_rod_of_the_gods", FishingRodOfTheGodsItem::new);
		CROSSBOW_OF_THE_GODS = REGISTRY.register("crossbow_of_the_gods", CrossbowOfTheGodsItem::new);
		TRIDENT_OF_THE_GODS = REGISTRY.register("trident_of_the_gods", TridentOfTheGodsItem::new);
		SPEAR_OF_THE_GODS = REGISTRY.register("spear_of_the_gods", SpearOfTheGodsItem::new);
		FLAIL_OF_THE_GODS = REGISTRY.register("flail_of_the_gods", FlailOfTheGodsItem::new);
		STAFF_OF_THE_GODS = REGISTRY.register("staff_of_the_gods", StaffOfTheGodsItem::new);
		WINGS_OF_THE_GODS = REGISTRY.register("wings_of_the_gods", WingsOfTheGodsItem::new);
	}

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
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
}