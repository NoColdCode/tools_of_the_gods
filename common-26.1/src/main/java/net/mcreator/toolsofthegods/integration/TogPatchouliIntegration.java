package net.mcreator.toolsofthegods.integration;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.fml.ModList;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

/**
 * Opens the TOG Patchouli guide when Patchouli is installed (reflection avoids a hard compile dependency).
 */
public final class TogPatchouliIntegration {

	public static final Identifier BOOK_ID =
		Identifier.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "tog_guide");

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

	/** Opens the Patchouli book from the server (syncs to the client). */
	public static void openBook(ServerPlayer player) {
		if (!isAvailable()) {
			return;
		}
		try {
			getApi().getClass()
				.getMethod("openBookGUI", ServerPlayer.class, Identifier.class)
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
