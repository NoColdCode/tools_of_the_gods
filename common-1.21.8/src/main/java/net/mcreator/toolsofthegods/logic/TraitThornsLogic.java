package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;

public final class TraitThornsLogic {

	public static void onPlayerDamaged(TogIncomingDamageContext ctx) {
		if (!(ctx.entity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide() || ctx.amount() <= 0f) {
			return;
		}
		if (!(ctx.source().getEntity() instanceof LivingEntity attacker) || attacker == player) {
			return;
		}

		float thornsDamage = 0f;
		for (ItemStack armor : TogEquipmentHelper.getWornTogArmor(player)) {
			thornsDamage += TraitSystem.getThornsDamage(armor);
		}
		if (thornsDamage <= 0f) {
			return;
		}

		attacker.hurt(player.damageSources().thorns(player), thornsDamage);
	}
}
