package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class TogRightClickItemContext {
	private final Player player;
	private final Level level;
	private final ItemStack itemStack;
	private boolean canceled;
	private InteractionResult cancellationResult = InteractionResult.PASS;

	public TogRightClickItemContext(Player player, Level level, ItemStack itemStack) {
		this.player = player;
		this.level = level;
		this.itemStack = itemStack;
	}

	public Player player() {
		return player;
	}

	public Level level() {
		return level;
	}

	public ItemStack itemStack() {
		return itemStack;
	}

	public boolean canceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public InteractionResult cancellationResult() {
		return cancellationResult;
	}

	public void setCancellationResult(InteractionResult cancellationResult) {
		this.cancellationResult = cancellationResult;
	}
}
