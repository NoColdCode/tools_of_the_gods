/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.mcreator.toolsofthegods.block.TraitSmithingTableBlockEntity;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public class ToolsOfTheGodsModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, ToolsOfTheGodsMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TraitSmithingTableBlockEntity>> TRAIT_SMITHING_TABLE = REGISTRY.register("trait_smithing_table",
			() -> BlockEntityType.Builder.of(TraitSmithingTableBlockEntity::new, ToolsOfTheGodsModBlocks.TRAIT_SMITHING_TABLE.get()).build(null));
}
