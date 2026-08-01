package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogBlockBreakContext;


import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;

import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;

public final class OrbLootLogic {
	private static final int MOB_DROP_CHANCE = 220; // 1/x chance
	private static final int BLOCK_DROP_CHANCE = 1800; // 1/x chance
	private static final int TRAIT_REMOVER_MOB_CHANCE = 1800; // 1/x chance
	private static final int TRAIT_REMOVER_BLOCK_CHANCE = 6000; // 1/x chance

	public static void onLivingDrops(TogLivingDropsContext ctx) {
		if (!(ctx.entity().level() instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (!(ctx.entity() instanceof Monster)) {
			return;
		}

		if (ctx.source().getEntity() instanceof Player player) {
			if (level.random.nextInt(TRAIT_REMOVER_MOB_CHANCE) == 0) {
				ctx.drops().add(new ItemEntity(level, ctx.entity().getX(), ctx.entity().getY() + 0.5, ctx.entity().getZ(), new ItemStack(ToolsOfTheGodsOrbItems.TRAIT_REMOVER.get())));
				return;
			}
			if (level.random.nextInt(MOB_DROP_CHANCE) == 0) {
				int tier = getHeldTier(player);
				ItemStack gem = ToolsOfTheGodsOrbItems.getRandomGem(level.random, Math.max(3, tier + 1));
				ctx.drops().add(new ItemEntity(level, ctx.entity().getX(), ctx.entity().getY() + 0.5, ctx.entity().getZ(), gem));
			}
		}
	}

	public static void onBlockBreak(TogBlockBreakContext ctx) {
		if (!(ctx.level() instanceof Level level) || level.isClientSide()) {
			return;
		}

		Player player = ctx.player();
		if (player == null || player.isCreative()) {
			return;
		}

		ItemStack held = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(held)) {
			return;
		}

		BlockState state = ctx.state();
		if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			return;
		}

		if (level.random.nextInt(BLOCK_DROP_CHANCE) == 0) {
			int tier = getHeldTier(player);
			ItemStack gem = ToolsOfTheGodsOrbItems.getRandomGem(level.random, Math.max(3, tier));
			level.addFreshEntity(new ItemEntity(level, ctx.pos().getX() + 0.5, ctx.pos().getY() + 0.8, ctx.pos().getZ() + 0.5, gem));
			return;
		}

		if (level.random.nextInt(TRAIT_REMOVER_BLOCK_CHANCE) == 0) {
			level.addFreshEntity(new ItemEntity(level, ctx.pos().getX() + 0.5, ctx.pos().getY() + 0.8, ctx.pos().getZ() + 0.5, new ItemStack(ToolsOfTheGodsOrbItems.TRAIT_REMOVER.get())));
		}
	}

	private static int getHeldTier(Player player) {
		ItemStack held = player.getMainHandItem();
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(held);
		if (type == ToolProgressionHelper.ToolType.NONE) {
			return 0;
		}
		int level = (int) held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level");
		if (level < 0) {
			return 0;
		}
		return ToolProgressionHelper.getStoredTier(held);
	}
}
