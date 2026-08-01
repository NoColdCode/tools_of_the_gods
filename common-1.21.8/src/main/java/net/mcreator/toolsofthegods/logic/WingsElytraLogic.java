package net.mcreator.toolsofthegods.logic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Loader-neutral elytra flight rules for Wings of the Gods. */
public final class WingsElytraLogic {
	private WingsElytraLogic() {
	}

	public static boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return WingsFlightLogic.canElytraFly(stack, entity);
	}

	public static boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		return WingsFlightLogic.elytraFlightTick(stack, entity, flightTicks);
	}
}
