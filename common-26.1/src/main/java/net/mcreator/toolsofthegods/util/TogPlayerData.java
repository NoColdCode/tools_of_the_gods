package net.mcreator.toolsofthegods.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import net.mcreator.toolsofthegods.platform.TogPlatforms;

public final class TogPlayerData {
	private TogPlayerData() {
	}

	public static CompoundTag get(Player player) {
		return TogPlatforms.get().getPersistentData(player);
	}
}
