package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogBlockDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogIncomingDamageContext;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.toolsofthegods.procedures.UpgradePickaxeProcedure;
import net.mcreator.toolsofthegods.platform.TogPlatforms;
import net.mcreator.toolsofthegods.util.FlailCombatHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper.ToolType;

/**
 * XP and tier upgrades for non-human tool users (MineColonies citizens, fake players, etc.).
 */
public final class TogMobProgressionLogic {

	public static void onBlockDrops(TogBlockDropsContext ctx) {
		if (!(ctx.level() instanceof ServerLevel level)) {
			return;
		}
		Entity breaker = ctx.breaker();
		if (!(breaker instanceof LivingEntity living)) {
			return;
		}
		// Real players are handled in Item#mineBlock to avoid double XP.
		if (breaker instanceof Player player && !TogPlatforms.get().isAutomatedPlayer(player)) {
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

		BlockState state = ctx.state();
		var pos = ctx.pos();
		int xp = 1;
		ToolProgressionHelper.gainXp(level, pos.getX(), pos.getY(), pos.getZ(), living, tool, xp);
	}

	public static void onLivingDamage(TogIncomingDamageContext ctx) {
		if (ctx.entity().level().isClientSide()) {
			return;
		}
		Entity attacker = ctx.source().getEntity();
		if (!(attacker instanceof LivingEntity living)) {
			return;
		}
		if (attacker instanceof Player player && !TogPlatforms.get().isAutomatedPlayer(player)) {
			return;
		}
		if (ctx.source().getDirectEntity() != attacker) {
			return;
		}

		ItemStack weapon = living.getMainHandItem();
		ToolType type = ToolProgressionHelper.getToolType(weapon);
		if (type == ToolType.NONE || type == ToolType.BOW || type == ToolType.CROSSBOW
			|| type == ToolType.STAFF || type == ToolType.FISHING_ROD) {
			return;
		}

		if (type == ToolType.FLAIL && ctx.entity() instanceof LivingEntity target) {
			FlailCombatHelper.applyStun(target, FlailCombatHelper.getLevel(weapon));
		}

		int xp = switch (type) {
			case SWORD, SPEAR, FLAIL, TRIDENT, AXE -> ToolProgressionHelper.getSwordHitXp(ctx.entity());
			default -> 1;
		};
		ToolProgressionHelper.gainXp(living.level(), living.getX(), living.getY(), living.getZ(), living, weapon, xp);
	}

	public static void onEntityTick(Entity entity) {
		if (!(entity instanceof LivingEntity living) || living.level().isClientSide()) {
			return;
		}
		if (living instanceof Player player && !TogPlatforms.get().isAutomatedPlayer(player)) {
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
