package net.mcreator.toolsofthegods.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

import java.util.List;
import java.util.Map;

/**
 * Divine armor materials for TOG gear — not vanilla iron/leather.
 * Worn textures per tier: {@code assets/tools_of_the_gods/textures/models/armor/{prefix}_layer_1.png}
 */
public final class TogArmorMaterials {
	public static final DeferredRegister<ArmorMaterial> REGISTRY =
		DeferredRegister.create(Registries.ARMOR_MATERIAL, ToolsOfTheGodsMod.MODID);

	/** Base material — defense comes from {@link ToolProgressionHelper} per tier via item attributes. */
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DIVINE = REGISTRY.register("motion_of_the_gods", () -> new ArmorMaterial(
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
			ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "motion_of_the_gods"))),
		0.0f,
		0.0f
	));

	/** Wings — no armor points, no chestplate overlay (elytra flight only). */
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> AERIAL = REGISTRY.register("aerial", () -> new ArmorMaterial(
		Map.of(ArmorItem.Type.CHESTPLATE, 0),
		15,
		SoundEvents.ARMOR_EQUIP_ELYTRA,
		() -> Ingredient.of(Items.PHANTOM_MEMBRANE),
		List.of(),
		0.0f,
		0.0f
	));

	private TogArmorMaterials() {
	}
}
