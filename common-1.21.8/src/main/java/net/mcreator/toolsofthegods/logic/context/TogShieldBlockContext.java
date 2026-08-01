package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class TogShieldBlockContext {
	private final LivingEntity entity;
	private final DamageSource damageSource;
	private final boolean originalBlock;
	private final float originalBlockedDamage;
	private boolean blocked;
	private float blockedDamage;
	private float shieldDamage;

	public TogShieldBlockContext(LivingEntity entity, DamageSource damageSource, boolean originalBlock, float originalBlockedDamage) {
		this.entity = entity;
		this.damageSource = damageSource;
		this.originalBlock = originalBlock;
		this.originalBlockedDamage = originalBlockedDamage;
		this.blocked = originalBlock;
		this.blockedDamage = originalBlockedDamage;
		this.shieldDamage = 0f;
	}

	public LivingEntity entity() {
		return entity;
	}

	public DamageSource damageSource() {
		return damageSource;
	}

	public boolean originalBlock() {
		return originalBlock;
	}

	public float originalBlockedDamage() {
		return originalBlockedDamage;
	}

	public boolean blocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public float blockedDamage() {
		return blockedDamage;
	}

	public void setBlockedDamage(float blockedDamage) {
		this.blockedDamage = blockedDamage;
	}

	public float shieldDamage() {
		return shieldDamage;
	}

	public void setShieldDamage(float shieldDamage) {
		this.shieldDamage = shieldDamage;
	}
}
