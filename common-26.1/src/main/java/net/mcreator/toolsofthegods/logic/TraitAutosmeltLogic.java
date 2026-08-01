package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;

import java.util.Optional;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public final class TraitAutosmeltLogic {
	public static void onBlockDrops(TogBlockDropsContext ctx) {
		if (!(ctx.level() instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (!(ctx.breaker() instanceof Player player)) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem) || !TraitSystem.isAutosmeltActive(heldItem)) {
			return;
		}

		autosmeltDrops(level, ctx.drops());
	}

	public static void onLivingDrops(TogLivingDropsContext ctx) {
		if (!(ctx.entity().level() instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (!(ctx.source().getEntity() instanceof Player player)) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem) || !TraitSystem.isAutosmeltActive(heldItem)) {
			return;
		}

		autosmeltDrops(level, ctx.drops());
	}

	private static void autosmeltDrops(Level level, Iterable<ItemEntity> drops) {
		for (ItemEntity dropEntity : drops) {
			ItemStack input = dropEntity.getItem();
			ItemStack smelted = getSmeltingResult(level, input);
			if (smelted.isEmpty()) {
				continue;
			}
			dropEntity.setItem(smelted);
		}
	}

	private static ItemStack getSmeltingResult(Level level, ItemStack inputStack) {
		if (inputStack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack singleInput = inputStack.copyWithCount(1);
		SingleRecipeInput input = new SingleRecipeInput(singleInput);
		Optional<RecipeHolder<SmeltingRecipe>> recipe = ((ServerLevel) level).recipeAccess().getRecipeFor(RecipeType.SMELTING, input, level);
		if (recipe.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack result = recipe.get().value().assemble(input);
		if (result.isEmpty()) {
			return ItemStack.EMPTY;
		}

		int resultCount = result.getCount() * inputStack.getCount();
		result.setCount(resultCount);
		return result;
	}
}
