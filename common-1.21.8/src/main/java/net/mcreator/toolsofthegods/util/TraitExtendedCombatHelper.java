package net.mcreator.toolsofthegods.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public final class TraitExtendedCombatHelper {
	private TraitExtendedCombatHelper() {
	}

	public static void applyMeleeEffects(ItemStack stack, LivingEntity target) {
		if (target.level().isClientSide()) {
			return;
		}
		applyHemorrhage(stack, target);
		applySearingMelee(stack, target);
		applyRime(stack, target);
	}

	public static void applyBowEffects(ItemStack stack, Level level, LivingEntity shooter) {
		if (level.isClientSide()) {
			return;
		}
		applySearingArrows(stack, level, shooter);
		TraitVolleyHelper.tryVolley(stack, level, shooter);
	}

	public static void applyCrossbowEffects(ItemStack stack, Level level, LivingEntity shooter) {
		applyBowEffects(stack, level, shooter);
	}

	public static void applyOnKill(ItemStack stack, Player player) {
		float heal = TraitSystem.getVitalityHealAmount(stack);
		if (heal <= 0f || player.level().isClientSide()) {
			return;
		}
		player.heal(heal);
	}

	private static void applyHemorrhage(ItemStack stack, LivingEntity target) {
		int duration = TraitSystem.getHemorrhageDurationTicks(stack);
		if (duration <= 0) {
			return;
		}
		target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, 0));
	}

	private static void applySearingMelee(ItemStack stack, LivingEntity target) {
		int fireSeconds = TraitSystem.getSearingFireSeconds(stack);
		if (fireSeconds <= 0) {
			return;
		}
		target.igniteForSeconds(fireSeconds);
	}

	private static void applyRime(ItemStack stack, LivingEntity target) {
		int amplifier = TraitSystem.getRimeSlownessAmplifier(stack);
		if (amplifier < 0) {
			return;
		}
		int duration = amplifier == 1 ? 100 : 80;
		target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, amplifier));
	}

	private static void applySearingArrows(ItemStack stack, Level level, LivingEntity shooter) {
		int fireSeconds = TraitSystem.getSearingFireSeconds(stack);
		if (fireSeconds <= 0) {
			return;
		}
		int fireTicks = fireSeconds * 20;
		List<Arrow> freshArrows = level.getEntitiesOfClass(Arrow.class,
			shooter.getBoundingBox().inflate(6.0d),
			arrow -> arrow.getOwner() == shooter && arrow.tickCount <= 1);
		for (Arrow arrow : freshArrows) {
			arrow.setRemainingFireTicks(Math.max(arrow.getRemainingFireTicks(), fireTicks));
		}
	}
}
