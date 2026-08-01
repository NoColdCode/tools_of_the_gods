package net.mcreator.toolsofthegods.world;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TempleOfTheGodsFeature extends Feature<NoneFeatureConfiguration> {
	public TempleOfTheGodsFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		var random = context.random();

		int surfaceY = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
		if (surfaceY < level.getMinY() + 8) {
			return false;
		}
		BlockPos base = new BlockPos(origin.getX(), surfaceY - 1, origin.getZ());

		int radius = 5;
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				for (int dy = 0; dy <= 6; dy++) {
					BlockPos pos = base.offset(dx, dy, dz);
					boolean wall = Math.abs(dx) == radius || Math.abs(dz) == radius;
					boolean floor = dy == 0;
					boolean ceiling = dy == 6;
					if (floor) {
						level.setBlock(pos, Blocks.QUARTZ_BLOCK.defaultBlockState(), 2);
					} else if (wall || ceiling) {
						level.setBlock(pos, Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState(), 2);
					} else {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}

		// Pillars
		for (int corner = 0; corner < 4; corner++) {
			int px = corner < 2 ? -radius + 1 : radius - 1;
			int pz = (corner % 2 == 0) ? -radius + 1 : radius - 1;
			for (int py = 1; py <= 5; py++) {
				level.setBlock(base.offset(px, py, pz), Blocks.QUARTZ_PILLAR.defaultBlockState(), 2);
			}
		}

		placeChest(level, base.offset(0, 1, 0), random, true);
		placeChest(level, base.offset(-3, 1, 0), random, false);
		placeChest(level, base.offset(3, 1, 0), random, false);

		level.setBlock(base.offset(0, 1, -2), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
		level.setBlock(base.offset(0, 2, -2), Blocks.LANTERN.defaultBlockState(), 2);

		return true;
	}

	private static void placeChest(WorldGenLevel level, BlockPos pos, net.minecraft.util.RandomSource random, boolean epic) {
		BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, net.minecraft.core.Direction.SOUTH);
		level.setBlock(pos, chestState, 2);
		BlockEntity be = level.getBlockEntity(pos);
		if (be != null) {
			TempleLoot.fillChest(be, random, epic);
		}
	}
}
