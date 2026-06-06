package net.mcreator.toolsofthegods.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;

import java.util.Optional;

/**
 * Charged spear thrust: extended reach and damage scale with level and charge.
 * Charge builds faster while riding a horse.
 */
public final class SpearThrustHelper {

	public static final int MIN_CHARGE_TICKS = 4;
	public static final int MAX_CHARGE_TICKS = 40;
	private static final double HORSE_CHARGE_MULTIPLIER = 2.0d;

	private SpearThrustHelper() {
	}

	public static boolean isSpear(ItemStack stack) {
		return stack.is(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get());
	}

	public static boolean isRidingHorse(Player player) {
		return player.getVehicle() instanceof AbstractHorse;
	}

	public static int effectiveChargeTicks(int useTicks, Player player) {
		int charge = Math.max(0, useTicks);
		if (isRidingHorse(player)) {
			charge = (int) Math.round(charge * HORSE_CHARGE_MULTIPLIER);
		}
		return Math.min(MAX_CHARGE_TICKS, charge);
	}

	public static float chargeRatio(int useTicks, Player player) {
		return Mth.clamp(effectiveChargeTicks(useTicks, player) / (float) MAX_CHARGE_TICKS, 0.0f, 1.0f);
	}

	public static double getThrustReach(ItemStack stack, int useTicks, Player player) {
		int level = (int) stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
			net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getDouble("level");
		double base = 3.0d + level * 0.03d;
		double chargeBonus = chargeRatio(useTicks, player) * 2.5d;
		return base + chargeBonus;
	}

	public static double getDisplayedReach(ItemStack stack) {
		int level = (int) stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
			net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getDouble("level");
		return 3.0d + level * 0.03d + 2.5d;
	}

	public static float getThrustDamage(Player player, ItemStack stack, int useTicks) {
		float chargeMult = 1.0f + chargeRatio(useTicks, player) * 0.45f;
		float horseMult = isRidingHorse(player) ? 1.12f : 1.0f;
		// Base 1.0; progression bonus is applied by TraitCombatDamageHandler on the hurt event.
		return 1.0f * chargeMult * horseMult * TraitSystem.getAttackDamageMultiplier(stack);
	}

	public static void performThrust(Player player, ItemStack stack, int useTicks) {
		if (useTicks < MIN_CHARGE_TICKS || player.level().isClientSide()) {
			return;
		}

		Level level = player.level();
		double reach = getThrustReach(stack, useTicks, player);
		LivingEntity target = findTargetInReach(player, reach);
		if (target == null) {
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP,
				SoundSource.PLAYERS, 0.6f, 1.2f);
			player.getCooldowns().addCooldown(stack.getItem(), 8);
			return;
		}

		float damage = getThrustDamage(player, stack, useTicks);
		target.hurt(level.damageSources().playerAttack(player), damage);
		TraitPoisonHelper.applyMeleePoison(stack, target);
		TraitFreezyHelper.applyMeleeFreeze(stack, target);
		ToolProgressionHelper.gainXp(level, player.getX(), player.getY(), player.getZ(), player, stack,
			ToolProgressionHelper.getSwordHitXp(target));

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW,
			SoundSource.PLAYERS, 0.8f, 1.0f + chargeRatio(useTicks, player) * 0.3f);
		player.getCooldowns().addCooldown(stack.getItem(), 10);
		player.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
		player.causeFoodExhaustion(0.2f);
	}

	public static LivingEntity findTargetInReach(Player player, double reach) {
		Vec3 start = player.getEyePosition();
		Vec3 look = player.getLookAngle();
		Vec3 end = start.add(look.scale(reach));
		AABB search = player.getBoundingBox().expandTowards(look.scale(reach)).inflate(1.0d, 0.5d, 1.0d);

		LivingEntity best = null;
		double bestDist = reach + 1.0d;

		for (Entity entity : player.level().getEntities(player, search, e -> e instanceof LivingEntity living
			&& living.isAttackable() && living != player && !living.isAlliedTo(player))) {
			Optional<Vec3> hit = entity.getBoundingBox().inflate(0.35d).clip(start, end);
			if (hit.isPresent()) {
				double dist = start.distanceTo(hit.get());
				if (dist <= reach && dist < bestDist) {
					bestDist = dist;
					best = (LivingEntity) entity;
				}
			}
		}
		return best;
	}
}
