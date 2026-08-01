package net.mcreator.toolsofthegods.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import java.util.function.Supplier;

public enum UltimateToolMode {
	PICKAXE("Pickaxe", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE),
	HAMMER("Hammer", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER),
	AXE("Axe", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE),
	SHOVEL("Shovel", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL),
	HOE("Hoe", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE),
	SWORD("Sword", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD),
	BOW("Bow", ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW),
	CROSSBOW("Crossbow", ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS),
	TRIDENT("Trident", ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS),
	STAFF("Staff", ToolsOfTheGodsModItems.STAFF_OF_THE_GODS),
	FISHING_ROD("Fishing Rod", ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS);

	private final String displayName;
	private final Supplier<Item> iconItem;

	UltimateToolMode(String displayName, Supplier<Item> iconItem) {
		this.displayName = displayName;
		this.iconItem = iconItem;
	}

	public String displayName() {
		return displayName;
	}

	public ItemStack createIconStack() {
		ItemStack stack = new ItemStack(iconItem.get());
		ToolProgressionHelper.initializeTool(stack, mapToToolType());
		return stack;
	}

	public ToolProgressionHelper.ToolType mapToToolType() {
		return switch (this) {
			case PICKAXE -> ToolProgressionHelper.ToolType.PICKAXE;
			case HAMMER -> ToolProgressionHelper.ToolType.HAMMER;
			case AXE -> ToolProgressionHelper.ToolType.AXE;
			case SHOVEL -> ToolProgressionHelper.ToolType.SHOVEL;
			case HOE -> ToolProgressionHelper.ToolType.HOE;
			case SWORD -> ToolProgressionHelper.ToolType.SWORD;
			case BOW -> ToolProgressionHelper.ToolType.BOW;
			case CROSSBOW -> ToolProgressionHelper.ToolType.CROSSBOW;
			case TRIDENT -> ToolProgressionHelper.ToolType.TRIDENT;
			case STAFF -> ToolProgressionHelper.ToolType.STAFF;
			case FISHING_ROD -> ToolProgressionHelper.ToolType.FISHING_ROD;
		};
	}

	public TagKey<Block> primaryMineableTag() {
		return switch (this) {
			case PICKAXE, HAMMER -> BlockTags.MINEABLE_WITH_PICKAXE;
			case AXE -> BlockTags.MINEABLE_WITH_AXE;
			case SHOVEL -> BlockTags.MINEABLE_WITH_SHOVEL;
			case HOE -> BlockTags.MINEABLE_WITH_HOE;
			default -> null;
		};
	}

	public boolean canHarvest(BlockState state) {
		TagKey<Block> tag = primaryMineableTag();
		if (tag != null && state.is(tag)) {
			return true;
		}
		return switch (this) {
			case SWORD, BOW, CROSSBOW, TRIDENT, STAFF, FISHING_ROD -> false;
			case PICKAXE, HAMMER -> state.is(BlockTags.MINEABLE_WITH_PICKAXE)
				|| state.is(BlockTags.MINEABLE_WITH_AXE)
				|| state.is(BlockTags.MINEABLE_WITH_SHOVEL)
				|| state.is(BlockTags.MINEABLE_WITH_HOE);
			default -> false;
		};
	}

	public static UltimateToolMode fromOrdinal(int ordinal) {
		UltimateToolMode[] values = values();
		if (ordinal < 0 || ordinal >= values.length) {
			return PICKAXE;
		}
		return values[ordinal];
	}
}
