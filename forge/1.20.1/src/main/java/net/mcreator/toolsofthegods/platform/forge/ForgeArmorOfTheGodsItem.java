package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.common.extensions.IForgeItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import com.google.common.collect.Multimap;

import net.mcreator.toolsofthegods.item.ArmorOfTheGodsItem;

public final class ForgeArmorOfTheGodsItem extends ArmorOfTheGodsItem implements IForgeItem {
	public ForgeArmorOfTheGodsItem(ArmorItem.Type armorType) {
		super(armorType);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		ResourceLocation texture = resolveArmorTexture(stack, "overlay".equals(type));
		return texture.toString();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return super.getDefaultAttributeModifiers(slot);
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(ItemStack stack) {
		return resolveAttributeModifiers(stack);
	}
}
