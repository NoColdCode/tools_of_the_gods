package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record TogLeftClickBlockContext(
	Player player,
	Level level,
	ItemStack itemStack
) {
}
