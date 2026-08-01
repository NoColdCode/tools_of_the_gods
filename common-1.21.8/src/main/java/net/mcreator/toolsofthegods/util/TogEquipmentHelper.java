package net.mcreator.toolsofthegods.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import java.util.ArrayList;
import java.util.List;

public final class TogEquipmentHelper {
	private static final EquipmentSlot[] ARMOR_SLOTS = {
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET
	};

	private TogEquipmentHelper() {
	}

	public static List<ItemStack> getWornTogArmor(Player player) {
		List<ItemStack> armor = new ArrayList<>();
		for (EquipmentSlot slot : ARMOR_SLOTS) {
			ItemStack stack = player.getItemBySlot(slot);
			if (ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.ARMOR) {
				armor.add(stack);
			}
		}
		return armor;
	}

	public static ItemStack getTogShield(Player player) {
		ItemStack offhand = player.getOffhandItem();
		if (!offhand.isEmpty() && offhand.getItem() == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return offhand;
		}
		if (player.isBlocking()) {
			ItemStack active = player.getUseItem();
			if (!active.isEmpty() && active.getItem() == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
				return active;
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getTogWings(Player player) {
		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		if (ToolProgressionHelper.getToolType(chest) == ToolProgressionHelper.ToolType.WINGS) {
			return chest;
		}
		return ItemStack.EMPTY;
	}

	public static List<ItemStack> getPassiveTraitSources(Player player) {
		List<ItemStack> sources = new ArrayList<>(getWornTogArmor(player));
		ItemStack wings = getTogWings(player);
		if (!wings.isEmpty()) {
			sources.add(wings);
		}
		ItemStack shield = getTogShield(player);
		if (!shield.isEmpty()) {
			sources.add(shield);
		}
		return sources;
	}

	public static List<ItemStack> getDefenseTraitSources(Player player) {
		return getPassiveTraitSources(player);
	}

	public static ItemStack getBestSustainingSource(Player player) {
		ItemStack best = ItemStack.EMPTY;
		int bestRank = 0;
		for (ItemStack stack : getPassiveTraitSources(player)) {
			int rank = TraitSystem.getSustainingRank(stack);
			if (rank > bestRank) {
				bestRank = rank;
				best = stack;
			}
		}
		ItemStack mainHand = player.getMainHandItem();
		if (ToolProgressionHelper.isTogTool(mainHand)) {
			int rank = TraitSystem.getSustainingRank(mainHand);
			if (rank > bestRank) {
				best = mainHand;
			}
		}
		return best;
	}

	public static float sumWornArmorFloat(Player player, java.util.function.ToDoubleFunction<ItemStack> extractor) {
		double total = 0.0d;
		for (ItemStack stack : getWornTogArmor(player)) {
			total += extractor.applyAsDouble(stack);
		}
		return (float) total;
	}

	public static boolean hasTraitOnDefenseGear(Player player, TraitSystem.Trait trait) {
		for (ItemStack stack : getDefenseTraitSources(player)) {
			if (TraitSystem.hasTrait(stack, trait)) {
				return true;
			}
		}
		return false;
	}

	/** Highest tier among worn Armor of the Gods pieces (0 if none). */
	public static int getHighestWornArmorTier(Player player) {
		int highest = 0;
		for (ItemStack stack : getWornTogArmor(player)) {
			highest = Math.max(highest, ToolProgressionHelper.getStoredTier(stack));
		}
		return highest;
	}
}
