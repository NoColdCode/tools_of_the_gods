package net.mcreator.toolsofthegods.logic.context;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record TogItemFishedContext(
	Player player,
	List<ItemStack> drops
) {
}
