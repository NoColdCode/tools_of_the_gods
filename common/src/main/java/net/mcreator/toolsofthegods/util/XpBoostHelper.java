package net.mcreator.toolsofthegods.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMobEffects;
import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

public final class XpBoostHelper {
	private XpBoostHelper() {
	}

	public static float getActiveMultiplier(Entity entity) {
		if (!(entity instanceof LivingEntity living)) {
			return 1.0f;
		}
		float best = 1.0f;
		if (has(living, ToolsOfTheGodsModMobEffects.XP_APOTHEOSIS)) {
			best = Math.max(best, 100.0f);
		}
		if (has(living, ToolsOfTheGodsModMobEffects.XP_RAPTURE)) {
			best = Math.max(best, 10.0f);
		}
		if (has(living, ToolsOfTheGodsModMobEffects.XP_SURGE)) {
			best = Math.max(best, 5.0f);
		}
		if (has(living, ToolsOfTheGodsModMobEffects.XP_FOCUS)) {
			best = Math.max(best, 2.0f);
		}
		return best;
	}

	private static boolean has(LivingEntity living, TogRegistryEntry<MobEffect> entry) {
		if (!entry.isBound()) {
			return false;
		}
		MobEffect want = entry.get();
		for (MobEffectInstance inst : living.getActiveEffects()) {
			if (inst.getEffect().value() == want) {
				return true;
			}
		}
		return false;
	}

	/** Apply flat XP to a TOG tool held in the player's offhand. */
	public static boolean applyOffhandXp(Level level, Player player, int amount) {
		ItemStack offhand = player.getOffhandItem();
		if (!ToolProgressionHelper.isTogTool(offhand)) {
			return false;
		}
		if (level.isClientSide()) {
			return true;
		}
		ToolProgressionHelper.gainRawXp(player, offhand, Math.max(1, amount));
		return true;
	}
}
