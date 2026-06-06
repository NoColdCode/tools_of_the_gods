package net.mcreator.toolsofthegods.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import java.util.List;

public class PrimalWoodenToolsBowItem extends BowItem {
	public PrimalWoodenToolsBowItem() {
		super(new Item.Properties().durability(0).fireResistant());
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
	public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int timeLeft) {
		super.releaseUsing(itemstack, world, entity, timeLeft);
		TraitPoisonHelper.applyBowPoison(itemstack, world, entity);
				TraitFreezyHelper.applyBowFreeze(itemstack, world, entity);
		if (!world.isClientSide() && entity instanceof Player) {
			int charge = this.getUseDuration(itemstack, entity) - timeLeft;
			if (charge > 5) {
				ToolProgressionHelper.gainXp(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack, 2);
			}
		}
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player player) {
		super.onCraftedBy(itemstack, world, player);
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.BOW);
	}

	@Override
	public Component getName(ItemStack itemstack) {
		return Component.literal(ToolProgressionHelper.getDisplayName(itemstack));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, net.minecraft.world.item.TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		String hoverText = PrimalWoodenToolsPickaxeSpecialInformationProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				list.add(Component.literal(line));
			}
		}
		list.add(Component.literal("§8Shift + Right-Click to upgrade"));
	}
}
