package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class ArmorProgressionHandler {
	private static final EquipmentSlot[] ARMOR_SLOTS = {
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET
	};

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
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

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		float original = event.getAmount();
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
