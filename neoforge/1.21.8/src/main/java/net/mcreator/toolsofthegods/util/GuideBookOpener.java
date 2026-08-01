package net.mcreator.toolsofthegods.util;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.toolsofthegods.integration.TogPatchouliIntegration;
import net.mcreator.toolsofthegods.network.OpenGuideBookPayload;

public final class GuideBookOpener {
	private GuideBookOpener() {
	}

	public static void open(ServerPlayer player) {
		if (TogPatchouliIntegration.isAvailable()) {
			TogPatchouliIntegration.openBook(player);
		} else {
			PacketDistributor.sendToPlayer(player, new OpenGuideBookPayload());
		}
	}
}
