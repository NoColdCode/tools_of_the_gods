package net.mcreator.toolsofthegods.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ToolsOfTheGodsCommonConfig {
	public static final ModConfigSpec SPEC;

	public static final ModConfigSpec.DoubleValue XP_CURVE_BASE;
	public static final ModConfigSpec.DoubleValue XP_EXPONENT_BASE;
	public static final ModConfigSpec.DoubleValue XP_EXPONENT_PER_LEVEL;
	public static final ModConfigSpec.DoubleValue XP_REQUIRED_MULTIPLIER;
	public static final ModConfigSpec.DoubleValue GLOBAL_XP_GAIN_MULTIPLIER;
	public static final ModConfigSpec.DoubleValue MINING_XP_GAIN_MULTIPLIER;
	public static final ModConfigSpec.DoubleValue COMBAT_XP_GAIN_MULTIPLIER;
	public static final ModConfigSpec.DoubleValue SWORD_XP_PACE_DIVISOR;
	public static final ModConfigSpec.DoubleValue HOE_XP_PACE_DIVISOR;
	public static final ModConfigSpec.DoubleValue BOW_XP_PACE_DIVISOR;
	public static final ModConfigSpec.DoubleValue SHIELD_XP_PACE_DIVISOR;

	public static final ModConfigSpec.DoubleValue SHIELD_BLOCK_CHANCE_MIN;
	public static final ModConfigSpec.DoubleValue SHIELD_BLOCK_CHANCE_MAX;
	public static final ModConfigSpec.IntValue ARMOR_WEAR_XP_INTERVAL;
	public static final ModConfigSpec.BooleanValue TIER_PARTICLES_ENABLED;
	public static final ModConfigSpec.BooleanValue EXTENDED_TOOLS_ENABLED;
	public static final ModConfigSpec.BooleanValue CREATIVE_TIER_PREVIEWS_ENABLED;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

		builder.push("progression");
		XP_CURVE_BASE = builder
			.comment("Base XP value in formula: floor(base * level^(expBase + expPerLevel * level)).")
			.defineInRange("xpCurveBase", 50.0d, 1.0d, 10000.0d);
		XP_EXPONENT_BASE = builder
			.comment("Base exponent in the XP formula.")
			.defineInRange("xpExponentBase", 0.15d, 0.01d, 3.0d);
		XP_EXPONENT_PER_LEVEL = builder
			.comment("Per-level exponent growth in the XP formula.")
			.defineInRange("xpExponentPerLevel", 0.01d, 0.0d, 1.0d);
		XP_REQUIRED_MULTIPLIER = builder
			.comment("Multiplier applied to required XP for each level.")
			.defineInRange("xpRequiredMultiplier", 1.0d, 0.1d, 100.0d);
		GLOBAL_XP_GAIN_MULTIPLIER = builder
			.comment("Global multiplier applied to all gained XP.")
			.defineInRange("globalXpGainMultiplier", 1.0d, 0.1d, 100.0d);
		MINING_XP_GAIN_MULTIPLIER = builder
			.comment("Additional multiplier for mining/tool-use XP.")
			.defineInRange("miningXpGainMultiplier", 1.0d, 0.1d, 100.0d);
		COMBAT_XP_GAIN_MULTIPLIER = builder
			.comment("Additional multiplier for combat/defense XP.")
			.defineInRange("combatXpGainMultiplier", 1.0d, 0.1d, 100.0d);
		SWORD_XP_PACE_DIVISOR = builder
			.comment("Divides sword XP gain by this value (higher = slower leveling). Default 1.2.")
			.defineInRange("swordXpPaceDivisor", 1.2d, 1.0d, 100.0d);
		HOE_XP_PACE_DIVISOR = builder
			.comment("Divides hoe XP gain by this value. Default 5.0.")
			.defineInRange("hoeXpPaceDivisor", 5.0d, 1.0d, 100.0d);
		BOW_XP_PACE_DIVISOR = builder
			.comment("Divides bow XP gain by this value. Default 1.2.")
			.defineInRange("bowXpPaceDivisor", 1.2d, 1.0d, 100.0d);
		SHIELD_XP_PACE_DIVISOR = builder
			.comment("Divides shield XP gain by this value. Default 4.0.")
			.defineInRange("shieldXpPaceDivisor", 4.0d, 1.0d, 100.0d);
		builder.pop();

		builder.push("shield");
		SHIELD_BLOCK_CHANCE_MIN = builder
			.comment("Minimum block chance at level 0 (0.0 - 1.0).")
			.defineInRange("blockChanceMin", 0.50d, 0.0d, 1.0d);
		SHIELD_BLOCK_CHANCE_MAX = builder
			.comment("Maximum block chance at level 100 (0.0 - 1.0).")
			.defineInRange("blockChanceMax", 0.99d, 0.0d, 1.0d);
		builder.pop();

		builder.push("armor");
		ARMOR_WEAR_XP_INTERVAL = builder
			.comment("Ticks between passive wear XP per equipped armor piece (20 ticks = 1 second).")
			.defineInRange("wearXpInterval", 400, 20, 72000);
		builder.pop();

		builder.push("features");
		EXTENDED_TOOLS_ENABLED = builder
			.comment("Enable special weapons (trident, spear, staff, wings, ultimate tool, etc.). Set false for armor-only packs.")
			.define("extendedToolsEnabled", false);
		CREATIVE_TIER_PREVIEWS_ENABLED = builder
			.comment("Show every tier (0-9) of core tools and armor in the creative tab.")
			.define("creativeTierPreviewsEnabled", true);
		builder.pop();

		builder.push("client");
		TIER_PARTICLES_ENABLED = builder
			.comment("Spawn tier-colored particles on held TOG tools (disabled by default).")
			.define("tierParticlesEnabled", false);
		builder.pop();

		SPEC = builder.build();
	}

	private ToolsOfTheGodsCommonConfig() {
	}
}
