package net.mcreator.toolsofthegods.integration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.fml.ModList;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public final class TogPatchouliIntegration {

	public static final ResourceLocation BOOK_ID =
		new ResourceLocation(ToolsOfTheGodsMod.MODID, "tog_guide");

	private TogPatchouliIntegration() {
	}

	public static boolean isLoaded() {
		return ModList.get().isLoaded("patchouli");
	}

	public static boolean isAvailable() {
		if (!isLoaded()) {
			return false;
		}
		try {
			Object api = getApi();
			return api != null && !(boolean) api.getClass().getMethod("isStub").invoke(api);
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}

	public static void openBook(ServerPlayer player) {
		if (!isAvailable()) {
			return;
		}
		try {
			getApi().getClass()
				.getMethod("openBookGUI", ServerPlayer.class, ResourceLocation.class)
				.invoke(getApi(), player, BOOK_ID);
		} catch (ReflectiveOperationException e) {
			ToolsOfTheGodsMod.LOGGER.warn("Failed to open Patchouli guide book", e);
		}
	}

	private static Object getApi() throws ReflectiveOperationException {
		Class<?> apiClass = Class.forName("vazkii.patchouli.api.PatchouliAPI");
		return apiClass.getMethod("get").invoke(null);
	}
}
