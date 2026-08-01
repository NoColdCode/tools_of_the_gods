package net.mcreator.toolsofthegods.item;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.util.StaffSpell;
import net.mcreator.toolsofthegods.util.StaffSpellCaster;
import net.mcreator.toolsofthegods.util.StaffSpellHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class StaffOfTheGodsItem extends Item {
	public StaffOfTheGodsItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).fireResistant());
	}


	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		InteractionResult upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		if (!world.isClientSide()) {
			StaffSpell spell = StaffSpellHelper.getSelectedSpell(stack);
			StaffSpellCaster.cast(world, player, stack, spell);
			ToolProgressionHelper.gainXp(world, player.getX(), player.getY(), player.getZ(), player, stack, 3);
			player.getCooldowns().addCooldown(stack, TraitSystem.getStaffCooldownTicks(stack));
		}
		return TogItemUtils.sidedSuccess(world);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Player player) {
		super.onCraftedBy(stack, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.STAFF);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
		TogItemUtils.appendTogTooltip(stack, context, display, tooltipAdder, flag);
		tooltipAdder.accept(Component.literal("§8Right-click: §7" + StaffSpellHelper.getSelectedSpell(stack).displayName()));
		tooltipAdder.accept(Component.literal("§8Hold G to open spell wheel"));
	}
}
