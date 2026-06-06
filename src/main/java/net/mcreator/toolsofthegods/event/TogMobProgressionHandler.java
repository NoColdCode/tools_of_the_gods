package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.util.FlailCombatHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

/**
 * XP and tier upgrades for non-human tool users (MineColonies citizens, fake players, etc.).
 */
@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TogMobProgressionHandler {

	@SubscribeEvent
	public static void onBlockDrops(BlockDropsEvent event) {
		if (!(event.getLevel() instanceof ServerLevel level)) {
			return;
		}
		Entity breaker = event.getBreaker();
		if (!(breaker instanceof LivingEntity living)) {
			return;
		}
		// Real players are handled in Item#mineBlock to avoid double XP.
		if (breaker instanceof Player player && !(player instanceof FakePlayer)) {
			return;
		}

		ItemStack tool = living.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(tool)) {
			return;
		}

		ToolType type = ToolProgressionHelper.getToolType(tool);
		if (type == ToolType.SWORD || type == ToolType.BOW || type == ToolType.CROSSBOW
			|| type == ToolType.STAFF || type == ToolType.FISHING_ROD || type == ToolType.WINGS
			|| type == ToolType.ARMOR || type == ToolType.SHIELD) {
			return;
		}

		BlockState state = event.getState();
		var pos = event.getPos();
		int tier = ToolProgressionHelper.getStoredTier(tool);
		int xp = tier >= 4 ? 2 : 1;
		ToolProgressionHelper.gainXp(level, pos.getX(), pos.getY(), pos.getZ(), living, tool, xp);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingIncomingDamageEvent event) {
		if (event.getEntity().level().isClientSide()) {
			return;
		}
		Entity attacker = event.getSource().getEntity();
		if (!(attacker instanceof LivingEntity living)) {
			return;
		}
		if (attacker instanceof Player player && !(player instanceof FakePlayer)) {
			return;
		}
		if (event.getSource().getDirectEntity() != attacker) {
			return;
		}

		ItemStack weapon = living.getMainHandItem();
		ToolType type = ToolProgressionHelper.getToolType(weapon);
		if (type == ToolType.NONE || type == ToolType.BOW || type == ToolType.CROSSBOW
			|| type == ToolType.STAFF || type == ToolType.FISHING_ROD) {
			return;
		}

		if (type == ToolType.FLAIL && event.getEntity() instanceof LivingEntity target) {
			FlailCombatHelper.applyStun(target, FlailCombatHelper.getLevel(weapon));
		}

		int xp = switch (type) {
			case SWORD, SPEAR, FLAIL, TRIDENT, AXE -> ToolProgressionHelper.getSwordHitXp(event.getEntity());
			default -> 1;
		};
		ToolProgressionHelper.gainXp(living.level(), living.getX(), living.getY(), living.getZ(), living, weapon, xp);
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof LivingEntity living) || living.level().isClientSide()) {
			return;
		}
		if (living instanceof Player player && !(player instanceof FakePlayer)) {
			return;
		}
		if (living.tickCount % 40 != 0) {
			return;
		}

		ItemStack tool = living.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(tool) || !ToolProgressionHelper.needsUpgrade(tool)) {
			return;
		}

		UpgradePickaxeProcedure.tryUpgrade(living.level(), living.getX(), living.getY(), living.getZ(), living, tool, false);
	}
}
