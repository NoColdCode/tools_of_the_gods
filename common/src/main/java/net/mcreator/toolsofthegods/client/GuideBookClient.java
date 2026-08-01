package net.mcreator.toolsofthegods.client;

import net.minecraft.client.Minecraft;

public final class GuideBookClient {
	private GuideBookClient() {
	}

	public static void open() {
		Minecraft.getInstance().setScreen(new TogGuideBookScreen());
	}
}
