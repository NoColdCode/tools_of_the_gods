package net.mcreator.toolsofthegods.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.item.TogGuideBookItem;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.TogFeatures;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

public final class TogCreativeTabHelper {
	private TogCreativeTabHelper() {
	}

	public static void populateMainTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
		output.accept(ToolsOfTheGodsModItems.TRAIT_SMITHING_TABLE.get());
		output.accept(TogGuideBookItem.createPopulatedStack());

		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get(), ToolType.PICKAXE);
		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HAMMER.get(), ToolType.HAMMER);
		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_AXE.get(), ToolType.AXE);
		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SHOVEL.get(), ToolType.SHOVEL);
		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_HOE.get(), ToolType.HOE);
		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_SWORD.get(), ToolType.SWORD);
		acceptCoreTool(output, ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get(), ToolType.BOW);

		acceptArmorPiece(output, ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_HELMET.get());
		acceptArmorPiece(output, ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_CHESTPLATE.get());
		acceptArmorPiece(output, ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_LEGGINGS.get());
		acceptArmorPiece(output, ToolsOfTheGodsModItems.ARMOR_OF_THE_GODS_BOOTS.get());
		acceptShieldPiece(output, ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get());

		if (TogFeatures.extendedToolsEnabled()) {
			output.accept(ToolsOfTheGodsModItems.FISHING_ROD_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.SPEAR_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.FLAIL_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get());
			output.accept(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
		}

		output.accept(ToolsOfTheGodsOrbItems.WHITE_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.YELLOW_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.PURPLE_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.RED_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.BLACK_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.GREEN_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.BLUE_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get());
		output.accept(ToolsOfTheGodsOrbItems.XP_INFUSION_MINOR.get());
		output.accept(ToolsOfTheGodsOrbItems.XP_INFUSION_MAJOR.get());
		output.accept(ToolsOfTheGodsOrbItems.XP_INFUSION_GRAND.get());
		output.accept(ToolsOfTheGodsOrbItems.ELIXIR_XP_FOCUS.get());
		output.accept(ToolsOfTheGodsOrbItems.ELIXIR_XP_SURGE.get());
		output.accept(ToolsOfTheGodsOrbItems.ELIXIR_XP_RAPTURE.get());
		output.accept(ToolsOfTheGodsOrbItems.ELIXIR_XP_APOTHEOSIS.get());
	}

	public static ItemStack createTierPreview(Item item, ToolType type, int tier) {
		int level = Math.min(TierSystem.MAX_LEVEL, tier * TierSystem.LEVELS_PER_TIER);
		ItemStack stack = new ItemStack(item);
		ToolProgressionHelper.initializeToolAtLevel(stack, type, level);
		return stack;
	}

	private static void acceptCoreTool(CreativeModeTab.Output output, Item item, ToolType type) {
		if (TogFeatures.creativeTierPreviewsEnabled()) {
			acceptTierPreviews(output, item, type);
		} else {
			output.accept(item);
		}
	}

	private static void acceptArmorPiece(CreativeModeTab.Output output, Item item) {
		if (TogFeatures.creativeTierPreviewsEnabled()) {
			acceptTierPreviews(output, item, ToolType.ARMOR);
		} else {
			output.accept(item);
		}
	}

	private static void acceptShieldPiece(CreativeModeTab.Output output, Item item) {
		if (TogFeatures.creativeTierPreviewsEnabled()) {
			acceptTierPreviews(output, item, ToolType.SHIELD);
		} else {
			output.accept(item);
		}
	}

	private static void acceptTierPreviews(CreativeModeTab.Output output, Item item, ToolType type) {
		ItemStack previous = null;
		for (int tier = 0; tier <= TierSystem.MAX_TIER; tier++) {
			ItemStack stack = createTierPreview(item, type, tier);
			if (previous != null && ItemStack.isSameItemSameComponents(previous, stack)) {
				continue;
			}
			output.accept(stack);
			previous = stack;
		}
	}
}
