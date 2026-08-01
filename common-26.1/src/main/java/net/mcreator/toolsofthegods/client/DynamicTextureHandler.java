package net.mcreator.toolsofthegods.client;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import org.jetbrains.annotations.Nullable;

/**
 * Tier-based tool textures on 26.1+ use item model definitions with a custom
 * {@link RangeSelectItemModelProperty} instead of the removed {@code ItemProperties} API.
 */
public final class DynamicTextureHandler {
	public static final Identifier TIER_PROPERTY_ID =
		Identifier.fromNamespaceAndPath(TogModConstants.MODID, "tier");

	private DynamicTextureHandler() {
	}

	public static void init() {
		// Registration happens in NeoForgeClientSetup via RegisterRangeSelectItemModelPropertyEvent.
	}

	public record ToolTierProperty() implements RangeSelectItemModelProperty {
		public static final MapCodec<ToolTierProperty> MAP_CODEC = MapCodec.unit(new ToolTierProperty());

		@Override
		public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
			if (!ToolProgressionHelper.isTogTool(stack)) {
				return 0.0f;
			}
			return ToolProgressionHelper.getStoredTier(stack) / 10.0f;
		}

		@Override
		public MapCodec<ToolTierProperty> type() {
			return MAP_CODEC;
		}
	}
}
