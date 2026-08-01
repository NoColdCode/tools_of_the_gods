package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.block.CompressedcobbleBlock;
import net.mcreator.toolsofthegods.block.TraitSmithingTableBlock;
import net.mcreator.toolsofthegods.block.TraitSmithingTableBlockEntity;
import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.mcreator.toolsofthegods.init.TogContentCatalog;
import net.mcreator.toolsofthegods.init.TogCreativeTabHelper;
import net.mcreator.toolsofthegods.init.TogModFeatures;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlockEntities;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlocks;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMobEffects;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModTabs;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.item.*;
import net.mcreator.toolsofthegods.network.TraitSmithingTableMenu;
import net.mcreator.toolsofthegods.potion.SpeedModifierMobEffect;
import net.mcreator.toolsofthegods.util.TogFeatures;
import net.mcreator.toolsofthegods.world.TempleOfTheGodsFeature;

public final class FabricModContent {
	private FabricModContent() {
	}

	public static void register() {
		registerBlock("compressedcobble", ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE, new CompressedcobbleBlock());
		registerBlock("trait_smithing_table", ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE, new TraitSmithingTableBlock());

		registerOrb("white_gem", ToolsOfTheGodsOrbItems.WHITE_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("yellow_gem", ToolsOfTheGodsOrbItems.YELLOW_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("purple_gem", ToolsOfTheGodsOrbItems.PURPLE_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("red_gem", ToolsOfTheGodsOrbItems.RED_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("black_gem", ToolsOfTheGodsOrbItems.BLACK_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("green_gem", ToolsOfTheGodsOrbItems.GREEN_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("blue_gem", ToolsOfTheGodsOrbItems.BLUE_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant()));
		registerOrb("universe_gem", ToolsOfTheGodsOrbItems.UNIVERSE_GEM,
			new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(net.minecraft.world.item.Rarity.EPIC)));
		registerOrb("trait_remover", ToolsOfTheGodsOrbItems.TRAIT_REMOVER,
			new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(net.minecraft.world.item.Rarity.RARE)));
		registerOrb("tog_guide_book", ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK, new TogGuideBookItem());

		registerArmorMaterial("motion_of_the_gods", TogArmorMaterials.DIVINE, TogArmorMaterials.createDivineMaterial());
		registerArmorMaterial("aerial", TogArmorMaterials.AERIAL, TogArmorMaterials.createAerialMaterial());

		registerBlockEntity("trait_smithing_table", ToolsOfTheGodsModBlockEntities.TRAIT_SMITHING_TABLE,
			BlockEntityType.Builder.of((pos, state) -> new TraitSmithingTableBlockEntity(pos, state),
				ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get()).build(null));

		registerItem("pickaxe_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE, new PrimalWoodenToolsPickaxeItem());
		registerItem("hammer_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER, new PrimalWoodenToolsHammerItem());
		registerItem("axe_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE, new PrimalWoodenToolsAxeItem());
		registerItem("sword_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD, new PrimalWoodenToolsSwordItem());
		registerItem("shovel_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL, new PrimalWoodenToolsShovelItem());
		registerItem("hoe_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE, new PrimalWoodenToolsHoeItem());
		registerItem("bow_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW, new PrimalWoodenToolsBowItem());
		registerItem("ultimate_tool_of_the_gods", ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS, new UltimateToolOfTheGodsItem());
		registerItem("compressedcobble", ToolsOfTheGodsModItems.COMPRESSEDCOBBLE,
			new BlockItem(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE.get(), new Item.Properties()));
		registerItem("trait_smithing_table", ToolsOfTheGodsModItems.TRAIT_SMITHING_TABLE,
			new BlockItem(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get(), new Item.Properties()));
		registerItem("helmet_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET, new ArmorOfTheGodsItem(EquipmentSlot.HEAD));
		registerItem("chestplate_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE, new ArmorOfTheGodsItem(EquipmentSlot.CHEST));
		registerItem("leggings_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS, new ArmorOfTheGodsItem(EquipmentSlot.LEGS));
		registerItem("boots_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS, new ArmorOfTheGodsItem(EquipmentSlot.FEET));
		registerItem("shield_of_the_gods", ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS, new ShieldOfTheGodsItem());
		registerItem("fishing_rod_of_the_gods", ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS, new FishingRodOfTheGodsItem());
		registerItem("crossbow_of_the_gods", ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS, new CrossbowOfTheGodsItem());
		registerItem("trident_of_the_gods", ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS, new TridentOfTheGodsItem());
		registerItem("spear_of_the_gods", ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS, new SpearOfTheGodsItem());
		registerItem("flail_of_the_gods", ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS, new FlailOfTheGodsItem());
		registerItem("staff_of_the_gods", ToolsOfTheGodsModItems.STAFF_OF_THE_GODS, new StaffOfTheGodsItem());
		registerItem("wings_of_the_gods", ToolsOfTheGodsModItems.WINGS_OF_THE_GODS, new WingsOfTheGodsItem());

		MenuType<TraitSmithingTableMenu> menuType = MenuType.register("trait_smithing_table", TraitSmithingTableMenu::new);
		TogContentCatalog.bind(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU, menuType);

		registerMobEffect("speed_modifier", ToolsOfTheGodsModMobEffects.SPEED_MODIFIER, new SpeedModifierMobEffect());
		registerFeature("temple_of_the_gods", TogModFeatures.TEMPLE_OF_THE_GODS,
			new TempleOfTheGodsFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

		CreativeModeTab tab = FabricItemGroupBuilder.create(id("tools_of_the_gods_tab"))
			.icon(() -> new ItemStack(TogFeatures.extendedToolsEnabled()
				? ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get()
				: ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get()))
			.appendItems(stacks -> TogCreativeTabHelper.populateMainTab(stacks::add))
			.build();
		TogContentCatalog.bind(ToolsOfTheGodsModTabs.TOOLS_OF_THE_GODS_TAB, tab);

		FabricModTabsHandler.register();
	}

	private static void registerBlock(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<Block> ref, Block block) {
		Registry.register(Registry.BLOCK, id(path), TogContentCatalog.bind(ref, block));
	}

	private static void registerItem(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		TogContentCatalog.trackItem(ref);
		Registry.register(Registry.ITEM, id(path), TogContentCatalog.bind(ref, item));
	}

	private static void registerOrb(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		registerItem(path, ref, item);
	}

	private static void registerArmorMaterial(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<net.minecraft.world.item.ArmorMaterial> ref,
		net.minecraft.world.item.ArmorMaterial material) {
		TogContentCatalog.bind(ref, material);
	}

	private static void registerBlockEntity(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<BlockEntityType<?>> ref,
		BlockEntityType<?> type) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id(path), TogContentCatalog.bind(ref, type));
	}

	private static void registerMenu(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<MenuType<TraitSmithingTableMenu>> ref,
		MenuType<TraitSmithingTableMenu> menu) {
		Registry.register(Registry.MENU, id(path), TogContentCatalog.bind(ref, menu));
	}

	private static void registerMobEffect(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<MobEffect> ref, MobEffect effect) {
		Registry.register(Registry.MOB_EFFECT, id(path), TogContentCatalog.bind(ref, effect));
	}

	private static void registerFeature(String path,
		net.mcreator.toolsofthegods.registry.TogRegistryEntry<Feature<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>> ref,
		Feature<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration> feature) {
		Registry.register(Registry.FEATURE, id(path), TogContentCatalog.bind(ref, feature));
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(TogModConstants.MODID, path);
	}
}
