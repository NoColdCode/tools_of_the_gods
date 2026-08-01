package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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

import java.util.Set;

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
import net.mcreator.toolsofthegods.util.TogArmorPiece;
import net.mcreator.toolsofthegods.util.TogFeatures;
import net.mcreator.toolsofthegods.util.TogRegistryKeys;
import net.mcreator.toolsofthegods.world.TempleOfTheGodsFeature;

public final class NeoForgeModContent {
	private NeoForgeModContent() {
	}

	public static void register(IEventBus modEventBus) {
		var blocks = DeferredRegister.createBlocks(TogModConstants.MODID);
		var items = DeferredRegister.createItems(TogModConstants.MODID);
		var blockEntities = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TogModConstants.MODID);
		var menus = DeferredRegister.create(Registries.MENU, TogModConstants.MODID);
		var mobEffects = DeferredRegister.create(Registries.MOB_EFFECT, TogModConstants.MODID);
		var features = DeferredRegister.create(Registries.FEATURE, TogModConstants.MODID);
		var tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TogModConstants.MODID);

		blocks.register("compressedcobble", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE,
			new CompressedcobbleBlock(TogRegistryKeys.block("compressedcobble"))));
		blocks.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE,
			new TraitSmithingTableBlock(TogRegistryKeys.block("trait_smithing_table"))));

		registerOrb(items, "white_gem", ToolsOfTheGodsOrbItems.WHITE_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("white_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "yellow_gem", ToolsOfTheGodsOrbItems.YELLOW_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("yellow_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "purple_gem", ToolsOfTheGodsOrbItems.PURPLE_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("purple_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "red_gem", ToolsOfTheGodsOrbItems.RED_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("red_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "black_gem", ToolsOfTheGodsOrbItems.BLACK_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("black_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "green_gem", ToolsOfTheGodsOrbItems.GREEN_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("green_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "blue_gem", ToolsOfTheGodsOrbItems.BLUE_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("blue_gem")).stacksTo(64).fireResistant()));
		registerOrb(items, "universe_gem", ToolsOfTheGodsOrbItems.UNIVERSE_GEM,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("universe_gem")).stacksTo(64).fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)));
		registerOrb(items, "trait_remover", ToolsOfTheGodsOrbItems.TRAIT_REMOVER,
			() -> new Item(new Item.Properties().setId(TogRegistryKeys.item("trait_remover")).stacksTo(16).fireResistant()
				.rarity(net.minecraft.world.item.Rarity.RARE)));
		registerOrb(items, "tog_guide_book", ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK,
			() -> new TogGuideBookItem(TogRegistryKeys.item("tog_guide_book")));
		registerOrb(items, "xp_infusion_minor", ToolsOfTheGodsOrbItems.XP_INFUSION_MINOR,
			() -> new XpInfusionItem(new Item.Properties().setId(TogRegistryKeys.item("xp_infusion_minor")).stacksTo(16)
				.rarity(net.minecraft.world.item.Rarity.UNCOMMON), 100));
		registerOrb(items, "xp_infusion_major", ToolsOfTheGodsOrbItems.XP_INFUSION_MAJOR,
			() -> new XpInfusionItem(new Item.Properties().setId(TogRegistryKeys.item("xp_infusion_major")).stacksTo(16)
				.rarity(net.minecraft.world.item.Rarity.RARE), 500));
		registerOrb(items, "xp_infusion_grand", ToolsOfTheGodsOrbItems.XP_INFUSION_GRAND,
			() -> new XpInfusionItem(new Item.Properties().setId(TogRegistryKeys.item("xp_infusion_grand")).stacksTo(8)
				.rarity(net.minecraft.world.item.Rarity.EPIC), 1000));
		registerOrb(items, "elixir_xp_focus", ToolsOfTheGodsOrbItems.ELIXIR_XP_FOCUS,
			() -> new XpElixirItem(new Item.Properties().setId(TogRegistryKeys.item("elixir_xp_focus")).stacksTo(8)
				.rarity(net.minecraft.world.item.Rarity.UNCOMMON),
				() -> ToolsOfTheGodsModMobEffects.XP_FOCUS, 20 * 180, 2.0f, "Divine Focus"));
		registerOrb(items, "elixir_xp_surge", ToolsOfTheGodsOrbItems.ELIXIR_XP_SURGE,
			() -> new XpElixirItem(new Item.Properties().setId(TogRegistryKeys.item("elixir_xp_surge")).stacksTo(8)
				.rarity(net.minecraft.world.item.Rarity.RARE),
				() -> ToolsOfTheGodsModMobEffects.XP_SURGE, 20 * 120, 5.0f, "Divine Surge"));
		registerOrb(items, "elixir_xp_rapture", ToolsOfTheGodsOrbItems.ELIXIR_XP_RAPTURE,
			() -> new XpElixirItem(new Item.Properties().setId(TogRegistryKeys.item("elixir_xp_rapture")).stacksTo(4)
				.rarity(net.minecraft.world.item.Rarity.EPIC),
				() -> ToolsOfTheGodsModMobEffects.XP_RAPTURE, 20 * 90, 10.0f, "Divine Rapture"));
		registerOrb(items, "elixir_xp_apotheosis", ToolsOfTheGodsOrbItems.ELIXIR_XP_APOTHEOSIS,
			() -> new XpElixirItem(new Item.Properties().setId(TogRegistryKeys.item("elixir_xp_apotheosis")).stacksTo(1)
				.rarity(net.minecraft.world.item.Rarity.EPIC),
				() -> ToolsOfTheGodsModMobEffects.XP_APOTHEOSIS, 20 * 30, 100.0f, "Divine Apotheosis"));

		TogContentCatalog.bind(TogArmorMaterials.DIVINE, TogArmorMaterials.createDivineMaterial());
		TogContentCatalog.bind(TogArmorMaterials.AERIAL, TogArmorMaterials.createAerialMaterial());

		blockEntities.register("trait_smithing_table", () -> TogContentCatalog.bind(ToolsOfTheGodsModBlockEntities.TRAIT_SMITHING_TABLE,
			new BlockEntityType<>(TraitSmithingTableBlockEntity::new,
				Set.of(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get()))));

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
		features.register(modEventBus);
		tabs.register(modEventBus);

		NeoForgeIngredientTypes.register(modEventBus);
	}

	private static void registerOrb(DeferredRegister.Items items, String path,
		net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, java.util.function.Supplier<Item> itemFactory) {
		items.register(path, () -> track(ref, itemFactory.get()));
	}

	private static void registerTools(DeferredRegister.Items items) {
		items.register("pickaxe_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE,
			new PrimalWoodenToolsPickaxeItem(TogRegistryKeys.item("pickaxe_of_the_gods"))));
		items.register("hammer_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER,
			new PrimalWoodenToolsHammerItem(TogRegistryKeys.item("hammer_of_the_gods"))));
		items.register("axe_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE,
			new PrimalWoodenToolsAxeItem(TogRegistryKeys.item("axe_of_the_gods"))));
		items.register("sword_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD,
			new PrimalWoodenToolsSwordItem(TogRegistryKeys.item("sword_of_the_gods"))));
		items.register("shovel_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL,
			new PrimalWoodenToolsShovelItem(TogRegistryKeys.item("shovel_of_the_gods"))));
		items.register("hoe_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE,
			new PrimalWoodenToolsHoeItem(TogRegistryKeys.item("hoe_of_the_gods"))));
		items.register("bow_of_the_gods", () -> track(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW,
			new PrimalWoodenToolsBowItem(TogRegistryKeys.item("bow_of_the_gods"))));
		items.register("ultimate_tool_of_the_gods", () -> track(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS,
			new UltimateToolOfTheGodsItem(TogRegistryKeys.item("ultimate_tool_of_the_gods"))));
		items.register("compressedcobble", () -> track(ToolsOfTheGodsModItems.COMPRESSEDCOBBLE,
			new BlockItem(ToolsOfTheGodsModBlocks.COMPRESSEDCOBBLE.get(),
				new Item.Properties().setId(TogRegistryKeys.item("compressedcobble")).useBlockDescriptionPrefix())));
		items.register("trait_smithing_table", () -> track(ToolsOfTheGodsModItems.TRAIT_SMITHING_TABLE,
			new BlockItem(ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get(),
				new Item.Properties().setId(TogRegistryKeys.item("trait_smithing_table")).useBlockDescriptionPrefix())));
		items.register("helmet_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("helmet_of_the_gods"), TogArmorPiece.HELMET)));
		items.register("chestplate_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("chestplate_of_the_gods"), TogArmorPiece.CHESTPLATE)));
		items.register("leggings_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("leggings_of_the_gods"), TogArmorPiece.LEGGINGS)));
		items.register("boots_of_the_gods", () -> track(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS,
			new ArmorOfTheGodsItem(TogRegistryKeys.item("boots_of_the_gods"), TogArmorPiece.BOOTS)));
		items.register("shield_of_the_gods", () -> track(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS,
			new ShieldOfTheGodsItem(TogRegistryKeys.item("shield_of_the_gods"))));
		items.register("fishing_rod_of_the_gods", () -> track(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS,
			new FishingRodOfTheGodsItem(TogRegistryKeys.item("fishing_rod_of_the_gods"))));
		items.register("crossbow_of_the_gods", () -> track(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS,
			new CrossbowOfTheGodsItem(TogRegistryKeys.item("crossbow_of_the_gods"))));
		items.register("trident_of_the_gods", () -> track(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS,
			new TridentOfTheGodsItem(TogRegistryKeys.item("trident_of_the_gods"))));
		items.register("spear_of_the_gods", () -> track(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS,
			new SpearOfTheGodsItem(TogRegistryKeys.item("spear_of_the_gods"))));
		items.register("flail_of_the_gods", () -> track(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS,
			new FlailOfTheGodsItem(TogRegistryKeys.item("flail_of_the_gods"))));
		items.register("staff_of_the_gods", () -> track(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS,
			new StaffOfTheGodsItem(TogRegistryKeys.item("staff_of_the_gods"))));
		items.register("wings_of_the_gods", () -> track(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS,
			new WingsOfTheGodsItem(TogRegistryKeys.item("wings_of_the_gods"))));
	}

	private static Item track(net.mcreator.toolsofthegods.registry.TogRegistryEntry<Item> ref, Item item) {
		TogContentCatalog.trackItem(ref);
		return TogContentCatalog.bind(ref, item);
	}
}
