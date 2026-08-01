package net.mcreator.toolsofthegods.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public final class TraitPoisonHelper {
	private TraitPoisonHelper() {
	}

	public static void applyMeleePoison(ItemStack stack, LivingEntity target) {
		int duration = TraitSystem.getPoisonDurationTicks(stack);
		if (duration <= 0 || target.level().isClientSide()) {
			return;
		}
		target.addEffect(new MobEffectInstance(MobEffects.POISON, duration, 0));
	}

	public static void applyBowPoison(ItemStack bowStack, Level level, LivingEntity shooter) {
		int duration = TraitSystem.getPoisonDurationTicks(bowStack);
		if (duration <= 0 || level.isClientSide()) {
			return;
		}

		List<Arrow> freshArrows = level.getEntitiesOfClass(Arrow.class, shooter.getBoundingBox().inflate(6.0d), arrow -> arrow.getOwner() == shooter && arrow.tickCount <= 1);
		for (Arrow arrow : freshArrows) {
			arrow.addEffect(new MobEffectInstance(MobEffects.POISON, duration, 0));
		}
	}

	public static void applyCrossbowPoison(ItemStack crossbowStack, Level level, LivingEntity shooter) {
		applyBowPoison(crossbowStack, level, shooter);
	}
}