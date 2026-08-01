package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record TogBlockDropsContext(
	Level level,
	BlockPos pos,
	BlockState state,
	Entity breaker,
	List<ItemEntity> drops,
	BlockEntity blockEntity
) {
}
