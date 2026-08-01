package net.mcreator.toolsofthegods.logic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TraitSystem;

import java.util.List;

public final class TraitRepulseLogic {
	private static final double RANGE = 5.0d;
	private static final double KNOCKBACK = 1.4d;
	private static final int COOLDOWN_TICKS = 30;

	private TraitRepulseLogic() {
	}

	public static void tryRepulse(Player player, ItemStack shield) {
		if (shield.isEmpty() || shield.getItem() != ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return;
		}
		if (!TraitSystem.hasTrait(shield, TraitSystem.Trait.REPULSE_I)) {
			return;
		}
		Level level = player.level();
		if (level.isClientSide()) {
			return;
		}

		long now = level.getGameTime();
		long cooldownEnd = (long) shield.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
			net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getDoubleOr("togRepulseCooldown", 0.0);
		if (now < cooldownEnd) {
			return;
		}

		Vec3 look = player.getViewVector(1f).normalize();
		AABB box = player.getBoundingBox().inflate(RANGE);
		List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box,
			e -> e != player && e.isAlive() && player.hasLineOfSight(e));

		boolean hit = false;
		for (LivingEntity target : targets) {
			Vec3 toTarget = target.position().subtract(player.position()).normalize();
			if (look.dot(toTarget) < 0.35d) {
				continue;
			}
			Vec3 knock = look.scale(KNOCKBACK).add(0, 0.25, 0);
			target.knockback(knock.length() * 0.5, -knock.x, -knock.z);
			target.hurtMarked = true;
			hit = true;
		}

		if (hit) {
			net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, shield,
				tag -> tag.putDouble("togRepulseCooldown", now + COOLDOWN_TICKS));
			level.playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.SHIELD_BLOCK.value(),
				net.minecraft.sounds.SoundSource.PLAYERS, 0.9f, 0.7f);
		}
	}
}
