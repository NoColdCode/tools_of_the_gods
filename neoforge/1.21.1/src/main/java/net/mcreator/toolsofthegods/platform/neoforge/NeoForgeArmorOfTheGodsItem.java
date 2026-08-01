package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.neoforge.common.extensions.IItemExtension;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.item.ArmorOfTheGodsItem;

/** NeoForge {@link IItemExtension} bridge for tiered armor textures. */
public final class NeoForgeArmorOfTheGodsItem extends ArmorOfTheGodsItem implements IItemExtension {
	public NeoForgeArmorOfTheGodsItem(ArmorItem.Type armorType) {
		super(armorType);
	}

	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return resolveArmorTexture(stack, innerModel);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		return resolveAttributeModifiers(stack);
	}
}
