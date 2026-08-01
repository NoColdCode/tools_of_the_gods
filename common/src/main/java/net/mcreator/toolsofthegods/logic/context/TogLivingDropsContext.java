package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.List;

public record TogLivingDropsContext(
	LivingEntity entity,
	DamageSource source,
	List<ItemEntity> drops
) {
}
