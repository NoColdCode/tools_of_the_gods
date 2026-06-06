package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.item.TogGuideBookItem;

public class ToolsOfTheGodsOrbItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(ToolsOfTheGodsMod.MODID);

	public static final DeferredItem<Item> WHITE_GEM = REGISTRY.register("white_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> YELLOW_GEM = REGISTRY.register("yellow_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> PURPLE_GEM = REGISTRY.register("purple_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> RED_GEM = REGISTRY.register("red_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> BLACK_GEM = REGISTRY.register("black_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> GREEN_GEM = REGISTRY.register("green_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> BLUE_GEM = REGISTRY.register("blue_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
	public static final DeferredItem<Item> UNIVERSE_GEM = REGISTRY.register("universe_gem", () -> new Item(new Item.Properties().stacksTo(64).fireResistant().rarity(net.minecraft.world.item.Rarity.EPIC)));
	public static final DeferredItem<Item> TRAIT_REMOVER = REGISTRY.register("trait_remover", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(net.minecraft.world.item.Rarity.RARE)));
	public static final DeferredItem<Item> TOG_GUIDE_BOOK = REGISTRY.register("tog_guide_book", TogGuideBookItem::new);

	private static Item gemByTier(int tier) {
		return switch (tier) {
			case 3 -> YELLOW_GEM.get();
			case 4 -> PURPLE_GEM.get();
			case 5 -> RED_GEM.get();
			case 6 -> BLACK_GEM.get();
			case 7 -> GREEN_GEM.get();
			case 8 -> BLUE_GEM.get();
			default -> UNIVERSE_GEM.get();
		};
	}

	public static ItemStack getRandomGem(RandomSource random, int maxTierInclusive) {
		int minTier = 3;
		int cappedMax = Math.max(minTier, Math.min(9, maxTierInclusive));
		int tier = minTier + random.nextInt(cappedMax - minTier + 1);
		return new ItemStack(gemByTier(tier));
	}
}
