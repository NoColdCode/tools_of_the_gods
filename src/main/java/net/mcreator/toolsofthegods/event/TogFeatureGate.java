package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TogFeatures;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TogFeatureGate {

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (TogFeatures.extendedToolsEnabled() || !TogFeatures.isExtendedTool(event.getItemStack())) {
			return;
		}
		event.setCanceled(true);
		if (!event.getLevel().isClientSide() && event.getEntity() instanceof Player player) {
			player.displayClientMessage(Component.literal("§8This item is disabled in this pack."), true);
		}
	}
}
