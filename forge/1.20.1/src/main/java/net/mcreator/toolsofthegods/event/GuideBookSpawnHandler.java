package net.mcreator.toolsofthegods.event;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.logic.GuideBookSpawnLogic;
import net.mcreator.toolsofthegods.platform.forge.ForgeEventAdapters;

@Mod.EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
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
		var player = ForgeEventAdapters.serverPlayerFromJoin(event);
		if (player != null) {
			GuideBookSpawnLogic.onPlayerJoinLevel(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		GuideBookSpawnLogic.onPlayerClone(ForgeEventAdapters.playerClone(event));
	}
}
