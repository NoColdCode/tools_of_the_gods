package net.mcreator.toolsofthegods.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.BlockPos;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlockEntities;

public class TraitSmithingTableBlockEntity extends BlockEntity {
	private static final int CONTAINER_SIZE = 2;
	private final Container inventory = new SimpleContainer(CONTAINER_SIZE);

	public TraitSmithingTableBlockEntity(BlockPos pos, BlockState blockState) {
		super(ToolsOfTheGodsModBlockEntities.TRAIT_SMITHING_TABLE.get(), pos, blockState);
	}

	public Container getInventory() {
		return inventory;
	}
}
