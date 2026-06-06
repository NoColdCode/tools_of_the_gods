package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitSustainingHandler {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide() || player.isCreative() || player.isSpectator()) {
			return;
		}

		ItemStack sustainingSource = TogEquipmentHelper.getBestSustainingSource(player);
		if (sustainingSource.isEmpty() || TraitSystem.getLootPenalty(sustainingSource) <= 0.0f) {
			return;
		}

		boolean fromWornArmor = ToolProgressionHelper.getToolType(sustainingSource) == ToolProgressionHelper.ToolType.ARMOR;
		int intervalMultiplier = fromWornArmor ? 2 : 1;

		long gameTime = player.level().getGameTime();
		FoodData foodData = player.getFoodData();

		int foodInterval = TraitSystem.getSustainingFoodIntervalTicks(sustainingSource) * intervalMultiplier;
		int foodAmount = TraitSystem.getSustainingFoodAmount(sustainingSource);
		if (foodInterval > 0 && foodAmount > 0 && gameTime % foodInterval == 0 && foodData.getFoodLevel() < 20) {
			foodData.eat(foodAmount, 0.0f);
		}

		int saturationInterval = TraitSystem.getSustainingSaturationIntervalTicks(sustainingSource) * intervalMultiplier;
		float saturationAmount = TraitSystem.getSustainingSaturationAmount(sustainingSource);
		if (saturationInterval > 0 && saturationAmount > 0.0f && gameTime % saturationInterval == 0) {
			foodData.setSaturation(Math.min(foodData.getFoodLevel(), foodData.getSaturationLevel() + saturationAmount));
		}
	}

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		if (!(event.getLevel() instanceof Level level) || level.isClientSide()) {
			return;
		}
		if (!(event.getBreaker() instanceof Player player)) {
			return;
		}
		applyLootPenalty(player, level, event.getDrops());
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		if (!(event.getEntity().level() instanceof Level level) || level.isClientSide()) {
			return;
		}
		if (!(event.getSource().getEntity() instanceof Player player)) {
			return;
		}
		applyLootPenalty(player, level, event.getDrops());
	}

	private static void applyLootPenalty(Player player, Level level, Iterable<ItemEntity> drops) {
		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem)) {
			heldItem = TogEquipmentHelper.getBestSustainingSource(player);
		}
		applyLootPenalty(heldItem, level, drops);
	}

	private static void applyLootPenalty(ItemStack heldItem, Level level, Iterable<ItemEntity> drops) {
		if (!ToolProgressionHelper.isTogTool(heldItem)) {
			return;
		}

		float penalty = TraitSystem.getLootPenalty(heldItem);
		if (penalty <= 0.0f) {
			return;
		}

		List<ItemEntity> toRemove = new ArrayList<>();
		for (ItemEntity dropEntity : drops) {
			ItemStack stack = dropEntity.getItem();
			int keptCount = 0;
			for (int i = 0; i < stack.getCount(); i++) {
				if (level.random.nextFloat() >= penalty) {
					keptCount++;
				}
			}

			if (keptCount <= 0) {
				toRemove.add(dropEntity);
			} else if (keptCount < stack.getCount()) {
				stack.setCount(keptCount);
				dropEntity.setItem(stack);
			}
		}

		for (ItemEntity dropEntity : toRemove) {
			dropEntity.discard();
		}
	}
}
