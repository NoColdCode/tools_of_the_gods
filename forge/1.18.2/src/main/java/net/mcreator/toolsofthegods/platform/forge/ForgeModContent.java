package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.block.CompressedcobbleBlock;
import net.mcreator.toolsofthegods.block.TraitSmithingTableBlock;
import net.mcreator.toolsofthegods.block.TraitSmithingTableBlockEntity;
import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.mcreator.toolsofthegods.init.TogContentCatalog;
import net.mcreator.toolsofthegods.init.TogModFeatures;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlockEntities;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlocks;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMobEffects;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.item.*;
import net.mcreator.toolsofthegods.network.TraitSmithingTableMenu;
import net.mcreator.toolsofthegods.potion.SpeedModifierMobEffect;
import net.mcreator.toolsofthegods.world.TempleOfTheGodsFeature;

import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ForgeModContent {
	private ForgeModContent() {
	}

	public static void register(IEventBus modEventBus) {
		var blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, TogModConstants.MODID);
		var items = DeferredRegister.create(ForgeRegistries.ITEMS, TogModConstants.MODID);
		var blockEntities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TogModConstants.MODID);
		var menus = DeferredRegister.create(ForgeRegistries.CONTAINERS, TogModConstants.MODID);
		var mobEffects = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TogModConstants.MODID);
		var features = DeferredRegister.create(ForgeRegistries.FEATURES, TogModConstants.MODID);

		TogContentCatalog.bind(TogArmorMaterials.DIVINE, TogArmorMaterials.createDivineMaterial());
		TogContentCatalog.bind(TogArmorMaterials.AERIAL, TogArmorMaterials.createAerialMaterial());

		blocks.register("compressedcobble", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE, new CompressedcobbleBlock()));
		blocks.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE, new TraitSmithingTableBlock()));

		registerOrb(items);

		blockEntities.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlockEntities.TRAIT_SMITHING_TABLE,
			BlockEntityType.Builder.of((pos, state) -> new TraitSmithingTableBlockEntity(pos, state),
				ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get()).build(null)));

		registerTools(items);
		menus.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU,
			IForgeMenuType.create((windowId, inv, extra) -> new TraitSmithingTableMenu(windowId, inv))));
		mobEffects.register("speed_modifier", () -> TogContentCatalog.bind(ToolsOfTheGodsModMobEffects.SPEED_MODIFIER, new SpeedModifierMobEffect()));
		features.register("temple_of_the_gods", () -> TogContentCatalog.bind(TogModFeatures.TEMPLE_OF_THE_GODS,
			new TempleOfTheGodsFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC)));

		blocks.register(modEventBus);
		items.register(modEventBus);
		blockEntities.register(modEventBus);
		menus.register(modEventBus);
		mobEffects.register(modEventBus);
		features.register(modEventBus);

		ForgeIngredientTypes.register(modEventBus);
	}

	private static void registerOrb(DeferredRegister<Item> items) {
		items.register("white_gem", () -> track(ToolsOfTheGodsOrbItems.WHITE_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("yellow_gem", () -> track(ToolsOfTheGodsOrbItems.YELLOW_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("purple_gem", () -> track(ToolsOfTheGodsOrbItems.PURPLE_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("red_gem", () -> track(ToolsOfTheGodsOrbItems.RED_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("black_gem", () -> track(ToolsOfTheGodsOrbItems.BLACK_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("green_gem", () -> track(ToolsOfTheGodsOrbItems.GREEN_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("blue_gem", () -> track(ToolsOfTheGodsOrbItems.BLUE_GEM, new Item(new Item.Properties().stacksTo(64).fireResistant())));
		items.register("universe_gem", () -> track(ToolsOfTheGodsOrbItems.UNIVERSE_GEM,
			new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(net.minecraft.world.item.Rarity.EPIC))));
		items.register("trait_remover", () -> track(ToolsOfTheGodsOrbItems.TRAIT_REMOVER,
			new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(net.minecraft.world.item.Rarity.RARE))));
		items.register("tog_guide_book", () -> track(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK, new TogGuideBookItem()));
	}

	private static void registerTools(DeferredRegister<Item> items) {
		items.register("pickaxe_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE, new PrimalWoodenToolsPickaxeItem()));
		items.register("hammer_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER, new PrimalWoodenToolsHammerItem()));
		items.register("axe_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE, new PrimalWoodenToolsAxeItem()));
		items.register("sword_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD, new PrimalWoodenToolsSwordItem()));
		items.register("shovel_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL, new PrimalWoodenToolsShovelItem()));
		items.register("hoe_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE, new PrimalWoodenToolsHoeItem()));
		items.register("bow_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW, new PrimalWoodenToolsBowItem()));
		items.register("ultimate_tool_of_the_gods", () -> track(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS, new UltimateToolOfTheGodsItem()));
		items.register("compressedcobble", () -> track(ToolsOfTheGodsModItems.COMPRESSEDCOBBLE,
			new BlockItem(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE.get(), new Item.Properties())));
		items.register("trait_smithing_table", () -> track(ToolsOfTheGodsModItems.TRAIT_SMITHING_TABLE,
			new BlockItem(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get(), new Item.Properties())));
		items.register("helmet_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET, new ForgeArmorOfTheGodsItem(EquipmentSlot.HEAD)));
		items.register("chestplate_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE, new ForgeArmorOfTheGodsItem(EquipmentSlot.CHEST)));
		items.register("leggings_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS, new ForgeArmorOfTheGodsItem(EquipmentSlot.LEGS)));
		items.register("boots_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS, new ForgeArmorOfTheGodsItem(EquipmentSlot.FEET)));
		items.register("shield_of_the_gods", () -> track(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS, new ShieldOfTheGodsItem()));
		items.register("fishing_rod_of_the_gods", () -> track(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS, new FishingRodOfTheGodsItem()));
		items.register("crossbow_of_the_gods", () -> track(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS, new CrossbowOfTheGodsItem()));
		items.register("trident_of_the_gods", () -> track(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS, new TridentOfTheGodsItem()));
		items.register("spear_of_the_gods", () -> track(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS, new SpearOfTheGodsItem()));
		items.register("flail_of_the_gods", () -> track(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS, new FlailOfTheGodsItem()));
		items.register("staff_of_the_gods", () -> track(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS, new StaffOfTheGodsItem()));
		items.register("wings_of_the_gods", () -> track(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS, new ForgeWingsOfTheGodsItem()));
	}

	private static Item track(net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		TogContentCatalog.trackItem(ref);
		return TogContentCatalog.bind(ref, item);
	}
}
