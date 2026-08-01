package net.mcreator.toolsofthegods.util;

import net.minecraft.server.level.ServerPlayer;

import net.mcreator.toolsofthegods.integration.TogPatchouliIntegration;

public final class GuideBookOpener {
	private GuideBookOpener() {
	}

	public static void open(ServerPlayer player) {
		if (TogPatchouliIntegration.isAvailable()) {
			TogPatchouliIntegration.openBook(player);
		}
	}
}
