package net.mcreator.toolsofthegods.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public class TierSystem {
	
	// Tier constants
	public static final int MAX_LEVEL = 100;
	public static final int LEVELS_PER_TIER = 10;
	public static final int MAX_TIER = 9; // Tiers 0-9 (levels 0-10, 10-20, ..., 90-100)
	
	/**
	 * Get the tier from a level (0-100)
	 * Tier 0: levels 0-9
	 * Tier 1: levels 10-19
	 * ...
	 * Tier 9: levels 90-100
	 */
	public static int getTierFromLevel(int level) {
		if (level >= MAX_LEVEL) return MAX_TIER;
		return level / LEVELS_PER_TIER;
	}
	
	/**
	 * Get the level within the current tier (0-10)
	 * Used for bonus calculations
	 */
	public static int getLevelInTier(int level) {
		if (level >= MAX_LEVEL) return LEVELS_PER_TIER;
		return level % LEVELS_PER_TIER;
	}
	
	/**
	 * Calculate mining speed bonus based on tier and level within tier
	 * @param level The current tool level (0-100)
	 * @return The mining speed multiplier
	 */
	public static float getMiningSpeedBonus(int level) {
		return getMiningSpeedBonus(getTierFromLevel(level), getLevelInTier(level));
	}

	/**
	 * Calculate mining speed bonus from an explicit tier state.
	 * This is used for tools parked on an upgrade boundary so they keep
	 * the fully-earned stats from the previous tier until upgraded.
	 */
	public static float getMiningSpeedBonus(int tier, int levelInTier) {
		tier = Math.max(0, Math.min(MAX_TIER, tier));
		levelInTier = Math.max(0, Math.min(LEVELS_PER_TIER, levelInTier));
		
		float bonus = 0;
		
		switch (tier) {
			case 0: // Levels 0-9: 0.13 * levelInTier
				bonus = 0.13f * levelInTier;
				break;
			case 1: // Levels 10-19: 0.18 * levelInTier
				bonus = 0.18f * levelInTier;
				break;
			case 2: // Levels 20-29: 0.25 * levelInTier
				bonus = 0.25f * levelInTier;
				break;
			case 3: // Levels 30-39: 0.30 * levelInTier
				bonus = 0.30f * levelInTier;
				break;
			case 4: // Levels 40-49: 0.45 * levelInTier
				bonus = 0.45f * levelInTier;
				break;
			case 5: // Levels 50-59: 0.60 * levelInTier
				bonus = 0.60f * levelInTier;
				break;
			case 6: // Levels 60-69: 1.0 * levelInTier
				bonus = 1.0f * levelInTier;
				break;
			case 7: // Levels 70-79: 1.5 * levelInTier
				bonus = 1.5f * levelInTier;
				break;
			case 8: // Levels 80-89: 2.2 * levelInTier
				bonus = 2.2f * levelInTier;
				break;
			case 9: // Levels 90-100: 2.5 * levelInTier
				bonus = 2.5f * levelInTier;
				break;
		}
		
		// Add cumulative bonus from previous tiers
		float cumulativeBonus = getTierCumulativeBonus(tier);
		
		return 0.5f + cumulativeBonus + bonus; // Base 0.5 + all bonuses
	}
	
	/**
	 * Get cumulative bonus from all previous tiers
	 */
	private static float getTierCumulativeBonus(int tier) {
		float cumulative = 0;
		
		if (tier >= 1) cumulative += 1.3f;  // Max from tier 0
		if (tier >= 2) cumulative += 1.8f;  // Max from tier 1
		if (tier >= 3) cumulative += 2.5f;  // Max from tier 2
		if (tier >= 4) cumulative += 3.0f;  // Max from tier 3
		if (tier >= 5) cumulative += 4.5f;  // Max from tier 4
		if (tier >= 6) cumulative += 6.0f;  // Max from tier 5
		if (tier >= 7) cumulative += 10.0f; // Max from tier 6
		if (tier >= 8) cumulative += 15.0f; // Max from tier 7
		if (tier >= 9) cumulative += 22.0f; // Max from tier 8
		
		return cumulative;
	}
	
	/**
	 * Get the texture name for the given tier
	 */
	public static String getTextureForTier(int tier) {
		switch (tier) {
			case 0: return "item21"; // Tier 0 (levels 0-9)
			case 1: return "item22"; // Tier 1 (levels 10-19)
			case 2: return "item24"; // Tier 2 (levels 20-29)
			case 3: return "item25"; // Tier 3 (levels 30-39)
			case 4: return "item23"; // Tier 4 (levels 40-49)
			case 5: return "item37"; // Tier 5 (levels 50-59)
			case 6: return "item27"; // Tier 6 (levels 60-69)
			case 7: return "item38"; // Tier 7 (levels 70-79)
			case 8: return "item26"; // Tier 8 (levels 80-89)
			case 9: return "item32"; // Tier 9 (levels 90-100)
			default: return "item21";
		}
	}
	
	/**
	 * Get the tier name
	 */
	public static String getTierName(int tier) {
		switch (tier) {
			case 0: return "Primal Wooden";
			case 1: return "Crude Stone";
			case 2: return "Hewn Iron";
			case 3: return "Gilded Gold";
			case 4: return "Lapis-Touched";
			case 5: return "Redstone-Forged";
			case 6: return "Obsidian Runed";
			case 7: return "Arcane Emerald";
			case 8: return "Ethereal Diamond";
			case 9: return "Pickaxe of the Gods";
			default: return "Unknown";
		}
	}

	/** Display names for Armor of the Gods tiers (leather → chain → iron → gold, then gem ascension). */
	public static String getArmorTierName(int tier) {
		switch (Math.max(0, Math.min(MAX_TIER, tier))) {
			case 0: return "Worn Leather";
			case 1: return "Rusty Chainmail";
			case 2: return "Forged Iron";
			case 3: return "Gilded Leather";
			case 4: return "Amethyst-Touched";
			case 5: return "Ruby-Forged";
			case 6: return "Obsidian Runed";
			case 7: return "Arcane Emerald";
			case 8: return "Ethereal Diamond";
			default: return "Divine";
		}
	}

	/**
	 * Wings display names (Dreamy elytra art). Upgrade gems stay on the normal tool path.
	 * Suffix follows flight mode: Cape (0–1) → Elytra (2–5) → Wings (6–9).
	 * Tier 9 is always {@code Wings of the Gods}.
	 */
	public static String getWingsTierName(int tier) {
		return switch (Math.max(0, Math.min(MAX_TIER, tier))) {
			case 0 -> "Crow";
			case 1 -> "Magpie";
			case 2 -> "Phantom";
			case 3 -> "Crimson Rosella";
			case 4 -> "Blue and Gold Macaw";
			case 5 -> "Scarlet Macaw";
			case 6 -> "Dark Spix Macaw";
			case 7 -> "Spix Macaw";
			case 8 -> "Allay";
			default -> "Wings of the Gods";
		};
	}

	/** Full inventory name for a wings tier (includes Cape / Elytra / Wings). */
	public static String getWingsDisplayName(int tier) {
		tier = Math.max(0, Math.min(MAX_TIER, tier));
		if (tier >= MAX_TIER) {
			return "Wings of the Gods";
		}
		String style = getWingsTierName(tier);
		if (tier <= 1) {
			return style + " Cape";
		}
		if (tier <= 5) {
			return style + " Elytra";
		}
		return style + " Wings";
	}

	public static String getPickaxeDisplayName(int tier) {
		if (tier >= MAX_TIER) {
			return "Pickaxe of the Gods";
		}
		return getTierName(tier) + " Pickaxe";
	}

	/**
	 * Tiered harvest capability for blocks that require higher tool grades.
	 * Tier 2 behaves like iron as requested.
	 */
	public static boolean canHarvest(BlockState state, int tier) {
		if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			return false;
		}

		if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
			return tier >= 8;
		}

		if (state.is(BlockTags.NEEDS_IRON_TOOL)) {
			return tier >= 2;
		}

		if (state.is(BlockTags.NEEDS_STONE_TOOL)) {
			return tier >= 1;
		}

		return true;
	}

	/**
	 * One-shot whitelist per tier, restricted to pickaxe-minable blocks only.
	 * Adjusted to make one-shot rare before level 50 (tier 5).
	 */
	public static boolean isOneShotBlock(BlockState state, int tier) {
		if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			return false;
		}

		// Tier 5 (Level 50-59): Coal ores
		if (tier >= 5 && state.is(BlockTags.COAL_ORES)) {
			return true;
		}

		// Tier 6 (Level 60-69): Stone and copper ores
		if (tier >= 6 && (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.COPPER_ORES))) {
			return true;
		}

		// Tier 7 (Level 70-79): Deepslate and gold ores
		if (tier >= 7 && (state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES) || state.is(BlockTags.GOLD_ORES))) {
			return true;
		}

		// Tier 8 (Level 80-89): Iron and redstone ores
		if (tier >= 8 && (state.is(BlockTags.IRON_ORES) || state.is(BlockTags.REDSTONE_ORES))) {
			return true;
		}

		// Tier 9 (Level 90-100): Diamond ores and all other blocks
		if (tier >= 9 && state.is(BlockTags.DIAMOND_ORES)) {
			return true;
		}

		return tier >= 9;
	}
	
	/**
	 * Calculate XP needed for next level
	 */
	public static int getXpForNextLevel(int level) {
		if (level >= MAX_LEVEL) return 0;
		
		int tier = getTierFromLevel(level);
		int levelInTier = getLevelInTier(level);
		
		// Base XP requirements scale with level
		int baseXP = 10 + (level * 5);
		
		// Double XP requirement at tier boundaries (levels 10, 20, 30, etc.)
		if (levelInTier == 9) {
			baseXP *= 3; // Triple for tier upgrades
		}
		
		return baseXP;
	}
	
	/**
	 * Check if level is at a tier boundary (requires upgrade)
	 */
	public static boolean isAtTierBoundary(int level) {
		return level > 0 && level % LEVELS_PER_TIER == 0 && level < MAX_LEVEL;
	}
	
	/**
	 * Get the required upgrade material message for the tier.
	 *
	 * @param forArmor when true, tier 1 uses chains instead of cobblestone
	 */
	public static String getUpgradeMaterialMessage(int tier, boolean forArmor) {
		if (forArmor && tier == 1) {
			return "Requires: 8 Chains";
		}
		return getUpgradeMaterialMessage(tier);
	}

	/**
	 * Get the required upgrade material message for tools (and non-armor gear).
	 */
	public static String getUpgradeMaterialMessage(int tier) {
		switch (tier) {
			case 1: return "Requires: 32 Cobblestone";
			case 2: return "Requires: White Gem";
			case 3: return "Requires: Yellow Gem";
			case 4: return "Requires: Purple Gem";
			case 5: return "Requires: Red Gem";
			case 6: return "Requires: Black Gem";
			case 7: return "Requires: Green Gem";
			case 8: return "Requires: Blue Gem";
			case 9: return "Requires: Universe Gem";
			default: return "";
		}
	}

	public static int getActiveHasteAmplifier(int tier) {
		return switch (tier) {
			case 1 -> 2; // Haste III
			case 2 -> 2;
			case 3 -> 2;
			case 4 -> 2;
			case 5 -> 1; // Haste II
			case 6 -> 2;
			case 7 -> 3; // Haste IV
			case 8 -> 4; // Haste V
			case 9 -> 5;
			default -> -1;
		};
	}

	public static int getActiveHasteDurationTicks(int tier) {
		return switch (tier) {
			case 1 -> 60;   // 3s
			case 2 -> 100;  // 5s
			case 3 -> 120;  // 6s
			case 4 -> 120;
			case 5 -> 160;  // 8s
			case 6 -> 300;  // 15s
			case 7 -> 300;
			case 8 -> 400;
			case 9 -> 500;
			default -> 0;
		};
	}

	public static int getActiveCooldownTicks(int tier) {
		return switch (tier) {
			case 6, 7 -> 900;  // 45s
			case 8 -> 2400;    // 2min
			case 9 -> 2000;
			default -> 1200;   // 1min
		};
	}

	public static boolean grantsActiveFlight(int tier) {
		return tier >= 8;
	}

	public static int getActiveFlightDurationTicks(int tier) {
		if (tier >= 9) {
			return 800;
		}
		if (tier >= 8) {
			return 600; // 30s
		}
		return 0;
	}
}
