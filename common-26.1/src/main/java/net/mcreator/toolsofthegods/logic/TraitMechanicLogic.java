package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;










import net.minecraft.tags.BlockTags;

import net.minecraft.world.effect.MobEffectInstance;

import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.item.ItemEntity;

import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.Level;




import net.mcreator.toolsofthegods.util.TogEquipmentHelper;

import net.mcreator.toolsofthegods.util.TogEffectHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;



import java.util.ArrayList;

import java.util.List;




public final class TraitMechanicLogic {

	private static final int PURIFY_INTERVAL = 160;




	public static void onPlayerTick(Player player) {


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




	public static void onBlockDrops(TogBlockDropsContext ctx) {

		if (!(ctx.level() instanceof Level level) || level.isClientSide()) {

			return;

		}

		if (!(ctx.breaker() instanceof Player player)) {

			return;

		}



		ItemStack held = player.getMainHandItem();

		if (!ToolProgressionHelper.isTogTool(held)) {

			return;

		}



		if (TraitSystem.hasTrait(held, TraitSystem.Trait.BOUNTIFUL_I) && !TraitSystem.isSilkyActive(held)) {

			duplicateDrops(level, ctx.drops(), 0.08f);

		}

		if (ToolProgressionHelper.getToolType(held) == ToolProgressionHelper.ToolType.HOE) {
			float harvestChance = TraitSystem.getHarvestBonusChance(held);
			if (harvestChance > 0f && ctx.state().is(BlockTags.CROPS)) {
				duplicateDrops(level, ctx.drops(), harvestChance);
			}
		}

		if (TraitSystem.hasTrait(held, TraitSystem.Trait.SCAVENGER_I)) {

			autoCollectDrops(player, ctx.drops());

		}

	}




	public static void onLivingDrops(TogLivingDropsContext ctx) {

		if (!(ctx.entity().level() instanceof Level level) || level.isClientSide()) {

			return;

		}

		if (!(ctx.source().getEntity() instanceof Player player)) {

			return;

		}



		ItemStack held = player.getMainHandItem();

		if (!ToolProgressionHelper.isTogTool(held)) {

			return;

		}



		if (TraitSystem.hasTrait(held, TraitSystem.Trait.SCAVENGER_I)) {

			autoCollectDrops(player, ctx.drops());

		}

	}



	private static void applyPassiveTraits(Player player, ItemStack stack) {

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.SWIFTSTEP_I)) {

			TogEffectHelper.refreshEffect(player, MobEffects.SPEED, 0, 220);

		}

		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.BULWARK_I)) {

			TogEffectHelper.refreshEffect(player, MobEffects.RESISTANCE, 0, 220);

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

			if (level.getRandom().nextFloat() <= chancePerStack) {

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

