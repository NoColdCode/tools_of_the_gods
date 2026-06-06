package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitThornsHandler {

	@SubscribeEvent
	public static void onPlayerDamaged(LivingIncomingDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide() || event.getAmount() <= 0f) {
			return;
		}
		if (!(event.getSource().getEntity() instanceof LivingEntity attacker) || attacker == player) {
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
