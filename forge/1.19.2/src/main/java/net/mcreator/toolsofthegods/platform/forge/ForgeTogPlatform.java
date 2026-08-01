package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import net.minecraft.world.entity.player.Player;

import net.minecraftforge.common.util.FakePlayer;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.platform.TogPlatform;

public final class ForgeTogPlatform implements TogPlatform {
	@Override
	public String modId() {
		return ToolsOfTheGodsMod.MODID;
	}

	@Override
	public String loaderName() {
		return "forge";
	}

	@Override
	public String minecraftVersion() {
		return "1.19.2";
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
