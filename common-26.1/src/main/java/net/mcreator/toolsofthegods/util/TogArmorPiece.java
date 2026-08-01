package net.mcreator.toolsofthegods.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.ArmorType;

public enum TogArmorPiece {
	HELMET(ArmorType.HELMET, EquipmentSlot.HEAD),
	CHESTPLATE(ArmorType.CHESTPLATE, EquipmentSlot.CHEST),
	LEGGINGS(ArmorType.LEGGINGS, EquipmentSlot.LEGS),
	BOOTS(ArmorType.BOOTS, EquipmentSlot.FEET);

	private final ArmorType armorType;
	private final EquipmentSlot slot;

	TogArmorPiece(ArmorType armorType, EquipmentSlot slot) {
		this.armorType = armorType;
		this.slot = slot;
	}

	public ArmorType armorType() {
		return armorType;
	}

	public EquipmentSlot slot() {
		return slot;
	}

	public static TogArmorPiece from(ArmorType type) {
		for (TogArmorPiece piece : values()) {
			if (piece.armorType == type) {
				return piece;
			}
		}
		return HELMET;
	}
}
