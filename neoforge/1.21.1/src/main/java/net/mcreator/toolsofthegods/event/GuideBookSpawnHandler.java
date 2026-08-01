package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.GuideBookSpawnLogic;
import net.mcreator.toolsofthegods.platform.neoforge.NeoForgeEventAdapters;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public final class GuideBookSpawnHandler {
	private GuideBookSpawnHandler() {
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
			GuideBookSpawnLogic.onPlayerLogin(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerJoinLevel(EntityJoinLevelEvent event) {
		var player = NeoForgeEventAdapters.serverPlayerFromJoin(event);
		if (player != null) {
			GuideBookSpawnLogic.onPlayerJoinLevel(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		GuideBookSpawnLogic.onPlayerClone(NeoForgeEventAdapters.playerClone(event));
	}
}
