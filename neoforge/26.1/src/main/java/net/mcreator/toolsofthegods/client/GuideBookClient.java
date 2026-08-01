package net.mcreator.toolsofthegods.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.item.TogGuideBookItem;

import java.util.List;

/**
 * NeoForge 26.1 guide opener. Uses vanilla BookViewScreen because the custom
 * GuiGraphics-based TogGuideBookScreen is incompatible with the 26.1 render rewrite.
 */
public final class GuideBookClient {
	private GuideBookClient() {
	}

	public static void open() {
		List<Component> pages = TogGuideBookItem.getGuidePages();
		Minecraft.getInstance().setScreen(new BookViewScreen(new BookViewScreen.BookAccess(pages)));
	}
}
