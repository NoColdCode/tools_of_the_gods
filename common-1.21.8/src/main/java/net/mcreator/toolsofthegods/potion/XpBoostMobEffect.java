package net.mcreator.toolsofthegods.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** Beneficial XP boost aura. Multiplier is defined by which effect is active, not amplifier. */
public class XpBoostMobEffect extends MobEffect {
	public XpBoostMobEffect(int color) {
		super(MobEffectCategory.BENEFICIAL, color);
	}
}
