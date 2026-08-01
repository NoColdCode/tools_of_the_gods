package net.mcreator.toolsofthegods.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;

/**
 * Progressive wings: cape (0–20) → slow elytra (21–60) → Icarus free-flight (61–100).
 */
public final class WingsFlightLogic {
	public static final int CAPE_MAX_LEVEL = 20;
	public static final int ELYTRA_MAX_LEVEL = 60;

	private static final String NBT_AIR_TICKS = "togWingAirTicks";

	public enum Mode {
		CAPE,
		ELYTRA,
		ICARUS
	}

	private WingsFlightLogic() {
	}

	public static boolean isWings(ItemStack stack) {
		return !stack.isEmpty()
			&& ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.WINGS;
	}

	public static ItemStack getWornWings(LivingEntity entity) {
		ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
		return isWings(chest) ? chest : ItemStack.EMPTY;
	}

	public static int getLevel(ItemStack wings) {
		ToolProgressionHelper.ensureInitialized(wings);
		return (int) wings.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("level", 0.0);
	}

	public static Mode getMode(ItemStack wings) {
		int level = getLevel(wings);
		if (level <= CAPE_MAX_LEVEL) {
			return Mode.CAPE;
		}
		if (level <= ELYTRA_MAX_LEVEL) {
			return Mode.ELYTRA;
		}
		return Mode.ICARUS;
	}

	public static Mode getMode(int level) {
		if (level <= CAPE_MAX_LEVEL) {
			return Mode.CAPE;
		}
		if (level <= ELYTRA_MAX_LEVEL) {
			return Mode.ELYTRA;
		}
		return Mode.ICARUS;
	}

	/** 0.20–1.00 — fall catch / glide quality. */
	public static float getGlide(ItemStack wings) {
		return 0.20f + 0.80f * (getLevel(wings) / 100.0f);
	}

	/** Max continuous air time in seconds before wings tire. */
	public static float getResistanceSeconds(ItemStack wings) {
		int level = getLevel(wings);
		return switch (getMode(level)) {
			case CAPE -> 4.0f + level * 0.20f;           // 4–8s
			case ELYTRA -> 10.0f + (level - 21) * 0.50f; // ~10–30s
			case ICARUS -> 35.0f + (level - 61) * 1.40f; // ~35–90s
		};
	}

	/** 0.25–1.00 — how quickly look direction steers flight. */
	public static float getTurnSpeed(ItemStack wings) {
		return 0.25f + 0.75f * (getLevel(wings) / 100.0f);
	}

	/** Minimum clear air blocks below the player to start powered flight. Lower is better. */
	public static float getFlyHeight(ItemStack wings) {
		int level = getLevel(wings);
		if (level <= CAPE_MAX_LEVEL) {
			return 0.0f;
		}
		if (level <= ELYTRA_MAX_LEVEL) {
			return Mth.lerp((level - 21) / 39.0f, 4.0f, 2.0f);
		}
		return Mth.lerp((level - 61) / 39.0f, 2.0f, 1.0f);
	}

	/** Elytra horizontal speed factor: 0.55 at L21 → 1.0 at L60; 1.05–1.25 in Icarus. */
	public static float getElytraSpeedFactor(ItemStack wings) {
		int level = getLevel(wings);
		if (level <= CAPE_MAX_LEVEL) {
			return 0.0f;
		}
		if (level <= ELYTRA_MAX_LEVEL) {
			return Mth.lerp((level - 21) / 39.0f, 0.55f, 1.0f);
		}
		return 1.05f + (level - 61) / 39.0f * 0.20f;
	}

	/** Fall damage multiplier while wearing wings (before Featherfall trait). */
	public static float getFallDamageMultiplier(ItemStack wings) {
		int level = getLevel(wings);
		return switch (getMode(level)) {
			case CAPE -> Mth.lerp(level / 20.0f, 0.70f, 0.30f);   // 30–70% reduction
			case ELYTRA -> Mth.lerp((level - 21) / 39.0f, 0.45f, 0.25f);
			case ICARUS -> Mth.lerp((level - 61) / 39.0f, 0.25f, 0.10f);
		};
	}

	public static String getModeLabel(ItemStack wings) {
		return switch (getMode(wings)) {
			case CAPE -> "Cape";
			case ELYTRA -> "Elytra";
			case ICARUS -> "Icarus";
		};
	}

	public static boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		if (!entity.getItemBySlot(EquipmentSlot.CHEST).is(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get())) {
			return false;
		}
		if (!isWings(stack)) {
			return false;
		}
		if (getMode(stack) == Mode.CAPE) {
			return false;
		}
		if (!hasStamina(stack)) {
			return false;
		}
		if (entity instanceof Player player && player.isFallFlying()) {
			return true;
		}
		return hasClearance(entity, stack);
	}

	public static boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		if (!(entity instanceof Player player)) {
			return false;
		}
		if (getMode(stack) == Mode.CAPE) {
			return false;
		}
		if (!hasStamina(stack)) {
			return false;
		}

		tickAirTime(stack, player, true);

		if (!player.level().isClientSide() && player.level().getGameTime() % 20 == 0) {
			int xp = Math.max(1, Math.round(TraitSystem.getAerodynamicGlideXpMultiplier(stack)));
			ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, stack, xp);
		}

		float speed = getElytraSpeedFactor(stack);
		float turn = getTurnSpeed(stack);
		Vec3 look = player.getLookAngle();
		Vec3 motion = player.getDeltaMovement();

		// Softly steer toward look direction and scale speed vs vanilla elytra feel.
		double mix = 0.04d + turn * 0.10d;
		Vec3 steered = new Vec3(
			Mth.lerp(mix, motion.x, look.x * motion.horizontalDistance()),
			motion.y,
			Mth.lerp(mix, motion.z, look.z * motion.horizontalDistance())
		);
		if (speed < 0.999f || speed > 1.001f) {
			steered = new Vec3(steered.x * speed, steered.y, steered.z * speed);
		}

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.AERODYNAMIC_I)
			|| TraitSystem.hasTrait(stack, TraitSystem.Trait.AERODYNAMIC_II)) {
			if (steered.y < -0.1d) {
				steered = new Vec3(steered.x * 1.02d, steered.y * 0.98d, steered.z * 1.02d);
			}
		}

		if (getMode(stack) == Mode.ICARUS) {
			steered = applyIcarusThrust(player, stack, steered);
		}

		player.setDeltaMovement(steered);
		return true;
	}

	/** Cape slow-fall + glider sync + Icarus; call every player tick while wings worn. */
	public static void onPlayerTick(Player player) {
		ItemStack wings = getWornWings(player);
		if (wings.isEmpty()) {
			return;
		}

		boolean airborne = !player.onGround() && !player.isInWater() && !player.isPassenger();
		tickAirTime(wings, player, airborne);
		syncGliderComponent(player, wings);

		Mode mode = getMode(wings);
		if (mode == Mode.CAPE) {
			applyCape(player, wings);
			return;
		}

		// 1.21.8+ uses the glider data component; apply flight modifiers here (no IItemExtension hooks).
		if (player.isFallFlying()) {
			elytraFlightTick(wings, player, player.getFallFlyingTicks());
			return;
		}

		if (mode == Mode.ICARUS && airborne && hasStamina(wings)) {
			if (player.getXRot() < -25.0f && hasClearance(player, wings)) {
				Vec3 motion = player.getDeltaMovement();
				player.setDeltaMovement(applyIcarusThrust(player, wings, motion));
				player.fallDistance = Math.min(player.fallDistance, 2.0f);
			}
		}
	}

	/** Enable/disable vanilla glider component based on level, stamina, and clearance. */
	private static void syncGliderComponent(Player player, ItemStack wings) {
		boolean want = getMode(wings) != Mode.CAPE
			&& hasStamina(wings)
			&& (player.isFallFlying() || hasClearance(player, wings));
		if (want) {
			if (!wings.has(DataComponents.GLIDER)) {
				wings.set(DataComponents.GLIDER, Unit.INSTANCE);
			}
		} else if (wings.has(DataComponents.GLIDER)) {
			wings.remove(DataComponents.GLIDER);
		}
	}

	private static void applyCape(Player player, ItemStack wings) {
		if (player.onGround() || player.isInWater() || player.getAbilities().flying) {
			return;
		}
		Vec3 motion = player.getDeltaMovement();
		if (motion.y >= -0.05d) {
			return;
		}
		float glide = getGlide(wings);
		// Pull fall speed toward a soft terminal velocity.
		double targetFall = -0.08d - (1.0d - glide) * 0.35d;
		double newY = motion.y;
		if (newY < targetFall) {
			newY = Mth.lerp(0.25d + glide * 0.35d, newY, targetFall);
		}
		double drift = 0.015d + glide * 0.03d;
		player.setDeltaMovement(motion.x * (1.0d + drift), newY, motion.z * (1.0d + drift));
		player.fallDistance *= (1.0f - glide * 0.04f);

		if (!player.level().isClientSide() && player.level().getGameTime() % 40 == 0) {
			int xp = Math.max(1, Math.round(0.5f * TraitSystem.getAerodynamicGlideXpMultiplier(wings)));
			ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, wings, xp);
		}
	}

	private static Vec3 applyIcarusThrust(Player player, ItemStack wings, Vec3 motion) {
		float pitch = player.getXRot();
		float turn = getTurnSpeed(wings);
		float power = 0.045f + (getLevel(wings) - 61) / 39.0f * 0.055f;
		Vec3 look = player.getLookAngle();

		if (pitch < -12.0f) {
			// Look up: climb
			double lift = power * ((-pitch - 12.0f) / 78.0f);
			motion = motion.add(look.x * power * 0.35d * turn, lift, look.z * power * 0.35d * turn);
			if (motion.y > 0.85d) {
				motion = new Vec3(motion.x, 0.85d, motion.z);
			}
		} else if (pitch > 35.0f) {
			// Look down: dive accelerate
			motion = motion.add(look.x * power * 0.2d, -power * 0.15d, look.z * power * 0.2d);
		} else {
			// Level flight sustain
			motion = motion.add(look.x * power * 0.25d * turn, Math.max(0.0d, -motion.y) * 0.08d, look.z * power * 0.25d * turn);
		}
		return motion;
	}

	private static boolean hasClearance(LivingEntity entity, ItemStack wings) {
		float need = getFlyHeight(wings);
		if (need <= 0.01f) {
			return true;
		}
		Level level = entity.level();
		BlockPos.MutableBlockPos cursor = entity.blockPosition().mutable();
		int cleared = 0;
		int maxCheck = Math.min(8, Mth.ceil(need) + 1);
		for (int i = 1; i <= maxCheck; i++) {
			cursor.set(entity.getBlockX(), entity.getBlockY() - i, entity.getBlockZ());
			if (level.getBlockState(cursor).isAir()) {
				cleared++;
			} else {
				break;
			}
		}
		return cleared >= need;
	}

	private static boolean hasStamina(ItemStack wings) {
		int air = getAirTicks(wings);
		return air < getResistanceSeconds(wings) * 20.0f;
	}

	private static int getAirTicks(ItemStack wings) {
		return wings.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBT_AIR_TICKS, 0);
	}

	private static void tickAirTime(ItemStack wings, Player player, boolean airborne) {
		if (player.level().isClientSide()) {
			return;
		}
		if (!airborne || player.onGround()) {
			if (getAirTicks(wings) != 0) {
				CustomData.update(DataComponents.CUSTOM_DATA, wings, tag -> tag.putInt(NBT_AIR_TICKS, 0));
			}
			return;
		}
		int next = getAirTicks(wings) + 1;
		CustomData.update(DataComponents.CUSTOM_DATA, wings, tag -> tag.putInt(NBT_AIR_TICKS, next));
	}

	public static float getStaminaRatio(ItemStack wings) {
		float max = getResistanceSeconds(wings) * 20.0f;
		if (max <= 0.0f) {
			return 1.0f;
		}
		return Mth.clamp(1.0f - getAirTicks(wings) / max, 0.0f, 1.0f);
	}
}
