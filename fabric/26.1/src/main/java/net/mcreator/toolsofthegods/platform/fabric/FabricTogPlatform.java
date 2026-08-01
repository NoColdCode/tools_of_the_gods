package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.world.entity.player.Player;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.platform.TogPlatform;
import net.mcreator.toolsofthegods.platform.fabric.FabricPlayerAttachments;

public final class FabricTogPlatform implements TogPlatform {
	@Override
	public String modId() {
		return TogModConstants.MODID;
	}

	@Override
	public String loaderName() {
		return "fabric";
	}

	@Override
	public String minecraftVersion() {
		return "1.21.1";
	}

	@Override
	public boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public boolean isDedicatedServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	@Override
	public boolean isAutomatedPlayer(Player player) {
		return player.getClass().getSimpleName().contains("FakePlayer");
	}

	@Override
	public net.minecraft.nbt.CompoundTag getPersistentData(Player player) {
		return FabricPlayerAttachments.get(player);
	}
}
