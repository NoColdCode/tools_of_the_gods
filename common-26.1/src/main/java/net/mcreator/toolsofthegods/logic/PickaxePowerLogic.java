package net.mcreator.toolsofthegods.logic;


import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.TogEffectHelper;
import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PickaxePowerLogic {
	private static final Map<UUID, Long> FLIGHT_END_TICKS = new ConcurrentHashMap<>();
	private static final String POWER_COOLDOWN_KEY = "togPowerCooldownEnd";

	public static void activatePower(ServerPlayer player) {
		ItemStack held = player.getMainHandItem();
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(held);
		if (type == ToolProgressionHelper.ToolType.NONE) {
			player.sendOverlayMessage(Component.literal("§cHold a TOG tool to activate power."));
			return;
		}

		int level = (int) held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("level", 0.0);
		int tier = ToolProgressionHelper.getStoredTier(held);
		long gameTime = player.level().getGameTime();
		long cooldownUntil = (long) held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr(POWER_COOLDOWN_KEY, 0.0);

		if (gameTime < cooldownUntil) {
			long remaining = cooldownUntil - gameTime;
			player.sendOverlayMessage(Component.literal("§ePower cooldown: " + (remaining / 20) + "s"));
			return;
		}

		int hasteAmplifier = TierSystem.getActiveHasteAmplifier(tier);
		int hasteDuration = TierSystem.getActiveHasteDurationTicks(tier);
		int cooldown = TierSystem.getActiveCooldownTicks(tier);
		if (type == ToolProgressionHelper.ToolType.BOW) {
			hasteAmplifier = Math.max(1, tier);
			hasteDuration = 80 + tier * 20;
			cooldown = 800;
		}

		if (hasteAmplifier >= 0 && hasteDuration > 0) {
			player.addEffect(new MobEffectInstance(MobEffects.HASTE, hasteDuration, hasteAmplifier, false, true, true));
		}

		if (TierSystem.grantsActiveFlight(tier)) {
			int flyDuration = TierSystem.getActiveFlightDurationTicks(tier);
			long endTick = gameTime + flyDuration;
			FLIGHT_END_TICKS.put(player.getUUID(), endTick);
			if (!player.getAbilities().mayfly) {
				player.getAbilities().mayfly = true;
			}
			player.onUpdateAbilities();
		}

		long newCooldown = gameTime + cooldown;
		CustomData.update(DataComponents.CUSTOM_DATA, held, tag -> tag.putDouble(POWER_COOLDOWN_KEY, newCooldown));
		player.sendOverlayMessage(Component.literal("§bPower activated!"));
	}

	public static void onPlayerTick(ServerPlayer player) {
		ServerLevel level = (ServerLevel) player.level();
		ItemStack held = player.getMainHandItem();
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(held);

		int tierForNightVision = TogEquipmentHelper.getHighestWornArmorTier(player);
		if (type != ToolProgressionHelper.ToolType.NONE) {
			int tier = ToolProgressionHelper.getStoredTier(held);
			tierForNightVision = Math.max(tierForNightVision, tier);
			applyPassiveEffects(player, type, tier);
		}

		if (tierForNightVision >= 6) {
			TogEffectHelper.refreshEffect(player, MobEffects.NIGHT_VISION, 0, 400);
		}

		handleFlightExpiration(player, level.getGameTime());
	}

	private static void applyPassiveEffects(Player player, ToolProgressionHelper.ToolType type, int tier) {
		if (type == ToolProgressionHelper.ToolType.SWORD && tier >= 4) {
			player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 240, Math.min(2, tier / 3), false, false, true));
		}

		if (type == ToolProgressionHelper.ToolType.BOW && tier >= 2) {
			player.addEffect(new MobEffectInstance(MobEffects.SPEED, 240, Math.min(2, tier / 2), false, false, true));
		}

		if (type == ToolProgressionHelper.ToolType.PICKAXE || type == ToolProgressionHelper.ToolType.HAMMER || type == ToolProgressionHelper.ToolType.AXE || type == ToolProgressionHelper.ToolType.SHOVEL || type == ToolProgressionHelper.ToolType.HOE) {
			if (tier >= 8) {
				player.addEffect(new MobEffectInstance(MobEffects.HASTE, 240, 4, false, false, true));
			} else if (tier >= 7) {
				player.addEffect(new MobEffectInstance(MobEffects.HASTE, 240, 3, false, false, true));
			}
		}
	}

	private static void handleFlightExpiration(ServerPlayer player, long gameTime) {
		Long end = FLIGHT_END_TICKS.get(player.getUUID());
		if (end == null) {
			return;
		}

		if (gameTime >= end) {
			FLIGHT_END_TICKS.remove(player.getUUID());
			if (!player.isCreative() && !player.isSpectator()) {
				player.getAbilities().mayfly = false;
				player.getAbilities().flying = false;
				player.onUpdateAbilities();
			}
		}
	}
}
