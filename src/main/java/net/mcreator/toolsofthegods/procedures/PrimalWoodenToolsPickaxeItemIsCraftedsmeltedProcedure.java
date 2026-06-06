package net.mcreator.toolsofthegods.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public class PrimalWoodenToolsPickaxeItemIsCraftedsmeltedProcedure {
	public static void execute(ItemStack itemstack) {
		ToolProgressionHelper.initializeTool(itemstack, ToolProgressionHelper.ToolType.PICKAXE);
	}
}