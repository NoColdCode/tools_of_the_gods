package net.mcreator.toolsofthegods.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum StaffSpell {
	DIVINE_BOLT("Divine Bolt", Items.BLAZE_POWDER),
	FROST_SHARD("Frost Shard", Items.SNOWBALL),
	ARCANE_BURST("Arcane Burst", Items.ENDER_PEARL),
	HEALING_PULSE("Healing Pulse", Items.GOLDEN_APPLE),
	LIGHTNING_STRIKE("Lightning Strike", Items.LIGHTNING_ROD),
	KNOCKBACK_WAVE("Knockback Wave", Items.PISTON),
	POISON_MIST("Poison Mist", Items.SPIDER_EYE),
	HOLY_BEAM("Holy Beam", Items.GLOWSTONE),
	METEOR("Meteor", Items.FIRE_CHARGE),
	SOUL_DRAIN("Soul Drain", Items.WITHER_SKELETON_SKULL),
	SHIELD_WARD("Shield Ward", Items.SHIELD),
	VOID_SNARE("Void Snare", Items.ENDER_EYE);

	private final String displayName;
	private final Item iconItem;

	StaffSpell(String displayName, Item iconItem) {
		this.displayName = displayName;
		this.iconItem = iconItem;
	}

	public String displayName() {
		return displayName;
	}

	public Item iconItem() {
		return iconItem;
	}

	/** Unlocks at staff level {@code ordinal + 1} (one new spell per level up to 12). */
	public int unlockLevel() {
		return ordinal() + 1;
	}

	public boolean isUnlocked(int staffLevel) {
		return staffLevel >= unlockLevel();
	}

	public static StaffSpell fromOrdinal(int ordinal) {
		StaffSpell[] values = values();
		if (ordinal < 0 || ordinal >= values.length) {
			return DIVINE_BOLT;
		}
		return values[ordinal];
	}
}
