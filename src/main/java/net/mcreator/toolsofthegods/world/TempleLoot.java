package net.mcreator.toolsofthegods.world;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.core.BlockPos;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.ArrayList;
import java.util.List;

public final class TempleLoot {
	private record ToolEntry(Item item, ToolProgressionHelper.ToolType type, int minLevel, int maxLevel) {
	}

	private static final List<ToolEntry> TOOLS = List.of(
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get(), ToolProgressionHelper.ToolType.PICKAXE, 1, 25),
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get(), ToolProgressionHelper.ToolType.SHOVEL, 1, 25),
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get(), ToolProgressionHelper.ToolType.HOE, 1, 20),
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get(), ToolProgressionHelper.ToolType.AXE, 5, 35),
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get(), ToolProgressionHelper.ToolType.SWORD, 5, 35),
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get(), ToolProgressionHelper.ToolType.HAMMER, 15, 45),
		new ToolEntry(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get(), ToolProgressionHelper.ToolType.BOW, 15, 40),
		new ToolEntry(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get(), ToolProgressionHelper.ToolType.CROSSBOW, 15, 40),
		new ToolEntry(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get(), ToolProgressionHelper.ToolType.FISHING_ROD, 20, 50),
		new ToolEntry(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get(), ToolProgressionHelper.ToolType.TRIDENT, 25, 50),
		new ToolEntry(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get(), ToolProgressionHelper.ToolType.SPEAR, 20, 45),
		new ToolEntry(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get(), ToolProgressionHelper.ToolType.STAFF, 20, 50)
	);

	private TempleLoot() {
	}

	public static ItemStack randomLeveledTool(RandomSource random) {
		ToolEntry entry = TOOLS.get(random.nextInt(TOOLS.size()));
		int level = entry.minLevel + random.nextInt(Math.max(1, entry.maxLevel - entry.minLevel + 1));
		ItemStack stack = new ItemStack(entry.item);
		ToolProgressionHelper.initializeToolAtLevel(stack, entry.type, level);
		return stack;
	}

	public static ItemStack randomGem(RandomSource random) {
		Item[] gems = new Item[] {
			ToolsOfTheGodsOrbItems.WHITE_GEM.get(),
			ToolsOfTheGodsOrbItems.YELLOW_GEM.get(),
			ToolsOfTheGodsOrbItems.PURPLE_GEM.get(),
			ToolsOfTheGodsOrbItems.RED_GEM.get(),
			ToolsOfTheGodsOrbItems.BLACK_GEM.get(),
			ToolsOfTheGodsOrbItems.GREEN_GEM.get(),
			ToolsOfTheGodsOrbItems.BLUE_GEM.get(),
			ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get()
		};
		return new ItemStack(gems[random.nextInt(gems.length)]);
	}

	public static void fillChest(BlockEntity entity, RandomSource random, boolean epic) {
		if (!(entity instanceof RandomizableContainerBlockEntity chest)) {
			return;
		}
		List<ItemStack> loot = new ArrayList<>();
		loot.add(randomLeveledTool(random));
		if (epic) {
			loot.add(randomLeveledTool(random));
			loot.add(randomGem(random));
			if (random.nextFloat() < 0.35f) {
				loot.add(randomGem(random));
			}
		} else if (random.nextFloat() < 0.5f) {
			loot.add(randomGem(random));
		}
		loot.add(new ItemStack(Items.GOLD_INGOT, 2 + random.nextInt(4)));
		for (int i = 0; i < loot.size() && i < chest.getContainerSize(); i++) {
			chest.setItem(i, loot.get(i));
		}
	}
}
