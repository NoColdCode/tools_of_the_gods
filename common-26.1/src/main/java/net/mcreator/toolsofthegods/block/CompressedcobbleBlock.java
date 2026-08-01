package net.mcreator.toolsofthegods.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class CompressedcobbleBlock extends Block {
	public CompressedcobbleBlock(ResourceKey<Block> blockId) {
		super(BlockBehaviour.Properties.of().setId(blockId).strength(5f, 25f));
	}
}