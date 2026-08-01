package net.mcreator.toolsofthegods.util;

import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.network.PacketDistributor;

import net.mcreator.toolsofthegods.integration.TogPatchouliIntegration;
import net.mcreator.toolsofthegods.network.OpenGuideBookPacket;
import net.mcreator.toolsofthegods.network.TogForgeNetwork;

public final class GuideBookOpener {
	private GuideBookOpener() {
	}

	public static void open(ServerPlayer player) {
		if (TogPatchouliIntegration.isAvailable()) {
			TogPatchouliIntegration.openBook(player);
		} else {
			TogForgeNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), OpenGuideBookPacket.INSTANCE);
		}
	}
}
