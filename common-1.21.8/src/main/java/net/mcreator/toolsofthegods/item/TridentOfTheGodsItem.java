package net.mcreator.toolsofthegods.item;


import net.minecraft.world.item.TridentItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class TridentOfTheGodsItem extends TridentItem {
	public TridentOfTheGodsItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).fireResistant());
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
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(stack, target, attacker);
		TraitPoisonHelper.applyMeleePoison(stack, target);
		TraitFreezyHelper.applyMeleeFreeze(stack, target);
		TraitExtendedCombatHelper.applyMeleeEffects(stack, target);
		ToolProgressionHelper.gainXp(attacker.level(), attacker.getX(), attacker.getY(), attacker.getZ(), attacker, stack,
			ToolProgressionHelper.getSwordHitXp(target));
	}

	@Override
	public void onCraftedBy(ItemStack stack, Player player) {
		super.onCraftedBy(stack, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.TRIDENT);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
		TogItemUtils.appendTogTooltip(stack, context, display, tooltipAdder, flag);
	}
}
