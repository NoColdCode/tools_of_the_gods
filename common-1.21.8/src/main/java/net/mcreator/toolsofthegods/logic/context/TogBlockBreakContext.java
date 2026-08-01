package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record TogBlockBreakContext(
	Level level,
	BlockPos pos,
	BlockState state,
	Player player
) {
}
