package net.mcreator.toolsofthegods.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public final class TraitFreezyHelper {
	private TraitFreezyHelper() {
	}

	public static void applyMeleeFreeze(ItemStack stack, LivingEntity target) {
		int duration = TraitSystem.getFreezeDurationTicks(stack);
		if (duration <= 0 || target.level().isClientSide()) {
			return;
		}
		int amplifier = TraitSystem.getFreezeAmplifier(stack);
		target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, amplifier));
	}

	public static void applyBowFreeze(ItemStack bowStack, Level level, LivingEntity shooter) {
		int duration = TraitSystem.getFreezeDurationTicks(bowStack);
		if (duration <= 0 || level.isClientSide()) {
			return;
		}
		int amplifier = TraitSystem.getFreezeAmplifier(bowStack);

		List<Arrow> freshArrows = level.getEntitiesOfClass(Arrow.class,
			shooter.getBoundingBox().inflate(6.0d),
			arrow -> arrow.getOwner() == shooter && arrow.tickCount <= 1);
		for (Arrow arrow : freshArrows) {
			arrow.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, amplifier));
		}
	}

	public static void applyCrossbowFreeze(ItemStack crossbowStack, Level level, LivingEntity shooter) {
		applyBowFreeze(crossbowStack, level, shooter);
	}
}
