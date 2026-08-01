package net.mcreator.toolsofthegods.platform.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.mcreator.toolsofthegods.logic.context.TogBlockBreakContext;
import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogBreakSpeedContext;
import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;
import net.mcreator.toolsofthegods.logic.context.TogItemFishedContext;
import net.mcreator.toolsofthegods.logic.context.TogLeftClickBlockContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDamagePostContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDeathContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogPlayerCloneContext;
import net.mcreator.toolsofthegods.logic.context.TogRightClickBlockContext;
import net.mcreator.toolsofthegods.logic.context.TogRightClickItemContext;
import net.mcreator.toolsofthegods.logic.context.TogShieldBlockContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Maps vanilla/Fabric callback data to loader-neutral {@code logic.context} types. */
public final class FabricEventAdapters {
	private FabricEventAdapters() {
	}

	public static TogBlockDropsContext blockDrops(Level level, BlockPos pos, BlockState state, Entity breaker,
		List<ItemEntity> drops, BlockEntity blockEntity) {
		return new TogBlockDropsContext(level, pos, state, breaker, new ArrayList<>(drops), blockEntity);
	}

	public static TogLivingDropsContext livingDrops(LivingEntity entity, DamageSource source, List<ItemEntity> drops) {
		return new TogLivingDropsContext(entity, source, new ArrayList<>(drops));
	}

	public static TogIncomingDamageContext incomingDamage(LivingEntity entity, DamageSource source, float amount) {
		return new TogIncomingDamageContext(entity, source, amount);
	}

	public static TogShieldBlockContext shieldBlock(LivingEntity entity, DamageSource source, boolean originalBlock, float originalBlockedDamage) {
		return new TogShieldBlockContext(entity, source, originalBlock, originalBlockedDamage);
	}

	public static TogLivingDamagePostContext livingDamagePost(LivingEntity entity, float newDamage, float blockedDamage) {
		return new TogLivingDamagePostContext(entity, newDamage, blockedDamage);
	}

	public static TogBreakSpeedContext breakSpeed(Player player, BlockState state, BlockPos pos, float originalSpeed) {
		return new TogBreakSpeedContext(player, state, Optional.ofNullable(pos), originalSpeed);
	}

	public static TogRightClickItemContext rightClickItem(Player player, Level level, ItemStack stack) {
		return new TogRightClickItemContext(player, level, stack);
	}

	public static TogRightClickBlockContext rightClickBlock(Player player, Level level, ItemStack stack) {
		return new TogRightClickBlockContext(player, level, stack);
	}

	public static TogLeftClickBlockContext leftClickBlock(Player player, Level level, ItemStack stack) {
		return new TogLeftClickBlockContext(player, level, stack);
	}

	public static TogBlockBreakContext blockBreak(Level level, BlockPos pos, BlockState state, Player player) {
		return new TogBlockBreakContext(level, pos, state, player);
	}

	public static TogLivingDeathContext livingDeath(LivingEntity entity, DamageSource source) {
		return new TogLivingDeathContext(entity, source);
	}

	public static TogItemFishedContext itemFished(Player player, List<ItemStack> drops) {
		return new TogItemFishedContext(player, drops);
	}

	public static TogPlayerCloneContext playerClone(ServerPlayer original, ServerPlayer player, boolean wasDeath) {
		return new TogPlayerCloneContext(original, player, wasDeath);
	}

	public static InteractionResult toUseItemResult(TogRightClickBlockContext ctx) {
		if (ctx.canceled()) {
			return ctx.cancellationResult() != InteractionResult.PASS ? ctx.cancellationResult() : InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}
}
