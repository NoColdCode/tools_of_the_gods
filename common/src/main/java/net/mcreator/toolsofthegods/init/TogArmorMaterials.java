package net.mcreator.toolsofthegods.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

import java.util.List;
import java.util.Map;

/**
 * Divine armor materials for TOG gear — not vanilla iron/leather.
 * Worn textures per tier: {@code assets/tools_of_the_gods/textures/models/armor/{prefix}_layer_1.png}
 */
public final class TogArmorMaterials {
	public static final TogRegistryEntry<ArmorMaterial> DIVINE = new TogRegistryEntry<>();
	public static final TogRegistryEntry<ArmorMaterial> AERIAL = new TogRegistryEntry<>();

	public static ArmorMaterial createDivineMaterial() {
		return new ArmorMaterial(
			Map.of(
				ArmorItem.Type.BOOTS, 0,
				ArmorItem.Type.LEGGINGS, 0,
				ArmorItem.Type.CHESTPLATE, 0,
				ArmorItem.Type.HELMET, 0
			),
			25,
			SoundEvents.ARMOR_EQUIP_LEATHER,
			() -> Ingredient.of(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get()),
			List.of(new ArmorMaterial.Layer(
				ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, "motion_of_the_gods"))),
			0.0f,
			0.0f
		);
	}

	/** Wings — no armor points, no chestplate overlay (elytra flight only). */
	public static ArmorMaterial createAerialMaterial() {
		return new ArmorMaterial(
			Map.of(ArmorItem.Type.CHESTPLATE, 0),
			15,
			SoundEvents.ARMOR_EQUIP_ELYTRA,
			() -> Ingredient.of(Items.PHANTOM_MEMBRANE),
			List.of(),
			0.0f,
			0.0f
		);
	}

	public static Holder<ArmorMaterial> divineHolder() {
		return BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(DIVINE.get());
	}

	public static Holder<ArmorMaterial> aerialHolder() {
		return BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(AERIAL.get());
	}

	private TogArmorMaterials() {
	}
}
