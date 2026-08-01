package net.mcreator.toolsofthegods.item;


import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeToolInInventoryTickProcedure;
import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeItemIsCraftedsmeltedProcedure;
import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeBlockDestroyedWithToolProcedure;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.TogToolMaterials;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;


public class PrimalWoodenToolsPickaxeItem extends Item {
	public PrimalWoodenToolsPickaxeItem(ResourceKey<Item> itemId) {
		super(new Item.Properties().setId(itemId).pickaxe(TogToolMaterials.PRIMAL, 0f, -3f).fireResistant());
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
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
		boolean retval = super.mineBlock(itemstack, world, blockstate, pos, entity);
		PrimalWoodenToolsPickaxeBlockDestroyedWithToolProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity, itemstack);
		return retval;
	}

	@Override
	public void hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(itemstack, target, attacker);
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack itemstack, BlockState state) {
		int level = (int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("level", 0.0);
		int tier = TierSystem.getTierFromLevel(level);
		return TierSystem.canHarvest(state, tier);
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

	@Override
	public void onCraftedBy(ItemStack itemstack, Player entity) {
		super.onCraftedBy(itemstack, entity);
		PrimalWoodenToolsPickaxeItemIsCraftedsmeltedProcedure.execute(itemstack);
	}

	@Override
	public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, EquipmentSlot slot) {
		super.inventoryTick(itemstack, world, entity, slot);
		PrimalWoodenToolsPickaxeToolInInventoryTickProcedure.execute(entity);
	}
}
