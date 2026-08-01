package net.mcreator.toolsofthegods.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlockEntities;
import net.mcreator.toolsofthegods.network.TraitSmithingTableMenu;

public class TraitSmithingTableBlock extends Block implements EntityBlock {
	public TraitSmithingTableBlock() {
		super(BlockBehaviour.Properties.of().strength(5f, 10f).noOcclusion());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TraitSmithingTableBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return null;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level level, BlockPos pos, Player entity, BlockHitResult hit) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof TraitSmithingTableBlockEntity blockEntity) {
			entity.openMenu(new SimpleMenuProvider((windowId, inventory, player) -> new TraitSmithingTableMenu(windowId, inventory, blockEntity.getInventory()),
					Component.literal("Trait Smithing Table")));
		}
		return InteractionResult.SUCCESS;
	}
}
