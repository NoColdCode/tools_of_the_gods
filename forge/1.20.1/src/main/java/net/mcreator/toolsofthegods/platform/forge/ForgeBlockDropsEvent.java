package net.mcreator.toolsofthegods.platform.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * Forge 1.20.1 polyfill for NeoForge's {@code BlockDropsEvent}, which is not available on legacy Forge.
 */
@Cancelable
public final class ForgeBlockDropsEvent extends Event {
	private final Level level;
	private final BlockPos pos;
	private final BlockState state;
	private final BlockEntity blockEntity;
	private final Entity breaker;
	private final ItemStack tool;
	private final List<ItemEntity> drops;

	public ForgeBlockDropsEvent(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity,
			Entity breaker, ItemStack tool, List<ItemEntity> drops) {
		this.level = level;
		this.pos = pos;
		this.state = state;
		this.blockEntity = blockEntity;
		this.breaker = breaker;
		this.tool = tool;
		this.drops = drops;
	}

	public Level getLevel() {
		return level;
	}

	public BlockPos getPos() {
		return pos;
	}

	public BlockState getState() {
		return state;
	}

	public BlockEntity getBlockEntity() {
		return blockEntity;
	}

	public Entity getBreaker() {
		return breaker;
	}

	public ItemStack getTool() {
		return tool;
	}

	public List<ItemEntity> getDrops() {
		return drops;
	}
}
