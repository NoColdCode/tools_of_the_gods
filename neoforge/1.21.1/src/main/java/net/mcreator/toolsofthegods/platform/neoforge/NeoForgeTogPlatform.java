package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;

import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.common.util.FakePlayer;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.platform.TogPlatform;

public final class NeoForgeTogPlatform implements TogPlatform {
	@Override
	public String modId() {
		return ToolsOfTheGodsMod.MODID;
	}

	@Override
	public String loaderName() {
		return "neoforge";
	}

	@Override
	public String minecraftVersion() {
		return "1.21.1";
	}

	@Override
	public boolean isClient() {
		return FMLEnvironment.dist == Dist.CLIENT;
	}

	@Override
	public boolean isDedicatedServer() {
		return FMLLoader.getDist() == Dist.DEDICATED_SERVER;
	}

	@Override
	public boolean isAutomatedPlayer(Player player) {
		return player instanceof FakePlayer;
	}

	@Override
	public net.minecraft.nbt.CompoundTag getPersistentData(Player player) {
		return player.getPersistentData();
	}
}
