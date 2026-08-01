package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.neoforged.bus.api.IEventBus;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.crafting.MaxProgressionToolIngredient;

public final class NeoForgeIngredientTypes {
	private static final DeferredRegister<IngredientType<?>> REGISTRY =
		DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, TogModConstants.MODID);

	public static final DeferredHolder<IngredientType<?>, IngredientType<MaxProgressionToolIngredient>> MAX_PROGRESSION_TOOL =
		REGISTRY.register("max_progression_tool", () -> new IngredientType<>(MaxProgressionToolIngredient.CODEC));

	private NeoForgeIngredientTypes() {
	}

	public static void register(IEventBus modEventBus) {
		REGISTRY.register(modEventBus);
	}
}
