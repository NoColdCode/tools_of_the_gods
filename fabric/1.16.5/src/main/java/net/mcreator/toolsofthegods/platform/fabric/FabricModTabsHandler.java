package net.mcreator.toolsofthegods.platform.fabric;

/** Vanilla creative tab injection is not available on Fabric API 0.42 (1.16.5); custom tab is registered in FabricModContent. */
public final class FabricModTabsHandler {
	private FabricModTabsHandler() {
	}

	public static void register() {
		// ItemGroupEvents was added in later Fabric API versions.
	}
}
