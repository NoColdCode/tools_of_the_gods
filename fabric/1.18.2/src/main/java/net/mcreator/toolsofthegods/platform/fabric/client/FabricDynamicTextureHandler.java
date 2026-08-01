package net.mcreator.toolsofthegods.platform.fabric.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TogItemStackData;

public final class FabricDynamicTextureHandler {
	private FabricDynamicTextureHandler() {
	}

	public static void init() {
		ResourceLocation tierPredicate = new ResourceLocation(TogModConstants.MODID, "tier");
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get(), tierPredicate, FabricDynamicTextureHandler::tierProperty);
		ItemProperties.register(
			ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get(),
			new ResourceLocation("blocking"),
			(stack, level, entity, seed) -> entity != null
				&& entity.isUsingItem()
				&& entity.getUseItem() == stack ? 1.0F : 0.0F
		);
	}

	private static float tierProperty(net.minecraft.world.item.ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level,
		net.minecraft.world.entity.LivingEntity entity, int seed) {
		int tier = (int) TogItemStackData.getDouble(stack, "tier");
		return tier / 10.0f;
	}
}
