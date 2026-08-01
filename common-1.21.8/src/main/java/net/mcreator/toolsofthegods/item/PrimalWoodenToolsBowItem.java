package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.Level;
import net.minecraft.world.item.BowItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;


public class PrimalWoodenToolsBowItem extends BowItem {
	public PrimalWoodenToolsBowItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).durability(384).fireResistant());
	}

	public int getEnchantmentValue(ItemStack stack) {
		return 15;
	}

	@Override
	public boolean releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int timeLeft) {
		boolean released = super.releaseUsing(itemstack, world, entity, timeLeft);
		TraitPoisonHelper.applyBowPoison(itemstack, world, entity);
				TraitFreezyHelper.applyBowFreeze(itemstack, world, entity);
		TraitExtendedCombatHelper.applyBowEffects(itemstack, world, entity);
		if (!world.isClientSide() && entity instanceof Player) {
			int charge = this.getUseDuration(itemstack, entity) - timeLeft;
			if (charge > 5) {
				ToolProgressionHelper.gainXp(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack, 2);
			}
		}
		return released;
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Player player) {
		super.onCraftedBy(itemstack, player);
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.BOW);
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
