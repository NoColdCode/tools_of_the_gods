package net.mcreator.toolsofthegods.logic;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import com.mojang.brigadier.CommandDispatcher;

public final class PickaxeCommandsLogic {
	private PickaxeCommandsLogic() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("tog")
			.requires(cs -> true)
			.then(Commands.literal("setlevel")
				.then(Commands.argument("level", IntegerArgumentType.integer(0, TierSystem.MAX_LEVEL))
					.executes(context -> {
						var player = context.getSource().getPlayerOrException();
						if (!player.getAbilities().instabuild) {
							context.getSource().sendFailure(Component.literal("§cCreative mode required."));
							return 0;
						}
						int level = IntegerArgumentType.getInteger(context, "level");

						ItemStack heldItem = player.getMainHandItem();
						ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(heldItem);
						if (type != ToolProgressionHelper.ToolType.NONE) {
							int max = ToolProgressionHelper.getMaxLevel(type);
							int adjustedLevel = Math.min(level, max);
							boolean needsUpgrade = ToolProgressionHelper.isBoundary(type, adjustedLevel);
							int tier = ToolProgressionHelper.getTier(type, adjustedLevel);
							if (needsUpgrade) {
								tier = Math.max(0, tier - 1);
							}
							final int finalTier = tier;
							int xpNeeded = ToolProgressionHelper.getXpForNextLevel(type, adjustedLevel);

							CustomData.update(DataComponents.CUSTOM_DATA, heldItem, tag -> {
								tag.putDouble("level", adjustedLevel);
								tag.putDouble("tier", finalTier);
								tag.putDouble("xp", 0);
								tag.putDouble("nexttier", xpNeeded);
								tag.putBoolean("needsUpgrade", needsUpgrade);
							});

							context.getSource().sendSuccess(() ->
								Component.literal("§aSet tool level to " + adjustedLevel + " (Tier " + finalTier + ")"), true);
							return 1;
						}
						context.getSource().sendFailure(Component.literal("§cYou must hold a TOG tool."));
						return 0;
					})
				)
			)
			.then(Commands.literal("givebow")
				.then(Commands.argument("level", IntegerArgumentType.integer(0, 50))
					.executes(context -> {
						var player = context.getSource().getPlayerOrException();
						int level = IntegerArgumentType.getInteger(context, "level");
						ItemStack bow = new ItemStack(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_BOW.get());
						ToolProgressionHelper.initializeTool(bow, ToolProgressionHelper.ToolType.BOW);
						boolean needsUpgrade = ToolProgressionHelper.isBoundary(ToolProgressionHelper.ToolType.BOW, level);
						int tier = ToolProgressionHelper.getTier(ToolProgressionHelper.ToolType.BOW, level);
						if (needsUpgrade) {
							tier = Math.max(0, tier - 1);
						}
						final int finalTier = tier;
						CustomData.update(DataComponents.CUSTOM_DATA, bow, tag -> {
							tag.putDouble("level", level);
							tag.putDouble("tier", finalTier);
							tag.putDouble("nexttier", ToolProgressionHelper.getXpForNextLevel(ToolProgressionHelper.ToolType.BOW, level));
							tag.putBoolean("needsUpgrade", needsUpgrade);
						});
						player.addItem(bow);
						context.getSource().sendSuccess(() -> Component.literal("§aGave TOG bow at level " + level), true);
						return 1;
					})
				)
			)
			.then(Commands.literal("givepickaxe")
				.then(Commands.argument("level", IntegerArgumentType.integer(0, TierSystem.MAX_LEVEL))
					.executes(context -> {
						var player = context.getSource().getPlayerOrException();
						if (!player.getAbilities().instabuild) {
							context.getSource().sendFailure(Component.literal("§cCreative mode required."));
							return 0;
						}
						int level = IntegerArgumentType.getInteger(context, "level");

						ItemStack pickaxe = new ItemStack(ToolsOfTheGodsModItems.PRIMAL_WOODEN_TOOLS_PICKAXE.get());
						ToolProgressionHelper.initializeTool(pickaxe, ToolProgressionHelper.ToolType.PICKAXE);
						boolean needsUpgrade = ToolProgressionHelper.isBoundary(ToolProgressionHelper.ToolType.PICKAXE, level);
						int tier = ToolProgressionHelper.getTier(ToolProgressionHelper.ToolType.PICKAXE, level);
						if (needsUpgrade) {
							tier = Math.max(0, tier - 1);
						}
						final int finalTier = tier;
						int xpNeeded = ToolProgressionHelper.getXpForNextLevel(ToolProgressionHelper.ToolType.PICKAXE, level);

						CustomData.update(DataComponents.CUSTOM_DATA, pickaxe, tag -> {
							tag.putDouble("level", level);
							tag.putDouble("tier", finalTier);
							tag.putDouble("xp", 0);
							tag.putDouble("nexttier", xpNeeded);
							tag.putBoolean("needsUpgrade", needsUpgrade);
						});

						player.addItem(pickaxe);

						context.getSource().sendSuccess(() ->
							Component.literal("§aGave pickaxe at level " + level), true);
						return 1;
					})
				)
			)
			.then(Commands.literal("addxp")
				.then(Commands.argument("amount", IntegerArgumentType.integer(1, 10000))
					.executes(context -> {
						var player = context.getSource().getPlayerOrException();
						if (!player.getAbilities().instabuild) {
							context.getSource().sendFailure(Component.literal("§cCreative mode required."));
							return 0;
						}
						int amount = IntegerArgumentType.getInteger(context, "amount");

						ItemStack heldItem = player.getMainHandItem();
						if (ToolProgressionHelper.isTogTool(heldItem)) {
							int currentXp = (int) heldItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
								.copyTag().getDouble("xp");

							CustomData.update(DataComponents.CUSTOM_DATA, heldItem, tag -> tag.putDouble("xp", currentXp + amount));

							context.getSource().sendSuccess(() ->
								Component.literal("§aAdded " + amount + " XP to tool"), true);
							return 1;
						}
						context.getSource().sendFailure(Component.literal("§cYou must hold a TOG tool."));
						return 0;
					})
				)
			)
			.then(Commands.literal("info")
				.executes(context -> {
					var player = context.getSource().getPlayerOrException();
					ItemStack heldItem = player.getMainHandItem();

					if (ToolProgressionHelper.isTogTool(heldItem)) {
						int level = (int) heldItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
							.copyTag().getDouble("level");
						int tier = (int) heldItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
							.copyTag().getDouble("tier");
						int xp = (int) heldItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
							.copyTag().getDouble("xp");
						int xpNeeded = (int) heldItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
							.copyTag().getDouble("nexttier");
						ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(heldItem);
						float speed = ToolProgressionHelper.getEffectiveMiningSpeed(heldItem);

						context.getSource().sendSuccess(() -> Component.literal(
							"§6=== Pickaxe Info ===\n" +
								"§7Tool: §f" + type + "\n" +
								"§7Level: §f" + level + "\n" +
								"§7Tier: §f" + tier + " (" + TierSystem.getTierName(tier) + ")\n" +
								"§7XP: §f" + xp + " / " + xpNeeded + "\n" +
								"§7Mining Speed: §f" + String.format("%.2fx", speed)
						), false);
						return 1;
					}
					context.getSource().sendFailure(Component.literal("§cYou must hold a TOG tool."));
					return 0;
				})
			)
		);
	}
}
