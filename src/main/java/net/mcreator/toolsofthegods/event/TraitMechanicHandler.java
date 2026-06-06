package net.mcreator.toolsofthegods.event;



import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import net.neoforged.neoforge.event.level.BlockDropsEvent;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;



import net.minecraft.world.effect.MobEffectInstance;

import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.item.ItemEntity;

import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.Level;



import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

import net.mcreator.toolsofthegods.util.TogEquipmentHelper;

import net.mcreator.toolsofthegods.util.TogEffectHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;



import java.util.ArrayList;

import java.util.List;



@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)

public class TraitMechanicHandler {

	private static final int PURIFY_INTERVAL = 160;



	@SubscribeEvent

	public static void onPlayerTick(PlayerTickEvent.Post event) {

		Player player = event.getEntity();

		if (player.level().isClientSide()) {

			return;

		}



		ItemStack held = player.getMainHandItem();

		if (ToolProgressionHelper.isTogTool(held)) {

			applyPassiveTraits(player, held);

		}



		for (ItemStack armor : TogEquipmentHelper.getWornTogArmor(player)) {
			applyPassiveTraits(player, armor);
			applyFireward(player, armor);
		}

		ItemStack wings = TogEquipmentHelper.getTogWings(player);
		if (!wings.isEmpty()) {
			applyPassiveTraits(player, wings);
		}



		ItemStack shield = TogEquipmentHelper.getTogShield(player);

		if (!shield.isEmpty()) {

			applyPassiveTraits(player, shield);

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



		ItemStack held = player.getMainHandItem();

		if (!ToolProgressionHelper.isTogTool(held)) {

			return;

		}



		if (TraitSystem.hasTrait(held, TraitSystem.Trait.BOUNTIFUL_I) && !TraitSystem.isSilkyActive(held)) {

			duplicateDrops(level, event.getDrops(), 0.08f);

		}

		if (TraitSystem.hasTrait(held, TraitSystem.Trait.SCAVENGER_I)) {

			autoCollectDrops(player, event.getDrops());

		}

	}



	@SubscribeEvent

	public static void onLivingDrops(LivingDropsEvent event) {

		if (!(event.getEntity().level() instanceof Level level) || level.isClientSide()) {

			return;

		}

		if (!(event.getSource().getEntity() instanceof Player player)) {

			return;

		}



		ItemStack held = player.getMainHandItem();

		if (!ToolProgressionHelper.isTogTool(held)) {

			return;

		}



		if (TraitSystem.hasTrait(held, TraitSystem.Trait.SCAVENGER_I)) {

			autoCollectDrops(player, event.getDrops());

		}

	}



	private static void applyPassiveTraits(Player player, ItemStack stack) {

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.SWIFTSTEP_I)) {

			TogEffectHelper.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 0, 220);

		}

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BULWARK_I)) {

			TogEffectHelper.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 0, 220);

		}

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.PURIFYING_I) && player.level().getGameTime() % PURIFY_INTERVAL == 0) {

			clearOneNegativeEffect(player);

		}

	}



	private static void applyFireward(Player player, ItemStack armor) {

		int amplifier = TraitSystem.getFirewardAmplifier(armor);

		if (amplifier >= 0) {

			TogEffectHelper.refreshEffect(player, MobEffects.FIRE_RESISTANCE, amplifier, 220);

		}

	}



	private static void duplicateDrops(Level level, java.util.List<ItemEntity> drops, float chancePerStack) {

		List<ItemEntity> extras = new ArrayList<>();

		for (ItemEntity entity : drops) {

			ItemStack stack = entity.getItem();

			if (stack.isEmpty()) {

				continue;

			}

			if (level.random.nextFloat() <= chancePerStack) {

				extras.add(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), stack.copy()));

			}

		}

		drops.addAll(extras);

	}



	private static void autoCollectDrops(Player player, Iterable<ItemEntity> drops) {

		for (ItemEntity entity : drops) {

			ItemStack stack = entity.getItem();

			if (stack.isEmpty()) {

				continue;

			}

			ItemStack toInsert = stack.copy();

			player.getInventory().add(toInsert);

			if (toInsert.isEmpty()) {

				entity.discard();

			} else {

				entity.setItem(toInsert);

			}

		}

	}



	private static void clearOneNegativeEffect(Player player) {

		for (MobEffectInstance effect : player.getActiveEffects()) {

			if (effect.getEffect().value().isBeneficial()) {

				continue;

			}

			player.removeEffect(effect.getEffect());

			break;

		}

	}

}


