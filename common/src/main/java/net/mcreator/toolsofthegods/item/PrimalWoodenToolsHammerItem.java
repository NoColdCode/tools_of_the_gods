package net.mcreator.toolsofthegods.item;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.procedures.PrimalWoodenToolsPickaxeSpecialInformationProcedure;
import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

public class PrimalWoodenToolsHammerItem extends PickaxeItem {
	private static final String NBT_AOE_ACTIVE = "togHammerAoeActive";

	private static final Tier TOOL_TIER = new Tier() {
		@Override
		public int getUses() {
			return 0;
		}

		@Override
		public float getSpeed() {
			return 0.5f;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 3;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(new ItemStack(Items.WOODEN_PICKAXE));
		}
	};

	public PrimalWoodenToolsHammerItem() {
		super(TOOL_TIER, new Item.Properties().attributes(DiggerItem.createAttributes(TOOL_TIER, 1.0f, -3.2f)).fireResistant());
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return InteractionResultHolder.pass(itemstack);
	}

	@Override
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
		boolean ret = super.mineBlock(itemstack, world, state, pos, entity);

		int tier = ToolProgressionHelper.getStoredTier(itemstack);
		int xpPerBlock = 1;
		ToolProgressionHelper.gainXp(world, pos.getX(), pos.getY(), pos.getZ(), entity, itemstack, xpPerBlock);

		if (world.isClientSide() || !(entity instanceof Player player)) {
			return ret;
		}

		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(NBT_AOE_ACTIVE)) {
			return ret;
		}

		if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			return ret;
		}

		setAoeFlag(itemstack, true);
		try {
			breakAroundCenter(world, player, itemstack, pos, tier, xpPerBlock);
		} finally {
			setAoeFlag(itemstack, false);
		}

		return ret;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity attacker) {
		boolean ret = super.hurtEnemy(itemstack, target, attacker);
		return ret;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack itemstack, BlockState state) {
		int level = (int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level");
		int tier = TierSystem.getTierFromLevel(level);
		return TierSystem.canHarvest(state, tier);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player player) {
		super.onCraftedBy(itemstack, world, player);
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.HAMMER);
	}

	@Override
	public Component getName(ItemStack itemstack) {
		return Component.literal(ToolProgressionHelper.getDisplayName(itemstack));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		String hoverText = PrimalWoodenToolsPickaxeSpecialInformationProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\\n")) {
				list.add(Component.literal(line));
			}
		}
		list.add(Component.literal("§8Mines 3x3x1 area"));
		list.add(Component.literal("§8Shift + Right-Click to upgrade"));
	}

	private static void setAoeFlag(ItemStack stack, boolean active) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(NBT_AOE_ACTIVE, active));
	}

	private static void breakAroundCenter(Level world, Player player, ItemStack hammer, BlockPos origin, int tier, int xpPerBlock) {
		Direction.Axis axis = getMiningPlaneAxis(player);

		for (int a = -1; a <= 1; a++) {
			for (int b = -1; b <= 1; b++) {
				if (a == 0 && b == 0) {
					continue;
				}

				BlockPos targetPos = switch (axis) {
					case X -> origin.offset(0, a, b);
					case Y -> origin.offset(a, 0, b);
					case Z -> origin.offset(a, b, 0);
				};

				BlockState target = world.getBlockState(targetPos);
				if (!canAoeBreak(world, targetPos, target, tier)) {
					continue;
				}

				world.destroyBlock(targetPos, true, player);
			}
		}
	}

	private static boolean canAoeBreak(Level world, BlockPos pos, BlockState state, int tier) {
		if (state.isAir()) {
			return false;
		}
		if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			return false;
		}
		if (!TierSystem.canHarvest(state, tier)) {
			return false;
		}
		if (state.getDestroySpeed(world, pos) < 0) {
			return false;
		}
		return true;
	}

	private static Direction.Axis getMiningPlaneAxis(Player player) {
		double lookX = Math.abs(player.getLookAngle().x);
		double lookY = Math.abs(player.getLookAngle().y);
		double lookZ = Math.abs(player.getLookAngle().z);

		if (lookY > lookX && lookY > lookZ) {
			return Direction.Axis.Y;
		}
		return lookX > lookZ ? Direction.Axis.X : Direction.Axis.Z;
	}
}
