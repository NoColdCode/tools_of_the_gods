package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class TogIncomingDamageContext {
	private final LivingEntity entity;
	private final DamageSource source;
	private float amount;

	public TogIncomingDamageContext(LivingEntity entity, DamageSource source, float amount) {
		this.entity = entity;
		this.source = source;
		this.amount = amount;
	}

	public LivingEntity entity() {
		return entity;
	}

	public DamageSource source() {
		return source;
	}

	public float amount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
