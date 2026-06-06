package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.util.GuideBookOpener;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class GuideBookOpenHandler {

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getLevel().isClientSide() || !(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		if (!event.getItemStack().is(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK.get())) {
			return;
		}

		GuideBookOpener.open(player);
		event.setCancellationResult(InteractionResult.SUCCESS);
		event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getLevel().isClientSide() || !(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		if (!event.getItemStack().is(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK.get())) {
			return;
		}

		GuideBookOpener.open(player);
		event.setUseBlock(TriState.FALSE);
		event.setUseItem(TriState.FALSE);
		event.setCancellationResult(InteractionResult.SUCCESS);
		event.setCanceled(true);
	}
}
