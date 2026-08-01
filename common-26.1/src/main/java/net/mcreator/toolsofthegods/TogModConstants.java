package net.mcreator.toolsofthegods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Loader-neutral mod metadata used by NeoForge, Fabric, and shared code. */
public final class TogModConstants {
	public static final Logger LOGGER = LogManager.getLogger(TogModConstants.class);
	public static final String MODID = "tools_of_the_gods";
	public static final String VERSION = "1.4.1";

	public static String getDisplayVersion() {
		return VERSION;
	}

	private TogModConstants() {
	}
}
