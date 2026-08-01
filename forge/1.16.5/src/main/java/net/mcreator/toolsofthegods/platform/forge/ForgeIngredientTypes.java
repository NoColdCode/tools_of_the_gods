package net.mcreator.toolsofthegods.platform.forge;

import net.minecraftforge.common.crafting.IngredientType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.NeoForgeRegistries;

import net.minecraftforge.eventbus.api.IEventBus;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.crafting.MaxProgressionToolIngredient;

public final class ForgeIngredientTypes {
	private static final DeferredRegister<IngredientType<?>> REGISTRY =
		DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, TogModConstants.MODID);

	public static final RegistryObject<IngredientType<?>, IngredientType<MaxProgressionToolIngredient>> MAX_PROGRESSION_TOOL =
		REGISTRY.register("max_progression_tool", () -> new IngredientType<>(MaxProgressionToolIngredient.CODEC));

	private ForgeIngredientTypes() {
	}

	public static void register(IEventBus modEventBus) {
		REGISTRY.register(modEventBus);
	}
}
