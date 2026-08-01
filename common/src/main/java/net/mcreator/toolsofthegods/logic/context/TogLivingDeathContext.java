package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record TogLivingDeathContext(
	LivingEntity entity,
	DamageSource source
) {
}
