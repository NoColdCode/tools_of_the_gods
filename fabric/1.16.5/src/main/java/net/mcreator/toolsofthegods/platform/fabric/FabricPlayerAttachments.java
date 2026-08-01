package net.mcreator.toolsofthegods.platform.fabric;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory player NBT for 1.16.5 Fabric (no attachment API). */
public final class FabricPlayerAttachments {
	private static final Map<UUID, CompoundTag> STORE = new ConcurrentHashMap<>();

	private FabricPlayerAttachments() {
	}

	public static CompoundTag get(Player player) {
		return STORE.computeIfAbsent(player.getUUID(), ignored -> new CompoundTag());
	}

	public static void copyOnClone(Player oldPlayer, Player newPlayer) {
		CompoundTag data = STORE.get(oldPlayer.getUUID());
		if (data != null) {
			STORE.put(newPlayer.getUUID(), data.copy());
		}
	}

	public static void remove(UUID uuid) {
		STORE.remove(uuid);
	}
}
