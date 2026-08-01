package net.mcreator.toolsofthegods.platform;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

/**
 * Loader- and version-specific services. Implemented in each platform module
 * (e.g. {@code neoforge/1.21.1}) and registered at mod startup.
 */
public interface TogPlatform {
	String modId();

	String loaderName();

	String minecraftVersion();

	boolean isClient();

	boolean isDedicatedServer();

	/** True for fake players / colony NPCs that should use mob progression paths. */
	boolean isAutomatedPlayer(Player player);

	/** Loader-specific player NBT that survives death (NeoForge persistent data, Fabric attachment). */
	CompoundTag getPersistentData(Player player);
}
