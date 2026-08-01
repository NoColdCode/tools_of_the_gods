package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.entity.LivingEntity;

public record TogLivingDamagePostContext(
	LivingEntity entity,
	float newDamage,
	float blockedDamage
) {
}
