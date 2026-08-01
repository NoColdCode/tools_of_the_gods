package net.mcreator.toolsofthegods.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public final class UltimateToolModeHelper {
	private static final String NBT_TOOL_MODE = "togUltimateToolMode";
	private static final String NBT_MANUAL_UNTIL = "togUltimateManualModeUntil";

	private UltimateToolModeHelper() {
	}

	public static UltimateToolMode getMode(ItemStack stack) {
		int ordinal = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBT_TOOL_MODE, 0);
		return UltimateToolMode.fromOrdinal(ordinal);
	}

	public static void setMode(ItemStack stack, UltimateToolMode mode, boolean manual) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			tag.putInt(NBT_TOOL_MODE, mode.ordinal());
			if (manual) {
				tag.putLong(NBT_MANUAL_UNTIL, System.currentTimeMillis() + 8000L);
			}
		});
	}

	public static boolean isManualLockActive(ItemStack stack) {
		long until = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getLongOr(NBT_MANUAL_UNTIL, 0L);
		return until > System.currentTimeMillis();
	}

	public static UltimateToolMode detectAdaptiveMode(Player player, ItemStack stack) {
		if (player.isInWater() || isLookingAtWater(player)) {
			return UltimateToolMode.FISHING_ROD;
		}

		HitResult hit = player.pick(6.0d, 0.0f, false);
		if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living
			&& living.isAlive() && living != player) {
			double dist = player.distanceTo(living);
			if (dist > 4.5d) {
				return UltimateToolMode.BOW;
			}
			return UltimateToolMode.SWORD;
		}

		if (hit instanceof BlockHitResult blockHit) {
			BlockState state = player.level().getBlockState(blockHit.getBlockPos());
			if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
				return UltimateToolMode.SHOVEL;
			}
			if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
				return UltimateToolMode.AXE;
			}
			if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
				return UltimateToolMode.HOE;
			}
			if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
				if (TraitSystem.hasBroadTouch(stack)) {
					return UltimateToolMode.HAMMER;
				}
				return UltimateToolMode.PICKAXE;
			}
		}

		if (player.isFallFlying()) {
			return UltimateToolMode.BOW;
		}

		return getMode(stack);
	}

	private static boolean isLookingAtWater(Player player) {
		Vec3 eye = player.getEyePosition();
		Vec3 look = player.getViewVector(1.0f).scale(6.0d);
		BlockHitResult fluidHit = player.level().clip(new ClipContext(eye, eye.add(look), ClipContext.Block.COLLIDER,
			ClipContext.Fluid.SOURCE_ONLY, player));
		if (fluidHit.getType() != HitResult.Type.BLOCK) {
			return false;
		}
		FluidState fluid = player.level().getFluidState(fluidHit.getBlockPos());
		return fluid.is(FluidTags.WATER);
	}

	public static void tryApplyAdaptiveMode(Player player, ItemStack stack) {
		if (!TraitSystem.hasTrait(stack, TraitSystem.Trait.ADAPTIVE_I)) {
			return;
		}
		if (isManualLockActive(stack)) {
			return;
		}
		UltimateToolMode detected = detectAdaptiveMode(player, stack);
		if (detected != getMode(stack)) {
			setMode(stack, detected, false);
		}
	}
}
