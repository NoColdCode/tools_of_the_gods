package net.mcreator.toolsofthegods.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;

public final class StaffSpellCaster {
	private StaffSpellCaster() {
	}

	public static void cast(Level world, Player player, ItemStack stack, StaffSpell spell) {
		if (world.isClientSide()) {
			return;
		}
		switch (spell) {
			case DIVINE_BOLT -> castDivineBolt(world, player);
			case FROST_SHARD -> castFrostShard(world, player);
			case ARCANE_BURST -> castArcaneBurst(world, player);
			case HEALING_PULSE -> castHealingPulse(player, stack);
			case LIGHTNING_STRIKE -> castLightningStrike(world, player);
			case KNOCKBACK_WAVE -> castKnockbackWave(world, player, stack);
			case POISON_MIST -> castPoisonMist(world, player);
			case HOLY_BEAM -> castHolyBeam(world, player, stack);
			case METEOR -> castMeteor(world, player);
			case SOUL_DRAIN -> castSoulDrain(world, player);
			case SHIELD_WARD -> castShieldWard(player, stack);
			case VOID_SNARE -> castVoidSnare(world, player);
		}
	}

	private static void castDivineBolt(Level world, Player player) {
		Vec3 look = player.getLookAngle();
		SmallFireball bolt = new SmallFireball(world, player, look.scale(0.25));
		bolt.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
		world.addFreshEntity(bolt);
		world.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.7f, 1.2f);
	}

	private static void castFrostShard(Level world, Player player) {
		Snowball snowball = new Snowball(world, player);
		snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.6f, 0.5f);
		world.addFreshEntity(snowball);
		LivingEntity target = raycastLiving(player, 14.0d);
		if (target != null) {
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
		}
		world.playSound(null, player.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.6f, 1.1f);
	}

	private static void castArcaneBurst(Level world, Player player) {
		Vec3 look = player.getLookAngle();
		for (int i = -2; i <= 2; i++) {
			double angle = i * 0.12d;
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			Vec3 spread = new Vec3(look.x * cos - look.z * sin, look.y, look.x * sin + look.z * cos);
			SmallFireball bolt = new SmallFireball(world, player, spread.scale(0.2));
			bolt.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
			world.addFreshEntity(bolt);
		}
		world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.5f, 1.4f);
	}

	private static void castHealingPulse(Player player, ItemStack stack) {
		float heal = 4.0f + StaffSpellHelper.getStaffLevel(stack) * 0.08f;
		player.heal(heal);
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
		player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.4f, 1.6f);
		spawnHealingParticles(player);
	}

	private static void castLightningStrike(Level world, Player player) {
		if (!(world instanceof ServerLevel serverLevel)) {
			return;
		}
		HitResult hit = player.pick(32.0d, 1.0f, false);
		if (hit.getType() == HitResult.Type.MISS) {
			return;
		}
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
		if (bolt == null) {
			return;
		}
		Vec3 pos = hit.getLocation();
		bolt.moveTo(pos.x, pos.y, pos.z);
		serverLevel.addFreshEntity(bolt);
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER,
			SoundSource.PLAYERS, 0.8f, 1.0f);
	}

	private static void castKnockbackWave(Level world, Player player, ItemStack stack) {
		double radius = 4.5d + StaffSpellHelper.getStaffLevel(stack) * 0.02d;
		AABB box = player.getBoundingBox().inflate(radius);
		float damage = 3.0f + StaffSpellHelper.getStaffLevel(stack) * 0.05f;
		for (LivingEntity living : world.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
			Vec3 push = living.position().subtract(player.position()).normalize().scale(1.2d).add(0.0d, 0.35d, 0.0d);
			living.push(push);
			living.hurt(player.damageSources().magic(), damage);
		}
		spawnKnockbackWaveParticles(world, player, radius);
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK,
			SoundSource.PLAYERS, 0.8f, 0.6f);
	}

	private static void castPoisonMist(Level world, Player player) {
		LivingEntity target = raycastLiving(player, 10.0d);
		if (target != null) {
			target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
			target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
			spawnPoisonMistParticles(world, target);
		} else {
			spawnPoisonMistParticles(world, player);
		}
		world.playSound(null, player.blockPosition(), SoundEvents.WITCH_THROW, SoundSource.PLAYERS, 0.6f, 0.9f);
	}

	private static void castHolyBeam(Level world, Player player, ItemStack stack) {
		Arrow arrow = new Arrow(world, player, stack, stack);
		arrow.setBaseDamage(5.0d + StaffSpellHelper.getStaffLevel(stack) * 0.06d);
		arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 3.5f, 0.0f);
		if (arrow instanceof AbstractArrow abstractArrow) {
			abstractArrow.setCritArrow(true);
		}
		world.addFreshEntity(arrow);
		world.playSound(null, player.blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.5f, 1.8f);
	}

	private static void castMeteor(Level world, Player player) {
		Vec3 look = player.getLookAngle().scale(8.0d);
		Vec3 target = player.getEyePosition().add(look);
		LargeFireball meteor = new LargeFireball(world, player, look.normalize().scale(0.5), 2);
		meteor.setPos(target.x, target.y + 6.0d, target.z);
		world.addFreshEntity(meteor);
		world.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.7f, 0.7f);
	}

	private static void castSoulDrain(Level world, Player player) {
		LivingEntity target = raycastLiving(player, 16.0d);
		if (target != null) {
			target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
			player.heal(2.0f);
		}
		world.playSound(null, player.blockPosition(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, 1.1f);
	}

	private static void castShieldWard(Player player, ItemStack stack) {
		int amp = StaffSpellHelper.getStaffLevel(stack) >= 50 ? 1 : 0;
		player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, amp));
		player.level().playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.5f, 1.3f);
	}

	private static void castVoidSnare(Level world, Player player) {
		LivingEntity target = raycastLiving(player, 18.0d);
		if (target != null) {
			target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0));
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
		}
		world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.6f, 0.6f);
	}

	private static LivingEntity raycastLiving(Player player, double range) {
		HitResult hit = player.pick(range, 1.0f, false);
		if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living
			&& living.isAlive() && living != player) {
			return living;
		}
		BlockPos pos = BlockPos.containing(hit.getLocation());
		AABB box = new AABB(pos).inflate(1.5d);
		LivingEntity nearest = null;
		double best = range * range;
		for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
			double dist = player.distanceToSqr(living);
			if (dist < best) {
				best = dist;
				nearest = living;
			}
		}
		return nearest;
	}

	private static void spawnHealingParticles(Player player) {
		if (!(player.level() instanceof ServerLevel level)) {
			return;
		}
		double x = player.getX();
		double y = player.getY() + player.getBbHeight() * 0.5d;
		double z = player.getZ();
		burst(level, ParticleTypes.HEART, x, y, z, 12, 0.45d);
		burst(level, ParticleTypes.HAPPY_VILLAGER, x, y, z, 20, 0.55d);
		burst(level, ParticleTypes.END_ROD, x, y + 0.2d, z, 8, 0.25d);
	}

	private static void spawnKnockbackWaveParticles(Level world, Player player, double radius) {
		if (!(world instanceof ServerLevel level)) {
			return;
		}
		double cx = player.getX();
		double cy = player.getY() + 0.15d;
		double cz = player.getZ();
		for (int i = 0; i < 24; i++) {
			double angle = (Math.PI * 2.0d * i) / 24.0d;
			double px = cx + Math.cos(angle) * radius * 0.85d;
			double pz = cz + Math.sin(angle) * radius * 0.85d;
			level.sendParticles(ParticleTypes.CLOUD, px, cy, pz, 2, 0.05d, 0.08d, 0.05d, 0.02d);
			level.sendParticles(ParticleTypes.SWEEP_ATTACK, px, cy + 0.4d, pz, 1, 0.0d, 0.0d, 0.0d, 0.0d);
		}
		burst(level, ParticleTypes.EXPLOSION, cx, cy + 0.5d, cz, 1, 0.0d);
	}

	private static void spawnPoisonMistParticles(Level world, LivingEntity center) {
		if (!(world instanceof ServerLevel level)) {
			return;
		}
		double x = center.getX();
		double y = center.getY() + center.getBbHeight() * 0.5d;
		double z = center.getZ();
		burst(level, ParticleTypes.ITEM_SLIME, x, y, z, 24, 0.45d);
		burst(level, ParticleTypes.SPORE_BLOSSOM_AIR, x, y + 0.2d, z, 16, 0.4d);
		burst(level, ParticleTypes.WITCH, x, y, z, 10, 0.35d);
	}

	private static void burst(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double spread) {
		level.sendParticles(particle, x, y, z, count, spread, spread, spread, 0.02d);
	}
}
