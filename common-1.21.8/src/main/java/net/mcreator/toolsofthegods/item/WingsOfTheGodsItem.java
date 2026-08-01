package net.mcreator.toolsofthegods.item;

import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.util.TogArmorPiece;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class WingsOfTheGodsItem extends Item {
	public WingsOfTheGodsItem(ResourceKey<Item> itemId) {
		super(
			new Item.Properties()
				.setId(itemId)
				.humanoidArmor(TogArmorMaterials.aerialHolder().value(), TogArmorPiece.CHESTPLATE.armorType())
				.fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)
				.durability(TogArmorPiece.CHESTPLATE.armorType().getDurability(TogArmorMaterials.AERIAL_BASE_DURABILITY))
		);
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
	public void onCraftedBy(ItemStack stack, Player player) {
		super.onCraftedBy(stack, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.WINGS);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
		TogItemUtils.appendTogTooltip(stack, context, display, tooltipAdder, flag);
		tooltipAdder.accept(Component.literal("\u00a78Chest slot · Cape → Elytra → Wings flight"));
	}
}
