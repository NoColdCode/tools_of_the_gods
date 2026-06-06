package net.mcreator.toolsofthegods.util;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

/**
 * Armor item icon names under {@code assets/tools_of_the_gods/textures/item/}.
 * Leather → chainmail → iron → gold, then gem tiers (not wood/stone tool naming).
 * Worn layers: {@code textures/models/armor/{prefix}_layer_1.png} and {@code _layer_2.png} per tier.
 */
public final class TogArmorTextures {

	public enum Piece {
		HELMET("helm", "helmog"),
		CHESTPLATE("chest", "chestog"),
		LEGGINGS("leg", "leggog"),
		BOOTS("boot", "bootog");

		private final String suffix;
		private final String tierNine;

		Piece(String suffix, String tierNine) {
			this.suffix = suffix;
			this.tierNine = tierNine;
		}
	}

	private TogArmorTextures() {
	}

	public static String itemTexture(Piece piece, int tier) {
		int t = Math.max(0, Math.min(9, tier));
		if (t == 9) {
			return piece.tierNine;
		}
		return switch (t) {
			case 0 -> "h" + piece.suffix;
			case 1 -> "c" + piece.suffix;
			case 2 -> "i" + piece.suffix;
			case 3 -> "g" + piece.suffix;
			case 4 -> "a" + piece.suffix;
			case 5 -> "u" + piece.suffix;
			case 6 -> "o" + piece.suffix;
			case 7 -> "e" + piece.suffix;
			default -> "d" + piece.suffix;
		};
	}

	/** Prefix for worn armor layer files ({@code h_layer_1.png}, …, {@code og_layer_2.png}). */
	public static String wornLayerPrefix(int tier) {
		int t = Math.max(0, Math.min(9, tier));
		if (t == 9) {
			return "og";
		}
		return switch (t) {
			case 0 -> "h";
			case 1 -> "c";
			case 2 -> "i";
			case 3 -> "g";
			case 4 -> "a";
			case 5 -> "u";
			case 6 -> "o";
			case 7 -> "e";
			default -> "d";
		};
	}

	public static ResourceLocation wornLayerTexture(int tier, boolean innerLayer) {
		String prefix = wornLayerPrefix(tier);
		int layer = innerLayer ? 2 : 1;
		return ResourceLocation.fromNamespaceAndPath(
			ToolsOfTheGodsMod.MODID,
			"textures/models/armor/" + prefix + "_layer_" + layer + ".png"
		);
	}
}
