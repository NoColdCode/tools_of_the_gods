package net.mcreator.toolsofthegods.client;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public class DynamicTextureHandler {
	
	@OnlyIn(Dist.CLIENT)
	public static void init() {
		var tierPredicate = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "tier");
		net.minecraft.client.renderer.item.ItemPropertyFunction tierFn = DynamicTextureHandler::tierProperty;
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get(), tierPredicate, tierFn);
		ItemProperties.register(ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get(), tierPredicate, tierFn);
		ItemProperties.register(
			ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get(),
			ResourceLocation.withDefaultNamespace("blocking"),
			(stack, level, entity, seed) -> entity instanceof net.minecraft.world.entity.LivingEntity living
				&& living.isUsingItem()
				&& living.getUseItem() == stack ? 1.0F : 0.0F
		);
	}

	@OnlyIn(Dist.CLIENT)
	private static float tierProperty(net.minecraft.world.item.ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level,
		net.minecraft.world.entity.LivingEntity entity, int seed) {
		return ToolProgressionHelper.getStoredTier(stack) / 10.0f;
	}
}
