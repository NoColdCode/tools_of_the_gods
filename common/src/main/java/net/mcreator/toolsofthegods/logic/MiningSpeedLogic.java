package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogBreakSpeedContext;


import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.TraitSystem;

public final class MiningSpeedLogic {
	
	public static void onBreakSpeed(TogBreakSpeedContext ctx) {
		ItemStack heldItem = ctx.player().getMainHandItem();
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(heldItem);
		var posOpt = ctx.position();
		if (posOpt.isEmpty()) {
			return;
		}
		var pos = posOpt.get();

		if (type == ToolProgressionHelper.ToolType.NONE || type == ToolProgressionHelper.ToolType.SWORD
			|| type == ToolProgressionHelper.ToolType.SPEAR || type == ToolProgressionHelper.ToolType.FLAIL
			|| type == ToolProgressionHelper.ToolType.BOW) {
			return;
		}

		if (type == ToolProgressionHelper.ToolType.PICKAXE || type == ToolProgressionHelper.ToolType.HAMMER) {
			int tier = ToolProgressionHelper.getEffectiveTierForStats(heldItem);
			
			if (!ctx.state().is(BlockTags.MINEABLE_WITH_PICKAXE)) {
				// Do not speed up dirt/gravel/clay/leaves and other non-pickaxe blocks.
				return;
			}

			if (!TierSystem.canHarvest(ctx.state(), tier)) {
				// Respect harvest requirements by tier progression.
				ctx.setNewSpeed(Math.min(ctx.originalSpeed(), 0.2f));
				return;
			}
			
			// Get mining speed bonus from tier system
			float speedMultiplier = ToolProgressionHelper.getEffectiveMiningSpeed(heldItem);
			speedMultiplier *= (1.0f + TraitSystem.getMomentumMiningBonus(heldItem));
			speedMultiplier *= (1.0f + TraitSystem.getMoonlitMiningBonus(heldItem, ctx.player().level().isNight()));

			if (TierSystem.isOneShotBlock(ctx.state(), tier)) {
				ctx.setNewSpeed(10000.0f);
				return;
			}
			
			// Apply the multiplier to the break speed
			ctx.setNewSpeed(ctx.originalSpeed() * speedMultiplier);
			return;
		}

		int tier = ToolProgressionHelper.getEffectiveTierForStats(heldItem);
		float speedMultiplier = ToolProgressionHelper.getEffectiveMiningSpeed(heldItem);
		speedMultiplier *= (1.0f + TraitSystem.getMoonlitMiningBonus(heldItem, ctx.player().level().isNight()));

		boolean matchingTag = switch (type) {
			case AXE -> ctx.state().is(BlockTags.MINEABLE_WITH_AXE);
			case SHOVEL -> ctx.state().is(BlockTags.MINEABLE_WITH_SHOVEL);
			case HOE -> ctx.state().is(BlockTags.MINEABLE_WITH_HOE);
			default -> false;
		};

		if (!matchingTag) {
			return;
		}

		if (tier >= 9) {
			ctx.setNewSpeed(10000.0f);
		} else {
			ctx.setNewSpeed(ctx.originalSpeed() * speedMultiplier);
		}
	}
}
