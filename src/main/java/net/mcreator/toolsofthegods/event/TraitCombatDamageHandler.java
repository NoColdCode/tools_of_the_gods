package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitCombatDamageHandler {

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) {
			return;
		}

		// Only scale direct melee hits from the player (not arrows/projectiles)
		if (event.getSource().getDirectEntity() != player) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(heldItem);
		if (type == ToolProgressionHelper.ToolType.NONE || type == ToolProgressionHelper.ToolType.BOW
			|| type == ToolProgressionHelper.ToolType.CROSSBOW || type == ToolProgressionHelper.ToolType.STAFF
			|| type == ToolProgressionHelper.ToolType.FISHING_ROD || type == ToolProgressionHelper.ToolType.WINGS) {
			return;
		}

		// Item constructors currently provide base attack bonuses directly
		// (hammer: +1, others: +0). Bridge helper values by applying only delta.
		float constructorBase = switch (type) {
			case HAMMER -> 1.0f;
			case TRIDENT, SPEAR -> 0.0f;
			default -> 0.0f;
		};
		float helperBase = ToolProgressionHelper.getBaseWeaponDamage(type);
		float baseDelta = helperBase - constructorBase;
		float progressionBonus = ToolProgressionHelper.getProgressionCombatBonus(heldItem, type);
		float extraDamage = baseDelta + progressionBonus;
		if (extraDamage == 0.0f) {
			return;
		}

		event.setAmount(Math.max(0.0f, event.getAmount() + extraDamage));
	}
}
