package net.mcreator.toolsofthegods.event;

import java.util.Optional;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitAutosmeltHandler {
	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		if (!(event.getLevel() instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (!(event.getBreaker() instanceof Player player)) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem) || !TraitSystem.isAutosmeltActive(heldItem)) {
			return;
		}

		autosmeltDrops(level, event.getDrops());
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		if (!(event.getEntity().level() instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (!(event.getSource().getEntity() instanceof Player player)) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem) || !TraitSystem.isAutosmeltActive(heldItem)) {
			return;
		}

		autosmeltDrops(level, event.getDrops());
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
		Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, level);
		if (recipe.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack result = recipe.get().value().assemble(input, level.registryAccess());
		if (result.isEmpty()) {
			return ItemStack.EMPTY;
		}

		int resultCount = result.getCount() * inputStack.getCount();
		result.setCount(resultCount);
		return result;
	}
}
