package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

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
import net.mcreator.toolsofthegods.logic.context.TogShieldBlockContext;

import java.util.List;

/** Registers Fabric API callbacks and dispatches mixin-driven events to shared logic. */
public final class FabricGameplayEvents {
	private FabricGameplayEvents() {
	}

	public static void register() {
		registerPlayerLifecycle();
		registerInteraction();
		registerCombat();
		registerElytra();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
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
			if (entity instanceof ServerPlayer player) {
				GuideBookSpawnLogic.onPlayerJoinLevel(player);
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
			GuideBookSpawnLogic.onPlayerLogin(handler.player));

		ServerPlayerEvents.COPY_FROM.register((player, oldPlayer, alive) -> {
			GuideBookSpawnLogic.onPlayerClone(FabricEventAdapters.playerClone(oldPlayer, player, !alive));
			TraitSoulboundLogic.onPlayerClone(FabricEventAdapters.playerClone(oldPlayer, player, !alive));
		});
	}

	private static void registerInteraction() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (player == null || world.isClientSide()) {
				return InteractionResultHolder.pass(player != null ? player.getItemInHand(hand) : ItemStack.EMPTY);
			}
			ItemStack stack = player.getItemInHand(hand);
			var ctx = FabricEventAdapters.rightClickItem(player, world, stack);
			GuideBookOpenLogic.onRightClickItem(ctx);
			TraitToggleLogic.onRightClickItem(ctx);
			TogFeatureGateLogic.onRightClickItem(ctx);
			if (ctx.canceled()) {
				InteractionResult result = ctx.cancellationResult() != net.minecraft.world.InteractionResult.PASS
					? ctx.cancellationResult() : net.minecraft.world.InteractionResult.FAIL;
				return InteractionResultHolder.fail(stack);
			}
			return InteractionResultHolder.pass(stack);
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (player == null || world.isClientSide()) {
				return InteractionResult.PASS;
			}
			var ctx = FabricEventAdapters.rightClickBlock(player, world, player.getItemInHand(hand));
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
			if (player != null && !world.isClientSide()) {
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

	private static void registerCombat() {
		// Post-damage shield handling is wired via LivingEntityDamagePostMixin (AFTER_DAMAGE is 1.21+).
	}

	private static void registerElytra() {
		EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
			ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
			if (WingsElytraLogic.canElytraFly(chest, entity)) {
				if (tickElytra) {
					WingsElytraLogic.elytraFlightTick(chest, entity, entity.getFallFlyingTicks());
				}
				return true;
			}
			return false;
		});
		EntityElytraEvents.ALLOW.register(entity -> {
			ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
			return WingsElytraLogic.canElytraFly(chest, entity);
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
		if (player instanceof ServerPlayer serverPlayer) {
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
		var ctx = FabricEventAdapters.breakSpeed(player, state, pos, speed);
		MiningSpeedLogic.onBreakSpeed(ctx);
		return ctx.newSpeed();
	}

	public static void dispatchItemFished(Player player, List<ItemStack> drops) {
		var ctx = FabricEventAdapters.itemFished(player, drops);
		TraitSpecialToolsLogic.onItemFished(ctx);
		FishingRodProgressionLogic.onItemFished(ctx);
	}
}
