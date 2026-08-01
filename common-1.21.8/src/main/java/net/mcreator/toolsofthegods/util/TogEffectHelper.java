package net.mcreator.toolsofthegods.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

/** Keeps passive mob effects topped up without flicker near expiry. */
public final class TogEffectHelper {
	/** Minimum remaining duration (10 seconds) before a passive effect is refreshed. */
	public static final int MIN_REMAINING_TICKS = 220;

	private TogEffectHelper() {
	}

	public static void refreshEffect(Player player, Holder<MobEffect> effect, int amplifier, int fullDurationTicks) {
		MobEffectInstance current = player.getEffect(effect);
		if (current == null || current.getDuration() < MIN_REMAINING_TICKS || current.getAmplifier() != amplifier) {
			player.addEffect(new MobEffectInstance(effect, fullDurationTicks, amplifier, false, false, true));
		}
	}
}
