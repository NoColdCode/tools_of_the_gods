package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

import net.mcreator.toolsofthegods.util.FlailCombatHelper;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TogToolMaterials;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class FlailOfTheGodsItem extends Item {
	public FlailOfTheGodsItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).sword(TogToolMaterials.PRIMAL, 0f, -3.5f).fireResistant());
	}


	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		InteractionResult upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(stack, target, attacker);
		int level = FlailCombatHelper.getLevel(stack);
		FlailCombatHelper.applyStun(target, level);
		TraitPoisonHelper.applyMeleePoison(stack, target);
		TraitFreezyHelper.applyMeleeFreeze(stack, target);
		TraitExtendedCombatHelper.applyMeleeEffects(stack, target);
		ToolProgressionHelper.gainXp(attacker.level(), attacker.getX(), attacker.getY(), attacker.getZ(), attacker, stack,
			ToolProgressionHelper.getSwordHitXp(target));
	}

	@Override
	public void onCraftedBy(ItemStack stack, Player player) {
		super.onCraftedBy(stack, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.FLAIL);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
		TogItemUtils.appendTogTooltip(stack, context, display, tooltipAdder, flag);
		int level = FlailCombatHelper.getLevel(stack);
		tooltipAdder.accept(Component.literal("§7Stun: §d" + String.format("%.1f", FlailCombatHelper.getStunSecondsForDisplay(level)) + "s §8(boss ×0.5)"));
		tooltipAdder.accept(Component.literal("§7Attack speed: §b" + String.format("%.2f", FlailCombatHelper.getDisplayedAttackSpeed(level))));
	}
}
