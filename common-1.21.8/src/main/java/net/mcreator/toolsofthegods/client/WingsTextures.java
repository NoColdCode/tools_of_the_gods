package net.mcreator.toolsofthegods.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.logic.WingsFlightLogic;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

/** Dreamy wing / cape / elytra entity textures by progression tier. */
public final class WingsTextures {
	private WingsTextures() {
	}

	public static ResourceLocation forTier(int tier) {
		int clamped = Mth.clamp(tier, 0, 9);
		return ResourceLocation.fromNamespaceAndPath(
			TogModConstants.MODID,
			"textures/entity/wings/tier" + clamped + ".png"
		);
	}

	public static ResourceLocation forStack(ItemStack stack) {
		if (!WingsFlightLogic.isWings(stack)) {
			return forTier(0);
		}
		return forTier(ToolProgressionHelper.getStoredTier(stack));
	}
}
