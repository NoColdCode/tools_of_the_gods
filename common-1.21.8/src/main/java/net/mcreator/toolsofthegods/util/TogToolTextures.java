package net.mcreator.toolsofthegods.util;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

/**
 * Tiered item texture file names under {@code assets/tools_of_the_gods/textures/item/}.
 * Pattern: [tier letter][tool suffix], e.g. {@code Rtrid.png} at tier 5 (level 50).
 */
public final class TogToolTextures {

	private TogToolTextures() {
	}

	/** Texture key for trident tier 0–9 (tier 9 = TRIDOG). */
	public static String trident(int tier) {
		return switch (Math.max(0, Math.min(9, tier))) {
			case 0 -> "wtrid";
			case 1 -> "strid";
			case 2 -> "itrid";
			case 3 -> "gtrid";
			case 4 -> "atrid";
			case 5 -> "rtrid";
			case 6 -> "otrid";
			case 7 -> "etrid";
			case 8 -> "dtrid";
			default -> "tridog";
		};
	}

	/** Texture key for fishing rod; only five visuals (tiers 0–4, then fishog). */
	public static String fishingRod(int tier) {
		int visualTier = Math.min(4, Math.max(0, tier));
		return switch (visualTier) {
			case 0 -> "wfish";
			case 1 -> "ifish";
			case 2 -> "rfish";
			case 3 -> "dfish";
			default -> "fishog";
		};
	}

	/** Texture key for spear tier 0–9 (tier 9 = spog). */
	public static String spear(int tier) {
		return switch (Math.max(0, Math.min(9, tier))) {
			case 0 -> "wspe";
			case 1 -> "sspe";
			case 2 -> "ispe";
			case 3 -> "gspe";
			case 4 -> "aspe";
			case 5 -> "rspe";
			case 6 -> "ospe";
			case 7 -> "espe";
			case 8 -> "dspe";
			default -> "spog";
		};
	}

	public static int visualTierForTexture(ToolType type, int tier) {
		if (type == ToolType.FISHING_ROD) {
			return Math.min(4, Math.max(0, tier));
		}
		return Math.max(0, Math.min(9, tier));
	}
}
