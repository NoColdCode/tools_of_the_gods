package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitToggleHandler {

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getLevel().isClientSide()) {
			return;
		}

		Player player = event.getEntity();
		if (!player.isShiftKeyDown()) {
			return;
		}

		ItemStack stack = event.getItemStack();
		if (!ToolProgressionHelper.isTogTool(stack)) {
			return;
		}

		if (ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.ULTIMATE) {
			return;
		}

		// Upgrade takes priority over trait toggles (same key bind: shift + right-click).
		if (ToolProgressionHelper.needsUpgrade(stack)) {
			return;
		}

		boolean toggled = false;

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.SILKY_II)) {
			boolean on = TraitSystem.toggleSilky(stack);
			player.displayClientMessage(Component.literal("\u00a7aSilk Touch: \u00a7f" + (on ? "ON" : "OFF")), true);
			toggled = true;
		}

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.AUTOSMELT_II)) {
			boolean on = TraitSystem.toggleAutosmelt(stack);
			player.displayClientMessage(Component.literal("\u00a76Autosmelt: \u00a7f" + (on ? "ON" : "OFF")), true);
			toggled = true;
		}

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BROAD_TOUCH_II)) {
			ToolType type = ToolProgressionHelper.getToolType(stack);
			int maxModes = (type == ToolType.HAMMER) ? 4 : (type == ToolType.AXE) ? 2 : 3;
			TraitSystem.cycleBroadTouchMode(stack, maxModes);
			int mode = TraitSystem.getBroadTouchMode(stack);
			String modeLabel = getBroadTouchModeLabel(type, mode);
			player.displayClientMessage(Component.literal("\u00a75Broad Touch: \u00a7f" + modeLabel), true);
			toggled = true;
		}

		if (toggled) {
			event.setCanceled(true);
		}
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
