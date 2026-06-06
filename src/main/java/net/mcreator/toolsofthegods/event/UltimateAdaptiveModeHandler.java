package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class UltimateAdaptiveModeHandler {

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide() || player.tickCount % 5 != 0) {
			return;
		}
		ItemStack stack = findUltimate(player);
		if (!stack.isEmpty()) {
			UltimateToolModeHelper.tryApplyAdaptiveMode(player, stack);
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getLevel().isClientSide()) {
			return;
		}
		ItemStack stack = event.getItemStack();
		if (stack.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			UltimateToolModeHelper.tryApplyAdaptiveMode(event.getEntity(), stack);
		}
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {
		if (!(event.getPlayer() instanceof Player player) || player.level().isClientSide()) {
			return;
		}
		ItemStack stack = event.getPlayer().getMainHandItem();
		if (stack.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			UltimateToolModeHelper.tryApplyAdaptiveMode(player, stack);
		}
	}

	private static ItemStack findUltimate(Player player) {
		ItemStack main = player.getMainHandItem();
		if (main.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			return main;
		}
		ItemStack off = player.getOffhandItem();
		if (off.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			return off;
		}
		return ItemStack.EMPTY;
	}
}
