package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitBroadTouchHandler {

	// Recursion guard: prevents re-entry when breaking neighbour blocks
	private static final Set<UUID> ACTIVE_BREAKERS = new HashSet<>();

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onBlockDrops(BlockDropsEvent event) {
		if (!(event.getBreaker() instanceof ServerPlayer player)) {
			return;
		}
		if (ACTIVE_BREAKERS.contains(player.getUUID())) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!TraitSystem.hasBroadTouch(heldItem)) {
			return;
		}

		ToolType toolType = ToolProgressionHelper.getToolType(heldItem);
		if (toolType == ToolType.NONE || toolType == ToolType.SWORD || toolType == ToolType.SPEAR || toolType == ToolType.FLAIL
				|| toolType == ToolType.BOW || toolType == ToolType.HOE) {
			return;
		}

		ServerLevel level = (ServerLevel) event.getLevel();
		BlockPos origin = event.getPos();

		// Axe: tree felling
		if (toolType == ToolType.AXE) {
			if (!event.getState().is(BlockTags.LOGS)) {
				return;
			}
			int maxLogs = getMaxLogs(heldItem);
			fellTree(level, player, origin, maxLogs);
			return;
		}

		// Pickaxe/Shovel/Hammer: area mining
		int radius = getAreaRadius(heldItem, toolType);
		if (radius <= 0) {
			return;
		}

		Direction miningFace = getMiningFace(player, origin);
		mineArea(level, player, origin, miningFace, radius);
	}

	private static int getMaxLogs(ItemStack stack) {
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BROAD_TOUCH_II)) {
			int mode = TraitSystem.getBroadTouchMode(stack);
			return (mode == 0) ? 12 : 64;
		}
		return 12;
	}

	private static int getAreaRadius(ItemStack stack, ToolType type) {
		boolean hasBT2 = TraitSystem.hasTrait(stack, TraitSystem.Trait.BROAD_TOUCH_II);
		if (type == ToolType.HAMMER || type == ToolType.ULTIMATE) {
			if (hasBT2) {
				int mode = TraitSystem.getBroadTouchMode(stack);
				// 0->1 (3x3), 1->2 (5x5), 2->3 (7x7), 3->4 (9x9)
				return mode + 1;
			}
			return 2; // BT I: 5x5
		}
		// Pickaxe / Shovel
		if (hasBT2) {
			int mode = TraitSystem.getBroadTouchMode(stack);
			if (mode == 0) return 0; // 1x1 (effectively off)
			return mode; // 1->1 (3x3), 2->2 (5x5)
		}
		return 1; // BT I: 3x3
	}

	private static Direction getMiningFace(ServerPlayer player, BlockPos pos) {
		Vec3 toBlock = Vec3.atCenterOf(pos).subtract(player.getEyePosition()).normalize();
		return Direction.getNearest(toBlock.x, toBlock.y, toBlock.z);
	}

	private static void mineArea(ServerLevel level, ServerPlayer player,
			BlockPos origin, Direction face, int radius) {
		Direction ax1;
		Direction ax2;
		// Two axes perpendicular to the face
		switch (face.getAxis()) {
			case Y -> { ax1 = Direction.EAST;  ax2 = Direction.SOUTH; }
			case X -> { ax1 = Direction.UP;    ax2 = Direction.SOUTH; }
			default -> { ax1 = Direction.EAST; ax2 = Direction.UP;    }
		}

		ACTIVE_BREAKERS.add(player.getUUID());
		try {
			for (int i = -radius; i <= radius; i++) {
				for (int j = -radius; j <= radius; j++) {
					if (i == 0 && j == 0) continue; // already broken
					BlockPos neighbor = origin.relative(ax1, i).relative(ax2, j);
					if (!level.isLoaded(neighbor)) continue;
					if (level.getBlockState(neighbor).isAir()) continue;
					level.destroyBlock(neighbor, true, player);
				}
			}
		} finally {
			ACTIVE_BREAKERS.remove(player.getUUID());
		}
	}

	private static void fellTree(ServerLevel level, ServerPlayer player,
			BlockPos start, int maxLogs) {
		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new ArrayDeque<>();
		queue.add(start);
		visited.add(start);

		ACTIVE_BREAKERS.add(player.getUUID());
		try {
			while (!queue.isEmpty()) {
				BlockPos current = queue.poll();
				// Break log (skip the origin – it's already being handled)
				if (!current.equals(start)) {
					if (!level.isLoaded(current)) continue;
					level.destroyBlock(current, true, player);
				}
				// BFS into connected logs (26-neighbour)
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = 0; dy <= 1; dy++) {  // only upward / same level
						for (int dz = -1; dz <= 1; dz++) {
							if (dx == 0 && dy == 0 && dz == 0) continue;
							BlockPos next = current.offset(dx, dy, dz);
							if (!visited.contains(next)
									&& level.isLoaded(next)
									&& level.getBlockState(next).is(BlockTags.LOGS)) {
								visited.add(next);
								if (visited.size() <= maxLogs) {
									queue.add(next);
								}
							}
						}
					}
				}
			}
		} finally {
			ACTIVE_BREAKERS.remove(player.getUUID());
		}
	}
}
