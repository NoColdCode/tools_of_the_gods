package net.mcreator.toolsofthegods.platform;

/**
 * Holds the active platform implementation for the running game instance.
 */
public final class TogPlatforms {
	private static TogPlatform current;

	private TogPlatforms() {
	}

	public static void init(TogPlatform platform) {
		if (current != null) {
			throw new IllegalStateException("TogPlatform already initialized");
		}
		current = platform;
	}

	public static TogPlatform get() {
		if (current == null) {
			throw new IllegalStateException("TogPlatform not initialized");
		}
		return current;
	}

	public static boolean isInitialized() {
		return current != null;
	}
}
