package net.mcreator.toolsofthegods.event;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;

import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class MiningSpeedHandler {
	
	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		ItemStack heldItem = event.getEntity().getMainHandItem();
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(heldItem);
		var posOpt = event.getPosition();
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
			
			if (!event.getState().is(BlockTags.MINEABLE_WITH_PICKAXE)) {
				// Do not speed up dirt/gravel/clay/leaves and other non-pickaxe blocks.
				return;
			}

			if (!TierSystem.canHarvest(event.getState(), tier)) {
				// Respect harvest requirements by tier progression.
				event.setNewSpeed(Math.min(event.getOriginalSpeed(), 0.2f));
				return;
			}
			
			// Get mining speed bonus from tier system
			float speedMultiplier = ToolProgressionHelper.getEffectiveMiningSpeed(heldItem);
			speedMultiplier *= (1.0f + TraitSystem.getMomentumMiningBonus(heldItem));
			speedMultiplier *= (1.0f + TraitSystem.getMoonlitMiningBonus(heldItem, event.getEntity().level().isNight()));

			if (TierSystem.isOneShotBlock(event.getState(), tier)) {
				event.setNewSpeed(10000.0f);
				return;
			}
			
			// Apply the multiplier to the break speed
			event.setNewSpeed(event.getOriginalSpeed() * speedMultiplier);
			return;
		}

		int tier = ToolProgressionHelper.getEffectiveTierForStats(heldItem);
		float speedMultiplier = ToolProgressionHelper.getEffectiveMiningSpeed(heldItem);
		speedMultiplier *= (1.0f + TraitSystem.getMoonlitMiningBonus(heldItem, event.getEntity().level().isNight()));

		boolean matchingTag = switch (type) {
			case AXE -> event.getState().is(BlockTags.MINEABLE_WITH_AXE);
			case SHOVEL -> event.getState().is(BlockTags.MINEABLE_WITH_SHOVEL);
			case HOE -> event.getState().is(BlockTags.MINEABLE_WITH_HOE);
			default -> false;
		};

		if (!matchingTag) {
			return;
		}

		if (tier >= 9) {
			event.setNewSpeed(10000.0f);
		} else {
			event.setNewSpeed(event.getOriginalSpeed() * speedMultiplier);
		}
	}
}
