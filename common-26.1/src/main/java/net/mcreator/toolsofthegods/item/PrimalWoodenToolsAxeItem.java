package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;

import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TogToolMaterials;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class PrimalWoodenToolsAxeItem extends AxeItem {
	public PrimalWoodenToolsAxeItem(ResourceKey<Item> itemId) {
		super(TogToolMaterials.PRIMAL, 0f, -3f, new Item.Properties().setId(itemId).fireResistant());
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
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
		boolean ret = super.mineBlock(itemstack, world, state, pos, entity);
		ToolProgressionHelper.gainXp(world, pos.getX(), pos.getY(), pos.getZ(), entity, itemstack, 1);
		return ret;
	}

	@Override
	public void hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(itemstack, target, attacker);
		TraitPoisonHelper.applyMeleePoison(itemstack, target);
		TraitFreezyHelper.applyMeleeFreeze(itemstack, target);
		TraitExtendedCombatHelper.applyMeleeEffects(itemstack, target);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Player player) {
		super.onCraftedBy(itemstack, player);
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.AXE);
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
}
