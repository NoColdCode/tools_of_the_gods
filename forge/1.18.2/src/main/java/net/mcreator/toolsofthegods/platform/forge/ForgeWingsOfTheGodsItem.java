package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.common.extensions.IForgeItem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.item.WingsOfTheGodsItem;
import net.mcreator.toolsofthegods.logic.WingsElytraLogic;

/** Forge {@link IForgeItem} bridge for wings elytra flight. */
public final class ForgeWingsOfTheGodsItem extends WingsOfTheGodsItem implements IForgeItem {
	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return WingsElytraLogic.canElytraFly(stack, entity);
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		return WingsElytraLogic.elytraFlightTick(stack, entity, flightTicks);
	}
}
