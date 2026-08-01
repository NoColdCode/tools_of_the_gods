package net.mcreator.toolsofthegods.platform.forge;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.crafting.MaxProgressionToolIngredient;

public final class ForgeIngredientTypes {
	public static final IIngredientSerializer<MaxProgressionToolIngredient> MAX_PROGRESSION_TOOL =
		new IIngredientSerializer<>() {
			@Override
			public MaxProgressionToolIngredient parse(FriendlyByteBuf buffer) {
				ResourceLocation id = buffer.readResourceLocation();
				Item item = Registry.ITEM.get(id);
				return new MaxProgressionToolIngredient(item);
			}

			@Override
			public MaxProgressionToolIngredient parse(JsonObject json) {
				ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "item"));
				Item item = Registry.ITEM.get(id);
				return new MaxProgressionToolIngredient(item);
			}

			@Override
			public void write(FriendlyByteBuf buffer, MaxProgressionToolIngredient ingredient) {
				buffer.writeResourceLocation(Registry.ITEM.getKey(ingredient.item()));
			}
		};

	private ForgeIngredientTypes() {
	}

	public static void register(IEventBus modEventBus) {
		modEventBus.addListener(ForgeIngredientTypes::onCommonSetup);
	}

	private static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> CraftingHelper.register(
			new ResourceLocation(TogModConstants.MODID, "max_progression_tool"),
			MAX_PROGRESSION_TOOL));
	}
}
