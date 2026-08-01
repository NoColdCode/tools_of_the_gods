package net.mcreator.toolsofthegods.crafting;

import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import net.mcreator.toolsofthegods.platform.forge.ForgeIngredientTypes;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.stream.Stream;

public final class MaxProgressionToolIngredient extends AbstractIngredient {
	private final Item item;

	public MaxProgressionToolIngredient(Item item) {
		super(Stream.of(new Ingredient.ItemValue(new ItemStack(item))));
		this.item = item;
	}

	public Item item() {
		return item;
	}

	public static Ingredient of(ItemLike itemLike) {
		return new MaxProgressionToolIngredient(itemLike.asItem());
	}

	@Override
	public boolean test(ItemStack stack) {
		return stack.is(item) && ToolProgressionHelper.isFullyProgressed(stack);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return ForgeIngredientTypes.MAX_PROGRESSION_TOOL;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("item", Registry.ITEM.getKey(item).toString());
		return json;
	}
}
