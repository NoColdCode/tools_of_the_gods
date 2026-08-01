package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class TogBreakSpeedContext {
	private final Player player;
	private final BlockState state;
	private final Optional<BlockPos> position;
	private final float originalSpeed;
	private float newSpeed;

	public TogBreakSpeedContext(Player player, BlockState state, Optional<BlockPos> position, float originalSpeed) {
		this.player = player;
		this.state = state;
		this.position = position;
		this.originalSpeed = originalSpeed;
		this.newSpeed = originalSpeed;
	}

	public Player player() {
		return player;
	}

	public BlockState state() {
		return state;
	}

	public Optional<BlockPos> position() {
		return position;
	}

	public float originalSpeed() {
		return originalSpeed;
	}

	public float newSpeed() {
		return newSpeed;
	}

	public void setNewSpeed(float newSpeed) {
		this.newSpeed = newSpeed;
	}
}
