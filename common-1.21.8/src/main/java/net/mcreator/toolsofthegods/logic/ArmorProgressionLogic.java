package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public final class ArmorProgressionLogic {
	private static final EquipmentSlot[] ARMOR_SLOTS = {
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET
	};

	public static void onPlayerTick(Player player) {
		if (player.level().isClientSide()) {
			return;
		}
		int wearInterval = ToolsOfTheGodsCommonConfig.ARMOR_WEAR_XP_INTERVAL.get();
		if (player.tickCount % wearInterval != 0) {
			return;
		}

		for (EquipmentSlot slot : ARMOR_SLOTS) {
			ItemStack armor = player.getItemBySlot(slot);
			if (ToolProgressionHelper.getToolType(armor) == ToolProgressionHelper.ToolType.ARMOR) {
				ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, armor, 1);
			}
		}
	}

	public static void onIncomingDamage(TogIncomingDamageContext ctx) {
		if (!(ctx.entity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		float original = ctx.amount();
		if (original <= 0f) {
			return;
		}

		for (EquipmentSlot slot : ARMOR_SLOTS) {
			ItemStack armor = player.getItemBySlot(slot);
			if (ToolProgressionHelper.getToolType(armor) != ToolProgressionHelper.ToolType.ARMOR) {
				continue;
			}
			int damageXp = Math.max(1, (int) Math.ceil(original));
			ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, armor, damageXp);
		}
	}
}
