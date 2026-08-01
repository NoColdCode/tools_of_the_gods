package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorMaterial;
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
import net.mcreator.toolsofthegods.potion.XpBoostMobEffect;
import net.mcreator.toolsofthegods.util.TogFeatures;
import net.mcreator.toolsofthegods.world.TempleOfTheGodsFeature;

/**
 * NeoForge {@link DeferredRegister} glue for shared {@link TogRegistryEntry} holders in {@code :common}.
 */
public final class NeoForgeModContent {
	private NeoForgeModContent() {
	}

	public static void register(IEventBus modEventBus) {
		var blocks = DeferredRegister.createBlocks(TogModConstants.MODID);
		var items = DeferredRegister.createItems(TogModConstants.MODID);
		var blockEntities = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TogModConstants.MODID);
		var menus = DeferredRegister.create(Registries.MENU, TogModConstants.MODID);
		var mobEffects = DeferredRegister.create(Registries.MOB_EFFECT, TogModConstants.MODID);
		var armorMaterials = DeferredRegister.create(Registries.ARMOR_MATERIAL, TogModConstants.MODID);
		var features = DeferredRegister.create(Registries.FEATURE, TogModConstants.MODID);
		var tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TogModConstants.MODID);

		blocks.register("compressedcobble", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE, new CompressedcobbleBlock()));
		blocks.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE, new TraitSmithingTableBlock()));

		registerOrb(items);
		armorMaterials.register("motion_of_the_gods", () -> TogContentCatalog.bind(TogArmorMaterials.DIVINE, TogArmorMaterials.createDivineMaterial()));
		armorMaterials.register("aerial", () -> TogContentCatalog.bind(TogArmorMaterials.AERIAL, TogArmorMaterials.createAerialMaterial()));

		blockEntities.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlockEntities.TRAIT_SMITHING_TABLE,
			BlockEntityType.Builder.of(TraitSmithingTableBlockEntity::new, ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get()).build(null)));

		registerTools(items);
		menus.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU,
			new MenuType<>(TraitSmithingTableMenu::new, FeatureFlags.DEFAULT_FLAGS)));
		mobEffects.register("speed_modifier", () -> TogContentCatalog.bind(ToolsOfTheGodsModMobEffects.SPEED_MODIFIER, new SpeedModifierMobEffect()));
		mobEffects.register("xp_focus", () -> TogContentCatalog.bind(ToolsOfTheGodsModMobEffects.XP_FOCUS, new XpBoostMobEffect(0x55FFFF)));
		mobEffects.register("xp_surge", () -> TogContentCatalog.bind(ToolsOfTheGodsModMobEffects.XP_SURGE, new XpBoostMobEffect(0x55FF55)));
		mobEffects.register("xp_rapture", () -> TogContentCatalog.bind(ToolsOfTheGodsModMobEffects.XP_RAPTURE, new XpBoostMobEffect(0xFF55FF)));
		mobEffects.register("xp_apotheosis", () -> TogContentCatalog.bind(ToolsOfTheGodsModMobEffects.XP_APOTHEOSIS, new XpBoostMobEffect(0xFFAA00)));
		features.register("temple_of_the_gods", () -> TogContentCatalog.bind(TogModFeatures.TEMPLE_OF_THE_GODS,
			new TempleOfTheGodsFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC)));
		tabs.register("tools_of_the_gods_tab", () -> TogContentCatalog.bind(ToolsOfTheGodsModTabs.TOOLS_OF_THE_GODS_TAB,
			CreativeModeTab.builder()
				.title(Component.translatable("itemGroup.tools_of_the_gods.special_tab"))
				.icon(() -> new ItemStack(TogFeatures.extendedToolsEnabled()
					? ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get()
					: ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get()))
				.displayItems(TogCreativeTabHelper::populateMainTab)
				.build()));

		blocks.register(modEventBus);
		items.register(modEventBus);
		blockEntities.register(modEventBus);
		menus.register(modEventBus);
		mobEffects.register(modEventBus);
		armorMaterials.register(modEventBus);
		features.register(modEventBus);
		tabs.register(modEventBus);

		NeoForgeIngredientTypes.register(modEventBus);
	}

	private static void registerOrb(DeferredRegister.Items items) {
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
		items.register("xp_infusion_minor", () -> track(ToolsOfTheGodsOrbItems.XP_INFUSION_MINOR,
			new XpInfusionItem(new Item.Properties().stacksTo(16).rarity(net.minecraft.world.item.Rarity.UNCOMMON), 100)));
		items.register("xp_infusion_major", () -> track(ToolsOfTheGodsOrbItems.XP_INFUSION_MAJOR,
			new XpInfusionItem(new Item.Properties().stacksTo(16).rarity(net.minecraft.world.item.Rarity.RARE), 500)));
		items.register("xp_infusion_grand", () -> track(ToolsOfTheGodsOrbItems.XP_INFUSION_GRAND,
			new XpInfusionItem(new Item.Properties().stacksTo(8).rarity(net.minecraft.world.item.Rarity.EPIC), 1000)));
		items.register("elixir_xp_focus", () -> track(ToolsOfTheGodsOrbItems.ELIXIR_XP_FOCUS,
			new XpElixirItem(new Item.Properties().stacksTo(8).rarity(net.minecraft.world.item.Rarity.UNCOMMON),
				() -> ToolsOfTheGodsModMobEffects.XP_FOCUS, 20 * 180, 2.0f, "Divine Focus")));
		items.register("elixir_xp_surge", () -> track(ToolsOfTheGodsOrbItems.ELIXIR_XP_SURGE,
			new XpElixirItem(new Item.Properties().stacksTo(8).rarity(net.minecraft.world.item.Rarity.RARE),
				() -> ToolsOfTheGodsModMobEffects.XP_SURGE, 20 * 120, 5.0f, "Divine Surge")));
		items.register("elixir_xp_rapture", () -> track(ToolsOfTheGodsOrbItems.ELIXIR_XP_RAPTURE,
			new XpElixirItem(new Item.Properties().stacksTo(4).rarity(net.minecraft.world.item.Rarity.EPIC),
				() -> ToolsOfTheGodsModMobEffects.XP_RAPTURE, 20 * 90, 10.0f, "Divine Rapture")));
		items.register("elixir_xp_apotheosis", () -> track(ToolsOfTheGodsOrbItems.ELIXIR_XP_APOTHEOSIS,
			new XpElixirItem(new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.EPIC),
				() -> ToolsOfTheGodsModMobEffects.XP_APOTHEOSIS, 20 * 30, 100.0f, "Divine Apotheosis")));
	}

	private static void registerTools(DeferredRegister.Items items) {
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
		items.register("helmet_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET, new NeoForgeArmorOfTheGodsItem(ArmorItem.Type.HELMET)));
		items.register("chestplate_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE, new NeoForgeArmorOfTheGodsItem(ArmorItem.Type.CHESTPLATE)));
		items.register("leggings_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS, new NeoForgeArmorOfTheGodsItem(ArmorItem.Type.LEGGINGS)));
		items.register("boots_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS, new NeoForgeArmorOfTheGodsItem(ArmorItem.Type.BOOTS)));
		items.register("shield_of_the_gods", () -> track(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS, new ShieldOfTheGodsItem()));
		items.register("fishing_rod_of_the_gods", () -> track(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS, new FishingRodOfTheGodsItem()));
		items.register("crossbow_of_the_gods", () -> track(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS, new CrossbowOfTheGodsItem()));
		items.register("trident_of_the_gods", () -> track(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS, new TridentOfTheGodsItem()));
		items.register("spear_of_the_gods", () -> track(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS, new SpearOfTheGodsItem()));
		items.register("flail_of_the_gods", () -> track(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS, new FlailOfTheGodsItem()));
		items.register("staff_of_the_gods", () -> track(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS, new StaffOfTheGodsItem()));
		items.register("wings_of_the_gods", () -> track(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS, new NeoForgeWingsOfTheGodsItem()));
	}

	private static Item track(net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		TogContentCatalog.trackItem(ref);
		return TogContentCatalog.bind(ref, item);
	}
}
