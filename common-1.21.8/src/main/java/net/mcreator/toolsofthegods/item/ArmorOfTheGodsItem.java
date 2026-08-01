package net.mcreator.toolsofthegods.item;

import net.minecraft.world.level.Level;
import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TogArmorPiece;
import net.mcreator.toolsofthegods.util.TogArmorTextures;
import net.mcreator.toolsofthegods.TogModConstants;


public class ArmorOfTheGodsItem extends Item {
	private final TogArmorPiece piece;

	public ArmorOfTheGodsItem(ResourceKey<Item> itemId, TogArmorPiece piece) {
		super(
			new Item.Properties()
				.setId(itemId)
				.humanoidArmor(TogArmorMaterials.divineHolder().value(), piece.armorType())
				.fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)
				.durability(piece.armorType().getDurability(TogArmorMaterials.DIVINE_BASE_DURABILITY))
		);
		this.piece = piece;
	}

	public TogArmorPiece getArmorType() {
		return piece;
	}


	public ResourceLocation resolveArmorTexture(ItemStack stack, boolean innerModel) {
		return TogArmorTextures.wornLayerTexture(ToolProgressionHelper.getStoredTier(stack), innerModel);
	}

	public ItemAttributeModifiers resolveAttributeModifiers(ItemStack stack) {
		EquipmentSlotGroup slotGroup = switch (piece) {
			case HELMET -> EquipmentSlotGroup.HEAD;
			case CHESTPLATE -> EquipmentSlotGroup.CHEST;
			case LEGGINGS -> EquipmentSlotGroup.LEGS;
			case BOOTS -> EquipmentSlotGroup.FEET;
		};
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

		float defense = ToolProgressionHelper.getArmorDefensePoints(stack, piece);
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
		String slot = switch (piece) {
			case HELMET -> "helmet";
			case CHESTPLATE -> "chest";
			case LEGGINGS -> "legs";
			case BOOTS -> "boots";
		};
		return ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, "tier_" + stat + "_" + slot);
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		InteractionResult upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return super.use(world, player, hand);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Player player) {
		super.onCraftedBy(itemstack, player);
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.ARMOR);
	}

	@Override
	public Component getName(ItemStack itemstack) {
		return Component.literal(ToolProgressionHelper.getDisplayName(itemstack));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, display, tooltipAdder, flag);
		String hoverText = PrimalWoodenToolsPickaxeSpecialInformationProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				tooltipAdder.accept(Component.literal(line));
			}
		}
		tooltipAdder.accept(Component.literal("§8Shift + Right-Click to upgrade"));
	}

	public static EquipmentSlot getEquipmentSlot(TogArmorPiece piece) {
		return piece.slot();
	}
}
























