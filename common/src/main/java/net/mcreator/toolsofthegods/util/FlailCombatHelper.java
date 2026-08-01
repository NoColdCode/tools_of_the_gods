package net.mcreator.toolsofthegods.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Flail scaling: slower attack speed (capped), higher damage, longer stun with level.
 */
public final class FlailCombatHelper {

	/** Attack-speed stat cannot exceed this (flail never swings faster than ~1.5). */
	private static final float ATTACK_SPEED_STAT_CAP = 1.5f;

	private static final float STUN_SECONDS_MAX = 10.0f;
	private static final float BOSS_STUN_MULTIPLIER = 0.5f;

	private FlailCombatHelper() {
	}

	public static int getLevel(ItemStack stack) {
		return (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level");
	}

	/**
	 * ADD_VALUE modifier for attack speed. More negative = slower with level; displayed speed capped at 1.5 max.
	 */
	public static double getAttackSpeedModifier(int level) {
		float progress = Math.min(1.0f, level / 100.0f);
		return -3.5d - progress * 0.85d;
	}

	public static float getDisplayedAttackSpeed(int level) {
		float stat = (float) (4.0d + getAttackSpeedModifier(level));
		return Math.min(stat, ATTACK_SPEED_STAT_CAP);
	}

	public static float getStunSecondsForDisplay(int level) {
		return Math.min(STUN_SECONDS_MAX, 0.35f + level * 0.0965f);
	}

	public static float getStunSeconds(int level, LivingEntity target) {
		float seconds = getStunSecondsForDisplay(level);
		if (target != null && isBoss(target)) {
			seconds *= BOSS_STUN_MULTIPLIER;
		}
		return seconds;
	}

	public static boolean isBoss(LivingEntity target) {
		return target instanceof WitherBoss || target instanceof EnderDragon || target instanceof Warden
			|| target.getMaxHealth() >= 150.0f;
	}

	public static void applyStun(LivingEntity target, int level) {
		int ticks = Math.max(10, (int) (getStunSeconds(level, target) * 20.0f));
		target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ticks, 6, false, true, true));
		target.addEffect(new MobEffectInstance(MobEffects.JUMP, ticks, 200, false, true, true));
		target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, ticks, 0, false, false, true));
	}
}
