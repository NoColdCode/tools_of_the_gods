package net.mcreator.toolsofthegods.init;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.item.TogGuideBookItem;
import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

public class ToolsOfTheGodsOrbItems {
	public static final TogRegistryEntry<Item> WHITE_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> YELLOW_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> PURPLE_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> RED_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> BLACK_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> GREEN_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> BLUE_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> UNIVERSE_GEM = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> TRAIT_REMOVER = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> TOG_GUIDE_BOOK = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> XP_INFUSION_MINOR = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> XP_INFUSION_MAJOR = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> XP_INFUSION_GRAND = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ELIXIR_XP_FOCUS = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ELIXIR_XP_SURGE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ELIXIR_XP_RAPTURE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<Item> ELIXIR_XP_APOTHEOSIS = new TogRegistryEntry<>();

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
