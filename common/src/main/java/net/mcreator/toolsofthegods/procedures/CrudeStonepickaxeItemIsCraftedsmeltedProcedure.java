package net.mcreator.toolsofthegods.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

public class CrudeStonepickaxeItemIsCraftedsmeltedProcedure {
	public static void execute(ItemStack itemstack) {
		{
			final String _tagName = "xp";
			final double _tagValue = 0;
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
		{
			final String _tagName = "level";
			final double _tagValue = 10;
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
		{
			final String _tagName = "tier";
			final double _tagValue = 2;
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
		{
			final String _tagName = "nexttier";
			final double _tagValue = 53;
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
	}
}