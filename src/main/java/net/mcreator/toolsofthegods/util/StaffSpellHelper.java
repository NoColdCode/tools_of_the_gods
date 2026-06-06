package net.mcreator.toolsofthegods.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;

public final class StaffSpellHelper {
	private static final String NBT_STAFF_SPELL = "togStaffSpell";

	private StaffSpellHelper() {
	}

	public static int getStaffLevel(ItemStack stack) {
		return (int) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level");
	}

	public static List<StaffSpell> getUnlockedSpells(ItemStack stack) {
		int level = getStaffLevel(stack);
		List<StaffSpell> unlocked = new ArrayList<>();
		for (StaffSpell spell : StaffSpell.values()) {
			if (spell.isUnlocked(level)) {
				unlocked.add(spell);
			}
		}
		if (unlocked.isEmpty()) {
			unlocked.add(StaffSpell.DIVINE_BOLT);
		}
		return unlocked;
	}

	public static StaffSpell getSelectedSpell(ItemStack stack) {
		int ordinal = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(NBT_STAFF_SPELL);
		StaffSpell selected = StaffSpell.fromOrdinal(ordinal);
		if (selected.isUnlocked(getStaffLevel(stack))) {
			return selected;
		}
		StaffSpell fallback = StaffSpell.DIVINE_BOLT;
		for (StaffSpell spell : StaffSpell.values()) {
			if (spell.isUnlocked(getStaffLevel(stack))) {
				fallback = spell;
			}
		}
		return fallback;
	}

	public static void setSelectedSpell(ItemStack stack, StaffSpell spell) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(NBT_STAFF_SPELL, spell.ordinal()));
	}
}
