package net.mcreator.toolsofthegods.platform.fabric.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.mcreator.toolsofthegods.client.WingsResistHud;

/**
 * Draws wing Resistance over the XP bar while fall-flying (Fabric has no cancel for XP layers on 1.21.1).
 */
public final class FabricWingsResistHud {
	private FabricWingsResistHud() {
	}

	public static void register() {
		HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
			WingsResistHud.tryRender(graphics);
		});
	}
}
