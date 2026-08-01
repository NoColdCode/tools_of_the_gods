package net.mcreator.toolsofthegods.item;

import net.minecraft.world.item.ArmorItem;
import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

public class WingsOfTheGodsItem extends ArmorItem {
	public WingsOfTheGodsItem() {
		super(
			TogArmorMaterials.aerialHolder(),
			ArmorItem.Type.CHESTPLATE,
			new Item.Properties()
				.fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)
				.durability(ArmorItem.Type.CHESTPLATE.getDurability(TogArmorMaterials.AERIAL.get().enchantmentValue()))
		);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return super.use(world, player, hand);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		super.onCraftedBy(stack, world, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.WINGS);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		TogItemUtils.appendTogTooltip(stack, context, list, flag);
		list.add(Component.literal("§8Chest slot · Cape → Elytra → Icarus flight"));
	}
}
