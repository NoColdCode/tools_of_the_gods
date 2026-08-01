package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.common.extensions.IItemExtension;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.item.WingsOfTheGodsItem;
import net.mcreator.toolsofthegods.logic.WingsElytraLogic;

/** NeoForge {@link IItemExtension} bridge for wings elytra flight. */
public final class ForgeWingsOfTheGodsItem extends WingsOfTheGodsItem implements IItemExtension {
	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return WingsElytraLogic.canElytraFly(stack, entity);
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		return WingsElytraLogic.elytraFlightTick(stack, entity, flightTicks);
	}
}
