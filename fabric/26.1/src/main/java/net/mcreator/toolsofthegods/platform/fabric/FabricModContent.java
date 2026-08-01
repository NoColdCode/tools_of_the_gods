package net.mcreator.toolsofthegods.platform.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

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
import net.mcreator.toolsofthegods.util.TogArmorPiece;
import net.mcreator.toolsofthegods.util.TogFeatures;
import net.mcreator.toolsofthegods.util.TogRegistryKeys;
import net.mcreator.toolsofthegods.world.TempleOfTheGodsFeature;

public final class FabricModContent {
	private FabricModContent() {
	}

	public static void register() {
		registerBlock("compressedcobble", ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE, new CompressedcobbleBlock(TogRegistryKeys.block("compressedcobble")));
		registerBlock("trait_smithing_table", ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE,
			new TraitSmithingTableBlock(TogRegistryKeys.block("trait_smithing_table")));

		registerOrb("white_gem", ToolsOfTheGodsOrbItems.WHITE_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("white_gem")).stacksTo(64).fireResistant()));
		registerOrb("yellow_gem", ToolsOfTheGodsOrbItems.YELLOW_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("yellow_gem")).stacksTo(64).fireResistant()));
		registerOrb("purple_gem", ToolsOfTheGodsOrbItems.PURPLE_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("purple_gem")).stacksTo(64).fireResistant()));
		registerOrb("red_gem", ToolsOfTheGodsOrbItems.RED_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("red_gem")).stacksTo(64).fireResistant()));
		registerOrb("black_gem", ToolsOfTheGodsOrbItems.BLACK_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("black_gem")).stacksTo(64).fireResistant()));
		registerOrb("green_gem", ToolsOfTheGodsOrbItems.GREEN_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("green_gem")).stacksTo(64).fireResistant()));
		registerOrb("blue_gem", ToolsOfTheGodsOrbItems.BLUE_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("blue_gem")).stacksTo(64).fireResistant()));
		registerOrb("universe_gem", ToolsOfTheGodsOrbItems.UNIVERSE_GEM,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("universe_gem")).stacksTo(64).fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)));
		registerOrb("trait_remover", ToolsOfTheGodsOrbItems.TRAIT_REMOVER,
			new Item(new Item.Properties().setId(TogRegistryKeys.item("trait_remover")).stacksTo(16).fireResistant()
				.rarity(net.minecraft.world.item.Rarity.RARE)));
		registerOrb("tog_guide_book", ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK, new TogGuideBookItem(TogRegistryKeys.item("tog_guide_book")));

		TogContentCatalog.bind(TogArmorMaterials.DIVINE, TogArmorMaterials.createDivineMaterial());
		TogContentCatalog.bind(TogArmorMaterials.AERIAL, TogArmorMaterials.createAerialMaterial());

		registerBlockEntity("trait_smithing_table", ToolsOfTheGodsModBlockEntities.TRAIT_SMITHING_TABLE,
			FabricBlockEntityTypeBuilder.create(TraitSmithingTableBlockEntity::new, ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get()).build());

		registerItem("pickaxe_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE,
			new PrimalWoodenToolsPickaxeItem(TogRegistryKeys.item("pickaxe_of_the_gods")));
		registerItem("hammer_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER,
			new PrimalWoodenToolsHammerItem(TogRegistryKeys.item("hammer_of_the_gods")));
		registerItem("axe_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE,
			new PrimalWoodenToolsAxeItem(TogRegistryKeys.item("axe_of_the_gods")));
		registerItem("sword_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD,
			new PrimalWoodenToolsSwordItem(TogRegistryKeys.item("sword_of_the_gods")));
		registerItem("shovel_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL,
			new PrimalWoodenToolsShovelItem(TogRegistryKeys.item("shovel_of_the_gods")));
		registerItem("hoe_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE,
			new PrimalWoodenToolsHoeItem(TogRegistryKeys.item("hoe_of_the_gods")));
		registerItem("bow_of_the_gods", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW,
			new PrimalWoodenToolsBowItem(TogRegistryKeys.item("bow_of_the_gods")));
		registerItem("ultimate_tool_of_the_gods", ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS,
			new UltimateToolOfTheGodsItem(TogRegistryKeys.item("ultimate_tool_of_the_gods")));
		registerItem("compressedcobble", ToolsOfTheGodsModItems.COMPRESSEDCOBBLE,
			new BlockItem(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE.get(),
				new Item.Properties().setId(TogRegistryKeys.item("compressedcobble")).useBlockDescriptionPrefix()));
		registerItem("trait_smithing_table", ToolsOfTheGodsModItems.TRAIT_SMITHING_TABLE,
			new BlockItem(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get(),
				new Item.Properties().setId(TogRegistryKeys.item("trait_smithing_table")).useBlockDescriptionPrefix()));
		registerItem("helmet_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("helmet_of_the_gods"), TogArmorPiece.HELMET));
		registerItem("chestplate_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("chestplate_of_the_gods"), TogArmorPiece.CHESTPLATE));
		registerItem("leggings_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("leggings_of_the_gods"), TogArmorPiece.LEGGINGS));
		registerItem("boots_of_the_gods", ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("boots_of_the_gods"), TogArmorPiece.BOOTS));
		registerItem("shield_of_the_gods", ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS,
			new ShieldOfTheGodsItem(TogRegistryKeys.item("shield_of_the_gods")));
		registerItem("fishing_rod_of_the_gods", ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS,
			new FishingRodOfTheGodsItem(TogRegistryKeys.item("fishing_rod_of_the_gods")));
		registerItem("crossbow_of_the_gods", ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS,
			new CrossbowOfTheGodsItem(TogRegistryKeys.item("crossbow_of_the_gods")));
		registerItem("trident_of_the_gods", ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS,
			new TridentOfTheGodsItem(TogRegistryKeys.item("trident_of_the_gods")));
		registerItem("spear_of_the_gods", ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS,
			new SpearOfTheGodsItem(TogRegistryKeys.item("spear_of_the_gods")));
		registerItem("flail_of_the_gods", ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS,
			new FlailOfTheGodsItem(TogRegistryKeys.item("flail_of_the_gods")));
		registerItem("staff_of_the_gods", ToolsOfTheGodsModItems.STAFF_OF_THE_GODS,
			new StaffOfTheGodsItem(TogRegistryKeys.item("staff_of_the_gods")));
		registerItem("wings_of_the_gods", ToolsOfTheGodsModItems.WINGS_OF_THE_GODS,
			new WingsOfTheGodsItem(TogRegistryKeys.item("wings_of_the_gods")));

		registerMenu("trait_smithing_table", ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU,
			new MenuType<>(TraitSmithingTableMenu::new, FeatureFlags.DEFAULT_FLAGS));
		registerMobEffect("speed_modifier", ToolsOfTheGodsModMobEffects.SPEED_MODIFIER, new SpeedModifierMobEffect());
		registerFeature("temple_of_the_gods", TogModFeatures.TEMPLE_OF_THE_GODS,
			new TempleOfTheGodsFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

		ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id("tools_of_the_gods_tab"));
		CreativeModeTab tab = Registry.register(
			BuiltInRegistries.CREATIVE_MODE_TAB,
			tabKey,
			FabricCreativeModeTab.builder()
				.title(Component.translatable("itemGroup.tools_of_the_gods.special_tab"))
				.icon(() -> new ItemStack(TogFeatures.extendedToolsEnabled()
					? ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get()
					: ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get()))
				.displayItems(TogCreativeTabHelper::populateMainTab)
				.build()
		);
		TogContentCatalog.bind(ToolsOfTheGodsModTabs.TOOLS_OF_THE_GODS_TAB, tab);

		FabricModTabsHandler.register();
	}

	private static void registerBlock(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<Block> ref, Block block) {
		ResourceKey<Block> key = TogRegistryKeys.block(path);
		Registry.register(BuiltInRegistries.BLOCK, key, TogContentCatalog.bind(ref, block));
	}

	private static void registerItem(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		TogContentCatalog.trackItem(ref);
		ResourceKey<Item> key = TogRegistryKeys.item(path);
		Registry.register(BuiltInRegistries.ITEM, key, TogContentCatalog.bind(ref, item));
	}

	private static void registerOrb(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		registerItem(path, ref, item);
	}

	private static void registerBlockEntity(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<BlockEntityType<?>> ref,
		BlockEntityType<?> type) {
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(path), TogContentCatalog.bind(ref, type));
	}

	private static void registerMenu(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<MenuType<TraitSmithingTableMenu>> ref,
		MenuType<TraitSmithingTableMenu> menu) {
		Registry.register(BuiltInRegistries.MENU, id(path), TogContentCatalog.bind(ref, menu));
	}

	private static void registerMobEffect(String path, net.mcreator.toolsofthegods.registry.TogRegistryEntry<MobEffect> ref, MobEffect effect) {
		Registry.register(BuiltInRegistries.MOB_EFFECT, id(path), TogContentCatalog.bind(ref, effect));
	}

	private static void registerFeature(String path,
		net.mcreator.toolsofthegods.registry.TogRegistryEntry<Feature<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>> ref,
		Feature<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration> feature) {
		Registry.register(BuiltInRegistries.FEATURE, id(path), TogContentCatalog.bind(ref, feature));
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(TogModConstants.MODID, path);
	}
}
