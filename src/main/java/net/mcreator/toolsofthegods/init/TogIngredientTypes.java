package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.crafting.MaxProgressionToolIngredient;

public final class TogIngredientTypes {
	public static final DeferredRegister<IngredientType<?>> REGISTRY =
		DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, ToolsOfTheGodsMod.MODID);

	public static final DeferredHolder<IngredientType<?>, IngredientType<MaxProgressionToolIngredient>> MAX_PROGRESSION_TOOL =
		REGISTRY.register("max_progression_tool", () -> new IngredientType<>(MaxProgressionToolIngredient.CODEC));

	private TogIngredientTypes() {
	}
}
