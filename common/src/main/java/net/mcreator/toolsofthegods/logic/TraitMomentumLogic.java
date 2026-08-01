package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;
import net.minecraft.world.entity.player.Player;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public final class TraitMomentumLogic {

	// Ticks of idle before momentum resets (3 seconds)
	private static final long IDLE_TICKS = 60L;

	public static void onBlockDrops(TogBlockDropsContext ctx) {
		if (!(ctx.breaker() instanceof ServerPlayer player)) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem)) {
			return;
		}

		int maxBlocks = TraitSystem.getMomentumMaxBlocks(heldItem);
		if (maxBlocks <= 0) {
			return;
		}

		long now = player.level().getGameTime();
		long lastTime = TraitSystem.getMomentumLastTime(heldItem);

		// Determine current block count (reset if been idle too long)
		int currentBlocks = TraitSystem.getMomentumBlocks(heldItem);
		if (now - lastTime > IDLE_TICKS && lastTime != 0) {
			currentBlocks = 0;
		}

		// Increment and cap at max
		currentBlocks = Math.min(currentBlocks + 1, maxBlocks);
		TraitSystem.setMomentumBlocks(heldItem, currentBlocks);
		TraitSystem.setMomentumLastTime(heldItem, now);
	}

	// Called every tick (via PlayerTickEvent) to decay momentum on idle.
	// Registered from the existing player tick subscription or as its own.
	public static void onPlayerTick(Player player) {
		if (player.level().isClientSide()) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem)) {
			return;
		}

		int maxBlocks = TraitSystem.getMomentumMaxBlocks(heldItem);
		if (maxBlocks <= 0) {
			return;
		}

		long now = player.level().getGameTime();
		long lastTime = TraitSystem.getMomentumLastTime(heldItem);

		if (lastTime != 0 && now - lastTime > IDLE_TICKS) {
			int current = TraitSystem.getMomentumBlocks(heldItem);
			if (current > 0) {
				TraitSystem.setMomentumBlocks(heldItem, 0);
			}
		}
	}
}
