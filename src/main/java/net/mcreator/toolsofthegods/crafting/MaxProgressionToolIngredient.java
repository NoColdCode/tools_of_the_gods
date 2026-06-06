package net.mcreator.toolsofthegods.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import net.mcreator.toolsofthegods.init.TogIngredientTypes;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.stream.Stream;

/**
 * Crafting ingredient that only matches a TOG tool at max tier and max level with no pending upgrade.
 */
public record MaxProgressionToolIngredient(Holder<Item> item) implements ICustomIngredient {
	public static final MapCodec<MaxProgressionToolIngredient> CODEC = RecordCodecBuilder.mapCodec(builder -> builder
		.group(BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(MaxProgressionToolIngredient::item))
		.apply(builder, MaxProgressionToolIngredient::new));

	public static Ingredient of(ItemLike item) {
		return new MaxProgressionToolIngredient(item.asItem().builtInRegistryHolder()).toVanilla();
	}

	@Override
	public boolean test(ItemStack stack) {
		return stack.is(item) && ToolProgressionHelper.isFullyProgressed(stack);
	}

	@Override
	public Stream<ItemStack> getItems() {
		return Stream.of(new ItemStack(item));
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IngredientType<?> getType() {
		return TogIngredientTypes.MAX_PROGRESSION_TOOL.get();
	}
}
