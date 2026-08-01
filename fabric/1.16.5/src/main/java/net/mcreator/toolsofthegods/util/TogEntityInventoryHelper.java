package net.mcreator.toolsofthegods.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Consumes upgrade materials from player inventories on Fabric.
 */
public final class TogEntityInventoryHelper {

	private TogEntityInventoryHelper() {
	}

	public static boolean consume(Entity entity, ItemStack template, int count) {
		if (entity == null || template.isEmpty() || count <= 0) {
			return false;
		}
		if (entity instanceof Player) {
		Player player = (Player) entity;
			return consumeFromPlayer(player, template, count);
		}
		return false;
	}

	private static boolean consumeFromPlayer(Player player, ItemStack template, int count) {
		int found = 0;
		for (int i = 0; i < player.inventory.getContainerSize(); i++) {
			ItemStack stack = player.inventory.getItem(i);
			if (ItemStack.matches(stack, template)) {
				found += stack.getCount();
			}
		}
		if (found < count) {
			return false;
		}
		int toRemove = count;
		for (int i = 0; i < player.inventory.getContainerSize() && toRemove > 0; i++) {
			ItemStack stack = player.inventory.getItem(i);
			if (ItemStack.matches(stack, template)) {
				int removed = Math.min(stack.getCount(), toRemove);
				stack.shrink(removed);
				toRemove -= removed;
			}
		}
		return true;
	}
}
