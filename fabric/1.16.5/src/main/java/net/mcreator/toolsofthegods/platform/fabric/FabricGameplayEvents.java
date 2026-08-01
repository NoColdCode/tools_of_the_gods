package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.logic.*;
import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogRightClickBlockContext;
import net.mcreator.toolsofthegods.logic.context.TogRightClickItemContext;
import net.mcreator.toolsofthegods.logic.context.TogShieldBlockContext;

import java.util.List;

/** Registers Fabric API callbacks and dispatches mixin-driven events to shared logic. */
public final class FabricGameplayEvents {
	private FabricGameplayEvents() {
	}

	public static void register() {
		registerPlayerLifecycle();
		registerInteraction();
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
			PickaxeCommandsLogic.register(dispatcher));
	}

	private static void registerPlayerLifecycle() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				onPlayerTickEnd(player);
			}
			for (ServerLevel level : server.getAllLevels()) {
				for (Entity entity : level.getAllEntities()) {
					TogMobProgressionLogic.onEntityTick(entity);
				}
			}
		});

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayer) {
				ServerPlayer player = (ServerPlayer) entity;
				GuideBookSpawnLogic.onPlayerJoinLevel(player);
			}
		});

		
		ServerPlayerEvents.COPY_FROM.register((player, oldPlayer, alive) -> {
			FabricPlayerAttachments.copyOnClone(oldPlayer, player);
			GuideBookSpawnLogic.onPlayerClone(FabricEventAdapters.playerClone(oldPlayer, player, !alive));
			TraitSoulboundLogic.onPlayerClone(FabricEventAdapters.playerClone(oldPlayer, player, !alive));
		});
	}

	private static void registerInteraction() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (player == null || world.isClientSide) {
				return InteractionResultHolder.pass(player != null ? player.getItemInHand(hand) : ItemStack.EMPTY);
			}
			ItemStack stack = player.getItemInHand(hand);
			TogRightClickItemContext ctx = FabricEventAdapters.rightClickItem(player, world, stack);
			GuideBookOpenLogic.onRightClickItem(ctx);
			TraitToggleLogic.onRightClickItem(ctx);
			TogFeatureGateLogic.onRightClickItem(ctx);
			if (ctx.canceled()) {
				return InteractionResultHolder.fail(stack);
			}
			return InteractionResultHolder.pass(stack);
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (player == null || world.isClientSide) {
				return InteractionResult.PASS;
			}
			TogRightClickBlockContext ctx = FabricEventAdapters.rightClickBlock(player, world, player.getItemInHand(hand));
			GuideBookOpenLogic.onRightClickBlock(ctx);
			if (ctx.canceled()) {
				return FabricEventAdapters.toUseItemResult(ctx);
			}
			if (ctx.denyBlockUse() || ctx.denyItemUse()) {
				return InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (player != null && !world.isClientSide) {
				UltimateAdaptiveModeLogic.onLeftClickBlock(
					FabricEventAdapters.leftClickBlock(player, world, player.getItemInHand(hand)));
			}
			return InteractionResult.PASS;
		});

		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			if (player != null) {
				UltimateAdaptiveModeLogic.onBreak(FabricEventAdapters.blockBreak(world, pos, state, player));
			}
			return true;
		});

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (player != null) {
				OrbLootLogic.onBlockBreak(FabricEventAdapters.blockBreak(world, pos, state, player));
			}
		});
	}

	static void onPlayerTickEnd(Player player) {
		TraitMechanicLogic.onPlayerTick(player);
		TraitSustainingLogic.onPlayerTick(player);
		TraitMagneticLogic.onPlayerTick(player);
		TraitAttributeLogic.onPlayerTick(player);
		TraitMomentumLogic.onPlayerTick(player);
		TraitSpecialToolsLogic.onPlayerTick(player);
		ShieldProgressionLogic.onPlayerTick(player);
		ArmorProgressionLogic.onPlayerTick(player);
		UltimateAdaptiveModeLogic.onPlayerTick(player);
		if (player instanceof ServerPlayer) {
			ServerPlayer serverPlayer = (ServerPlayer) player;
			PickaxePowerLogic.onPlayerTick(serverPlayer);
		}
	}

	public static void dispatchBlockDrops(TogBlockDropsContext ctx) {
		TraitSilkyLogic.onBlockDrops(ctx);
		TraitMechanicLogic.onBlockDrops(ctx);
		TraitSustainingLogic.onBlockDrops(ctx);
		TraitAutosmeltLogic.onBlockDrops(ctx);
		TraitMomentumLogic.onBlockDrops(ctx);
		TogMobProgressionLogic.onBlockDrops(ctx);
		TraitBroadTouchLogic.onBlockDrops(ctx);
	}

	public static void dispatchLivingDrops(TogLivingDropsContext ctx) {
		TraitMechanicLogic.onLivingDrops(ctx);
		TraitSustainingLogic.onLivingDrops(ctx);
		TraitAutosmeltLogic.onLivingDrops(ctx);
		TraitSoulboundLogic.onPlayerDrops(ctx);
		OrbLootLogic.onLivingDrops(ctx);
	}

	public static float dispatchIncomingDamage(TogIncomingDamageContext ctx) {
		TraitThornsLogic.onPlayerDamaged(ctx);
		TraitCombatDamageLogic.onLivingIncomingDamage(ctx);
		TraitSpecialToolsLogic.onLivingIncomingDamage(ctx);
		ArmorProgressionLogic.onIncomingDamage(ctx);
		TogMobProgressionLogic.onLivingDamage(ctx);
		return ctx.amount();
	}

	public static void dispatchShieldBlock(TogShieldBlockContext ctx) {
		ShieldProgressionLogic.onShieldBlock(ctx);
	}

	public static void dispatchLivingDeath(LivingEntity entity, DamageSource source) {
		TraitVitalityLogic.onLivingDeath(FabricEventAdapters.livingDeath(entity, source));
	}

	public static float dispatchBreakSpeed(Player player, net.minecraft.world.level.block.state.BlockState state,
		net.minecraft.core.BlockPos pos, float speed) {
		net.mcreator.toolsofthegods.logic.context.TogBreakSpeedContext ctx = FabricEventAdapters.breakSpeed(player, state, pos, speed);
		MiningSpeedLogic.onBreakSpeed(ctx);
		return ctx.newSpeed();
	}

	public static void dispatchItemFished(Player player, List<ItemStack> drops) {
		net.mcreator.toolsofthegods.logic.context.TogItemFishedContext ctx = FabricEventAdapters.itemFished(player, drops);
		TraitSpecialToolsLogic.onItemFished(ctx);
		FishingRodProgressionLogic.onItemFished(ctx);
	}
}
