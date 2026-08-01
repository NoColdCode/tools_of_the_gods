package net.mcreator.toolsofthegods.config;

/**
 * Fabric defaults matching {@code tools_of_the_gods-common.toml} until Cloth Config wiring lands.
 */
public final class ToolsOfTheGodsCommonConfig {
	public static final class DoubleValue {
		private final double value;

		public DoubleValue(double value) {
			this.value = value;
		}

		public double get() {
			return value;
		}
	}

	public static final class IntValue {
		private final int value;

		public IntValue(int value) {
			this.value = value;
		}

		public int get() {
			return value;
		}
	}

	public static final class BooleanValue {
		private final boolean value;

		public BooleanValue(boolean value) {
			this.value = value;
		}

		public boolean get() {
			return value;
		}
	}

	public static final DoubleValue XP_CURVE_BASE = new DoubleValue(50.0d);
	public static final DoubleValue XP_EXPONENT_BASE = new DoubleValue(0.15d);
	public static final DoubleValue XP_EXPONENT_PER_LEVEL = new DoubleValue(0.01d);
	public static final DoubleValue XP_REQUIRED_MULTIPLIER = new DoubleValue(1.0d);
	public static final DoubleValue GLOBAL_XP_GAIN_MULTIPLIER = new DoubleValue(1.0d);
	public static final DoubleValue MINING_XP_GAIN_MULTIPLIER = new DoubleValue(1.0d);
	public static final DoubleValue COMBAT_XP_GAIN_MULTIPLIER = new DoubleValue(1.0d);
	public static final DoubleValue SWORD_XP_PACE_DIVISOR = new DoubleValue(1.2d);
	public static final DoubleValue HOE_XP_PACE_DIVISOR = new DoubleValue(5.0d);
	public static final DoubleValue BOW_XP_PACE_DIVISOR = new DoubleValue(1.2d);
	public static final DoubleValue SHIELD_XP_PACE_DIVISOR = new DoubleValue(4.0d);
	public static final DoubleValue SHIELD_BLOCK_CHANCE_MIN = new DoubleValue(0.50d);
	public static final DoubleValue SHIELD_BLOCK_CHANCE_MAX = new DoubleValue(0.99d);
	public static final IntValue ARMOR_WEAR_XP_INTERVAL = new IntValue(400);
	public static final BooleanValue TIER_PARTICLES_ENABLED = new BooleanValue(false);
	public static final BooleanValue EXTENDED_TOOLS_ENABLED = new BooleanValue(false);
	public static final BooleanValue CREATIVE_TIER_PREVIEWS_ENABLED = new BooleanValue(true);

	private ToolsOfTheGodsCommonConfig() {
	}
}
