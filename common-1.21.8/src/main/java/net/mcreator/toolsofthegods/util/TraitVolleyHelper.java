package net.mcreator.toolsofthegods.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class TraitVolleyHelper {
	private TraitVolleyHelper() {
	}

	public static void tryVolley(ItemStack stack, Level level, LivingEntity shooter) {
		float chance = TraitSystem.getVolleyChance(stack);
		if (chance <= 0f || shooter.getRandom().nextFloat() >= chance) {
			return;
		}

		List<Arrow> freshArrows = level.getEntitiesOfClass(Arrow.class,
			shooter.getBoundingBox().inflate(8.0d),
			arrow -> arrow.getOwner() == shooter && arrow.tickCount <= 2 && arrow.isAlive());
		if (freshArrows.isEmpty()) {
			return;
		}

		Arrow original = freshArrows.getFirst();
		Arrow extra = new Arrow(level, shooter, new ItemStack(Items.ARROW), stack);
		extra.setPos(original.getX(), original.getY(), original.getZ());
		Vec3 motion = original.getDeltaMovement();
		float speed = (float) motion.length();
		if (speed < 0.01f) {
			return;
		}
		extra.shoot(motion.x, motion.y, motion.z, speed * 0.92f, 2.0f);
		extra.setBaseDamage(2.0 * 0.75d);
		extra.setCritArrow(original.isCritArrow());
		extra.setRemainingFireTicks(original.getRemainingFireTicks());
		level.addFreshEntity(extra);
	}
}
