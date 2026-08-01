package net.mcreator.toolsofthegods.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

import java.util.Map;

public final class TogArmorMaterials {
	public static final int DIVINE_BASE_DURABILITY = 25;
	public static final int AERIAL_BASE_DURABILITY = 15;

	public static final TagKey<Item> REPAIRS_DIVINE_ARMOR = TagKey.create(
		BuiltInRegistries.ITEM.key(),
		Identifier.fromNamespaceAndPath(TogModConstants.MODID, "repairs_divine_armor")
	);

	public static final ResourceKey<EquipmentAsset> DIVINE_ASSET_KEY = ResourceKey.create(
		EquipmentAssets.ROOT_ID,
		Identifier.fromNamespaceAndPath(TogModConstants.MODID, "motion_of_the_gods")
	);

	public static final ResourceKey<EquipmentAsset> AERIAL_ASSET_KEY = ResourceKey.create(
		EquipmentAssets.ROOT_ID,
		Identifier.fromNamespaceAndPath(TogModConstants.MODID, "aerial")
	);

	public static final TogRegistryEntry<ArmorMaterial> DIVINE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<ArmorMaterial> AERIAL = new TogRegistryEntry<>();

	public static ArmorMaterial createDivineMaterial() {
		return new ArmorMaterial(
			DIVINE_BASE_DURABILITY,
			Map.of(
				ArmorType.BOOTS, 0,
				ArmorType.LEGGINGS, 0,
				ArmorType.CHESTPLATE, 0,
				ArmorType.HELMET, 0
			),
			25,
			SoundEvents.ARMOR_EQUIP_LEATHER,
			0.0F,
			0.0F,
			REPAIRS_DIVINE_ARMOR,
			DIVINE_ASSET_KEY
		);
	}

	public static ArmorMaterial createAerialMaterial() {
		return new ArmorMaterial(
			AERIAL_BASE_DURABILITY,
			Map.of(ArmorType.CHESTPLATE, 0),
			15,
			SoundEvents.ARMOR_EQUIP_ELYTRA,
			0.0F,
			0.0F,
			TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(TogModConstants.MODID, "repairs_aerial_armor")),
			AERIAL_ASSET_KEY
		);
	}

	public static Holder<ArmorMaterial> divineHolder() {
		return Holder.direct(DIVINE.get());
	}

	public static Holder<ArmorMaterial> aerialHolder() {
		return Holder.direct(AERIAL.get());
	}

	private TogArmorMaterials() {
	}
}
