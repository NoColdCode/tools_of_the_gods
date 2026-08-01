package net.mcreator.toolsofthegods.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks shield strain while raised. Strain builds from blocked damage and only decays after the shield stays lowered for a while.
 * Rapid raise/lower spam adds a penalty instead of resetting strain.
 */
public final class ShieldStrainHelper {
	private static final int DECAY_DELAY_TICKS = 60;
	private static final int RAPID_TOGGLE_TICKS = 15;
	private static final float RAPID_TOGGLE_PENALTY = 4f;

	private static final Map<UUID, Float> strain = new ConcurrentHashMap<>();
	private static final Map<UUID, Long> strainedUntilTick = new ConcurrentHashMap<>();
	private static final Map<UUID, Boolean> wasBlocking = new ConcurrentHashMap<>();
	private static final Map<UUID, Long> decayAllowedAfter = new ConcurrentHashMap<>();
	private static final Map<UUID, Long> lastReleaseTick = new ConcurrentHashMap<>();

	private ShieldStrainHelper() {
	}

	public static float getStrainCapacity(ItemStack stack) {
		int level = Math.max(0, Math.min(TierSystem.MAX_LEVEL, ToolProgressionHelper.getShieldLevel(stack)));
		return 4f + (level / 100f) * 46f;
	}

	public static int getStrainRecoveryTicks(int level) {
		int clamped = Math.max(0, Math.min(TierSystem.MAX_LEVEL, level));
		return (int) (120 - (clamped / 100f) * 60);
	}

	/** Damage returned to the attacker on a successful block (0–0.15). */
	public static float getReflectRatio(ItemStack stack) {
		int level = Math.max(0, Math.min(TierSystem.MAX_LEVEL, ToolProgressionHelper.getShieldLevel(stack)));
		if (level <= 0) {
			return 0f;
		}
		if (level <= 50) {
			return (level / 50f) * 0.01f;
		}
		return 0.01f + ((level - 50) / 50f) * 0.14f;
	}

	public static boolean isStrained(Player player) {
		return player.level().getGameTime() < strainedUntilTick.getOrDefault(player.getUUID(), 0L);
	}

	public static float getStrain(Player player) {
		return strain.getOrDefault(player.getUUID(), 0f);
	}

	public static void tick(Player player) {
		UUID id = player.getUUID();
		long now = player.level().getGameTime();
		boolean blocking = player.isBlocking() && isRaisingTogShield(player);

		boolean prev = wasBlocking.getOrDefault(id, false);
		if (blocking) {
			if (!prev) {
				Long releasedAt = lastReleaseTick.get(id);
				if (releasedAt != null && now - releasedAt <= RAPID_TOGGLE_TICKS) {
					strain.merge(id, RAPID_TOGGLE_PENALTY, Float::sum);
					tryTriggerStrain(player, getTogShield(player));
				}
			}
			decayAllowedAfter.remove(id);
			wasBlocking.put(id, true);
			return;
		}

		if (prev) {
			lastReleaseTick.put(id, now);
			if (getStrain(player) > 0f) {
				decayAllowedAfter.put(id, now + DECAY_DELAY_TICKS);
			}
		}
		wasBlocking.put(id, false);

		float current = strain.getOrDefault(id, 0f);
		if (current <= 0f) {
			return;
		}
		long allowedAfter = decayAllowedAfter.getOrDefault(id, now);
		if (now < allowedAfter) {
			return;
		}
		strain.put(id, Math.max(0f, current - 0.25f));
	}

	/**
	 * Adds strain from damage negated by a successful block.
	 *
	 * @return true when blocking should still apply for this hit
	 */
	public static boolean addStrainFromBlock(Player player, ItemStack shield, float blockedDamage) {
		if (isStrained(player)) {
			return false;
		}
		if (blockedDamage <= 0f) {
			return true;
		}
		strain.merge(player.getUUID(), blockedDamage * TraitSystem.getStrainGainMultiplier(shield), Float::sum);
		return tryTriggerStrain(player, shield);
	}

	private static boolean tryTriggerStrain(Player player, ItemStack shield) {
		if (shield.isEmpty()) {
			return true;
		}
		int level = shieldLevel(shield);
		float cap = getStrainCapacity(shield);
		float current = getStrain(player);
		if (current < cap) {
			return true;
		}
		strain.put(player.getUUID(), 0f);
		int recovery = getStrainRecoveryTicks(level);
		strainedUntilTick.put(player.getUUID(), player.level().getGameTime() + recovery);
		player.sendOverlayMessage(Component.literal("§c§lShield strained! §7(" + (recovery / 20) + "s)"));
		return true;
	}

	public static void clear(Player player) {
		UUID id = player.getUUID();
		strain.remove(id);
		strainedUntilTick.remove(id);
		wasBlocking.remove(id);
		decayAllowedAfter.remove(id);
		lastReleaseTick.remove(id);
	}

	private static boolean isRaisingTogShield(Player player) {
		return !getTogShield(player).isEmpty();
	}

	public static ItemStack getTogShield(Player player) {
		if (!player.isBlocking()) {
			return ItemStack.EMPTY;
		}
		ItemStack raised = player.getUseItem();
		if (!raised.isEmpty() && raised.getItem() == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return raised;
		}
		ItemStack offhand = player.getOffhandItem();
		if (!offhand.isEmpty() && offhand.getItem() == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return offhand;
		}
		return ItemStack.EMPTY;
	}

	private static int shieldLevel(ItemStack stack) {
		return ToolProgressionHelper.getShieldLevel(stack);
	}
}
