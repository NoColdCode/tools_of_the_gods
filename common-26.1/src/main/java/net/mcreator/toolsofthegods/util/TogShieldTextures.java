package net.mcreator.toolsofthegods.util;

/**
 * Shield item textures under {@code textures/item/}.
 * Same first-letter tier prefix convention as {@link TogArmorTextures}.
 */
public final class TogShieldTextures {

	private TogShieldTextures() {
	}

	public static String itemTexture(int tier) {
		int t = Math.max(0, Math.min(9, tier));
		if (t == 9) {
			return "shieldog";
		}
		return switch (t) {
			case 0 -> "hshield";
			case 1 -> "cshield";
			case 2 -> "ishield";
			case 3 -> "gshield";
			case 4 -> "ashield";
			case 5 -> "ushield";
			case 6 -> "oshield";
			case 7 -> "eshield";
			default -> "dshield";
		};
	}
}
