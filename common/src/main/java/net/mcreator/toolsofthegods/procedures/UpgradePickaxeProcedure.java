package net.mcreator.toolsofthegods.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.util.TogEntityInventoryHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TierSystem;

public class UpgradePickaxeProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		tryUpgrade(world, x, y, z, entity, itemstack);
	}

	/** Attempts a tier upgrade using materials from the entity's inventory (player or mob/citizen). */
	public static boolean tryUpgrade(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		return tryUpgrade(world, x, y, z, entity, itemstack, true);
	}

	public static boolean tryUpgrade(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack, boolean notifyPlayer) {
		if (entity == null) {
			return false;
		}

		ToolProgressionHelper.ensureInitialized(itemstack);

		int currentLevel = (int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level");
		int currentTier = (int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("tier");
		boolean needsUpgrade = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("needsUpgrade");

		if (!needsUpgrade) {
			return false;
		}

		int nextTier = currentTier + 1;
		ToolProgressionHelper.ToolType toolType = ToolProgressionHelper.getToolType(itemstack);
		boolean hasResources = switch (nextTier) {
			case 1 -> ToolProgressionHelper.usesArmorProgression(toolType)
				? TogEntityInventoryHelper.consume(entity, new ItemStack(Items.CHAIN), 8)
				: TogEntityInventoryHelper.consume(entity, new ItemStack(Items.COBBLESTONE), 32);
			case 2 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.WHITE_GEM.get()), 1);
			case 3 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.YELLOW_GEM.get()), 1);
			case 4 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.PURPLE_GEM.get()), 1);
			case 5 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.RED_GEM.get()), 1);
			case 6 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.BLACK_GEM.get()), 1);
			case 7 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.GREEN_GEM.get()), 1);
			case 8 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.BLUE_GEM.get()), 1);
			case 9 -> TogEntityInventoryHelper.consume(entity, new ItemStack(ToolsOfTheGodsOrbItems.UNIVERSE_GEM.get()), 1);
			default -> false;
		};

		if (hasResources) {
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> {
				tag.putDouble("tier", nextTier);
				tag.putBoolean("needsUpgrade", false);
				tag.putDouble("nexttier", ToolProgressionHelper.getXpForNextLevel(toolType, currentLevel));
			});

			if (world instanceof Level level && !level.isClientSide()) {
				level.playSound(null, BlockPos.containing(x, y, z),
					BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.anvil.use")),
					SoundSource.PLAYERS, 1.0f, 1.2f);
			}

			if (notifyPlayer && entity instanceof Player player) {
				String tierLabel;
				if (toolType == ToolProgressionHelper.ToolType.WINGS) {
					tierLabel = TierSystem.getWingsDisplayName(nextTier);
				} else if (ToolProgressionHelper.usesArmorProgression(toolType)) {
					tierLabel = TierSystem.getArmorTierName(nextTier);
				} else {
					tierLabel = TierSystem.getTierName(nextTier);
				}
				player.displayClientMessage(Component.literal("§a§lUPGRADED! §6" + tierLabel + " §aunlocked!"), false);
			}
			return true;
		}

		return false;
	}
}
