package net.mcreator.toolsofthegods.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ArmorItem;
import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TogArmorTextures;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.TogModConstants;

import java.util.List;

public class ArmorOfTheGodsItem extends ArmorItem {
	private final ArmorItem.Type armorType;

	public ArmorOfTheGodsItem(ArmorItem.Type armorType) {
		super(
			TogArmorMaterials.divineHolder(),
			armorType,
			new Item.Properties()
				.fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)
				.durability(armorType.getDurability(TogArmorMaterials.DIVINE.get().enchantmentValue()))
		);
		this.armorType = armorType;
	}

	public ArmorItem.Type getArmorType() {
		return armorType;
	}


	public ResourceLocation resolveArmorTexture(ItemStack stack, boolean innerModel) {
		return TogArmorTextures.wornLayerTexture(ToolProgressionHelper.getStoredTier(stack), innerModel);
	}

	public ItemAttributeModifiers resolveAttributeModifiers(ItemStack stack) {
		EquipmentSlotGroup slotGroup = switch (armorType) {
			case HELMET -> EquipmentSlotGroup.HEAD;
			case CHESTPLATE -> EquipmentSlotGroup.CHEST;
			case LEGGINGS -> EquipmentSlotGroup.LEGS;
			case BOOTS -> EquipmentSlotGroup.FEET;
			default -> EquipmentSlotGroup.HEAD;
		};
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

		float defense = ToolProgressionHelper.getArmorDefensePoints(stack, armorType);
		if (defense > 0f) {
			builder.add(
				Attributes.ARMOR,
				new AttributeModifier(armorModifierId("armor"), defense, AttributeModifier.Operation.ADD_VALUE),
				slotGroup
			);
		}

		float toughness = ToolProgressionHelper.getArmorToughnessValue(stack);
		if (toughness > 0f) {
			builder.add(
				Attributes.ARMOR_TOUGHNESS,
				new AttributeModifier(armorModifierId("toughness"), toughness, AttributeModifier.Operation.ADD_VALUE),
				slotGroup
			);
		}

		float knockback = ToolProgressionHelper.getArmorKnockbackResistance(stack);
		if (knockback > 0f) {
			builder.add(
				Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(armorModifierId("knockback"), knockback, AttributeModifier.Operation.ADD_VALUE),
				slotGroup
			);
		}

		return builder.build();
	}

	/** Unique ID per slot so multiple worn pieces stack on the armor bar. */
	private ResourceLocation armorModifierId(String stat) {
		String slot = switch (armorType) {
			case HELMET -> "helmet";
			case CHESTPLATE -> "chest";
			case LEGGINGS -> "legs";
			case BOOTS -> "boots";
			default -> "body";
		};
		return ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, "tier_" + stat + "_" + slot);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return super.use(world, player, hand);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player player) {
		super.onCraftedBy(itemstack, world, player);
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.ARMOR);
	}

	@Override
	public Component getName(ItemStack itemstack) {
		return Component.literal(ToolProgressionHelper.getDisplayName(itemstack));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		String hoverText = PrimalWoodenToolsPickaxeSpecialInformationProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				list.add(Component.literal(line));
			}
		}
		list.add(Component.literal("§8Shift + Right-Click to upgrade"));
	}

	public static EquipmentSlot getEquipmentSlot(ArmorItem.Type type) {
		return switch (type) {
			case HELMET -> EquipmentSlot.HEAD;
			case CHESTPLATE -> EquipmentSlot.CHEST;
			case LEGGINGS -> EquipmentSlot.LEGS;
			case BOOTS -> EquipmentSlot.FEET;
			default -> EquipmentSlot.HEAD;
		};
	}
}
