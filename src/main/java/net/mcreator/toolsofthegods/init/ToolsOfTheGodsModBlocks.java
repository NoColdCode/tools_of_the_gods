/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.toolsofthegods.block.CompressedcobbleBlock;
import net.mcreator.toolsofthegods.block.TraitSmithingTableBlock;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public class ToolsOfTheGodsModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(ToolsOfTheGodsMod.MODID);
	public static final DeferredBlock<Block> COMPRESSEDCOBBLE;
	public static final DeferredBlock<Block> TRAIT_SMITHING_TABLE;
	static {
		COMPRESSEDCOBBLE = REGISTRY.register("compressedcobble", CompressedcobbleBlock::new);
		TRAIT_SMITHING_TABLE = REGISTRY.register("trait_smithing_table", TraitSmithingTableBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}