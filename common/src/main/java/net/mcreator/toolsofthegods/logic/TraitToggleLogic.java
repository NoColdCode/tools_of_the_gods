package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogRightClickItemContext;


import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

import java.util.ArrayList;
import java.util.List;

public final class TraitToggleLogic {

	private enum ToggleTarget {
		SILKY,
		AUTOSMELT,
		BROAD_TOUCH
	}

	public static void onRightClickItem(TogRightClickItemContext ctx) {
		if (ctx.level().isClientSide()) {
			return;
		}

		Player player = ctx.player();
		if (!player.isShiftKeyDown()) {
			return;
		}

		ItemStack stack = ctx.itemStack();
		if (!ToolProgressionHelper.isTogTool(stack)) {
			return;
		}

		if (ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.ULTIMATE) {
			return;
		}

		if (ToolProgressionHelper.needsUpgrade(stack)) {
			UpgradePickaxeProcedure.tryUpgrade(
				ctx.level(), player.getX(), player.getY(), player.getZ(), player, stack);
			ctx.setCanceled(true);
			return;
		}

		List<ToggleTarget> targets = getToggleTargets(stack);
		if (targets.isEmpty()) {
			return;
		}

		int index = TraitSystem.advanceToggleCycle(stack, targets.size());
		ToggleTarget target = targets.get(index);
		switch (target) {
			case SILKY -> TraitSystem.toggleSilky(stack);
			case AUTOSMELT -> TraitSystem.toggleAutosmelt(stack);
			case BROAD_TOUCH -> {
				ToolType type = ToolProgressionHelper.getToolType(stack);
				int maxModes = (type == ToolType.HAMMER) ? 4 : (type == ToolType.AXE) ? 2 : 3;
				TraitSystem.cycleBroadTouchMode(stack, maxModes);
			}
		}

		player.displayClientMessage(Component.literal(buildToggleStatus(stack)), true);
		ctx.setCanceled(true);
	}

	private static List<ToggleTarget> getToggleTargets(ItemStack stack) {
		List<ToggleTarget> targets = new ArrayList<>();
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.SILKY_II)) {
			targets.add(ToggleTarget.SILKY);
		}
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.AUTOSMELT_II)) {
			targets.add(ToggleTarget.AUTOSMELT);
		}
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BROAD_TOUCH_II)) {
			targets.add(ToggleTarget.BROAD_TOUCH);
		}
		return targets;
	}

	private static String buildToggleStatus(ItemStack stack) {
		StringBuilder status = new StringBuilder("\u00a77Traits: ");
		boolean first = true;
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.SILKY_II)) {
			status.append("\u00a7aSilk \u00a7f").append(TraitSystem.isSilkyActive(stack) ? "ON" : "OFF");
			first = false;
		}
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.AUTOSMELT_II)) {
			if (!first) {
				status.append("\u00a78 | ");
			}
			status.append("\u00a76Smelt \u00a7f").append(TraitSystem.isAutosmeltActive(stack) ? "ON" : "OFF");
			first = false;
		}
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BROAD_TOUCH_II)) {
			if (!first) {
				status.append("\u00a78 | ");
			}
			ToolType type = ToolProgressionHelper.getToolType(stack);
			int mode = TraitSystem.getBroadTouchMode(stack);
			status.append("\u00a75Area \u00a7f").append(getBroadTouchModeLabel(type, mode));
		}
		return status.toString();
	}

	private static String getBroadTouchModeLabel(ToolType type, int mode) {
		if (type == ToolType.AXE) {
			return (mode == 0) ? "Fell 12" : "Fell 64";
		} else if (type == ToolType.HAMMER) {
			return switch (mode) { case 0 -> "3x3"; case 1 -> "5x5"; case 2 -> "7x7"; default -> "9x9"; };
		} else {
			return switch (mode) { case 0 -> "1x1"; case 1 -> "3x3"; default -> "5x5"; };
		}
	}
}
