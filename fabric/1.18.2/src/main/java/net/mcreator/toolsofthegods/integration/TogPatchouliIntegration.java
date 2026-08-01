package net.mcreator.toolsofthegods.integration;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.toolsofthegods.TogModConstants;

/**
 * Opens the TOG Patchouli guide when Patchouli is installed (reflection avoids a hard compile dependency).
 */
public final class TogPatchouliIntegration {

	public static final ResourceLocation BOOK_ID =
		new ResourceLocation(TogModConstants.MODID, "tog_guide");

	private TogPatchouliIntegration() {
	}

	public static boolean isLoaded() {
		return FabricLoader.getInstance().isModLoaded("patchouli");
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
			TogModConstants.LOGGER.warn("Failed to open Patchouli guide book", e);
		}
	}

	private static Object getApi() throws ReflectiveOperationException {
		Class<?> apiClass = Class.forName("vazkii.patchouli.api.PatchouliAPI");
		return apiClass.getMethod("get").invoke(null);
	}
}
