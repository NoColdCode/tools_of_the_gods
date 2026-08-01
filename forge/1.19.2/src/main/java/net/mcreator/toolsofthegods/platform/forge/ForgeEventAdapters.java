package net.mcreator.toolsofthegods.platform.forge;



import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraftforge.event.entity.living.LivingDropsEvent;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraftforge.event.entity.player.ItemFishedEvent;

import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraftforge.event.level.BlockEvent;



import net.minecraft.server.level.ServerPlayer;



import net.mcreator.toolsofthegods.logic.context.TogBlockBreakContext;

import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;

import net.mcreator.toolsofthegods.logic.context.TogBreakSpeedContext;

import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;

import net.mcreator.toolsofthegods.logic.context.TogItemFishedContext;

import net.mcreator.toolsofthegods.logic.context.TogLeftClickBlockContext;

import net.mcreator.toolsofthegods.logic.context.TogLivingDamagePostContext;

import net.mcreator.toolsofthegods.logic.context.TogLivingDeathContext;

import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;

import net.mcreator.toolsofthegods.logic.context.TogPlayerCloneContext;

import net.mcreator.toolsofthegods.logic.context.TogRightClickBlockContext;

import net.mcreator.toolsofthegods.logic.context.TogRightClickItemContext;



public final class ForgeEventAdapters {

	private ForgeEventAdapters() {

	}



	public static TogBlockDropsContext blockDrops(ForgeBlockDropsEvent event) {

		return new TogBlockDropsContext(

			event.getLevel(),

			event.getPos(),

			event.getState(),

			event.getBreaker(),

			new java.util.ArrayList<>(event.getDrops()),

			event.getBlockEntity()

		);

	}



	public static TogLivingDropsContext livingDrops(LivingDropsEvent event) {

		return new TogLivingDropsContext(event.getEntity(), event.getSource(), new java.util.ArrayList<>(event.getDrops()));

	}



	public static TogIncomingDamageContext incomingDamage(LivingHurtEvent event) {

		return new TogIncomingDamageContext(event.getEntity(), event.getSource(), event.getAmount());

	}



	public static void applyIncomingDamage(LivingHurtEvent event, TogIncomingDamageContext ctx) {

		event.setAmount(ctx.amount());

	}



	public static TogLivingDamagePostContext livingDamagePost(LivingHurtEvent event) {

		return new TogLivingDamagePostContext(event.getEntity(), event.getAmount(), 0f);

	}



	public static TogBreakSpeedContext breakSpeed(PlayerEvent.BreakSpeed event) {

		return new TogBreakSpeedContext(event.getEntity(), event.getState(), event.getPosition(), event.getOriginalSpeed());

	}



	public static void applyBreakSpeed(PlayerEvent.BreakSpeed event, TogBreakSpeedContext ctx) {

		event.setNewSpeed(ctx.newSpeed());

	}



	public static TogRightClickItemContext rightClickItem(PlayerInteractEvent.RightClickItem event) {

		return new TogRightClickItemContext(event.getEntity(), event.getLevel(), event.getItemStack());

	}



	public static void applyRightClickItem(PlayerInteractEvent.RightClickItem event, TogRightClickItemContext ctx) {

		event.setCanceled(ctx.canceled());

		if (ctx.cancellationResult() != net.minecraft.world.InteractionResult.PASS) {

			event.setCancellationResult(ctx.cancellationResult());

		}

	}



	public static TogRightClickBlockContext rightClickBlock(PlayerInteractEvent.RightClickBlock event) {

		return new TogRightClickBlockContext(event.getEntity(), event.getLevel(), event.getItemStack());

	}



	public static void applyRightClickBlock(PlayerInteractEvent.RightClickBlock event, TogRightClickBlockContext ctx) {

		event.setCanceled(ctx.canceled());

		if (ctx.cancellationResult() != net.minecraft.world.InteractionResult.PASS) {

			event.setCancellationResult(ctx.cancellationResult());

		}

	}



	public static TogLeftClickBlockContext leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {

		return new TogLeftClickBlockContext(event.getEntity(), event.getLevel(), event.getItemStack());

	}



	public static TogBlockBreakContext blockBreak(BlockEvent.BreakEvent event) {

		net.minecraft.world.level.Level level = event.getLevel() instanceof net.minecraft.world.level.Level l

			? l

			: null;

		return new TogBlockBreakContext(level, event.getPos(), event.getState(), event.getPlayer());

	}



	public static TogLivingDeathContext livingDeath(LivingDeathEvent event) {

		return new TogLivingDeathContext(event.getEntity(), event.getSource());

	}



	public static TogItemFishedContext itemFished(ItemFishedEvent event) {

		return new TogItemFishedContext(event.getEntity(), event.getDrops());

	}



	public static TogPlayerCloneContext playerClone(PlayerEvent.Clone event) {

		return new TogPlayerCloneContext(event.getOriginal(), event.getEntity(), event.isWasDeath());

	}



	public static ServerPlayer serverPlayerFromJoin(EntityJoinLevelEvent event) {

		if (event.getEntity() instanceof ServerPlayer player) {

			return player;

		}

		return null;

	}

}


