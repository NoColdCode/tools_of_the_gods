package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogLivingDeathContext;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.util.TraitExtendedCombatHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public final class TraitVitalityLogic {

	public static void onLivingDeath(TogLivingDeathContext ctx) {
		if (ctx.entity().level().isClientSide()) {
			return;
		}
		if (!(ctx.source().getEntity() instanceof Player player)) {
			return;
		}

		ItemStack weapon = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(weapon) || TraitSystem.getVitalityHealAmount(weapon) <= 0f) {
			return;
		}

		TraitExtendedCombatHelper.applyOnKill(weapon, player);
	}
}
