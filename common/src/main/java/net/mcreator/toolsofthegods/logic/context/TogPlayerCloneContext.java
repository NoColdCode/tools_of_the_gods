package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.entity.player.Player;

public record TogPlayerCloneContext(
	Player original,
	Player newPlayer,
	boolean wasDeath
) {
}
