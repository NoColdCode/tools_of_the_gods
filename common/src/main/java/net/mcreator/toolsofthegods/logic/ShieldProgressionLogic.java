package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogShieldBlockContext;
import net.mcreator.toolsofthegods.logic.context.TogLivingDamagePostContext;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.ShieldStrainHelper;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ShieldProgressionLogic {
	private static final Map<UUID, Long> suppressHurtVisualUntilTick = new ConcurrentHashMap<>();

	public static void onPlayerTick(Player player) {
		if (!player.level().isClientSide()) {
			ShieldStrainHelper.tick(player);
		}
		Long until = suppressHurtVisualUntilTick.get(player.getUUID());
		if (until != null && player.level().getGameTime() <= until) {
			player.hurtTime = 0;
			player.hurtDuration = 0;
		} else if (until != null) {
			suppressHurtVisualUntilTick.remove(player.getUUID());
		}
	}

	public static void onShieldBlock(TogShieldBlockContext ctx) {
		if (!(ctx.entity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		ItemStack shield = player.getUseItem();
		if (shield.isEmpty() || shield.getItem() != ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return;
		}

		if (!ctx.originalBlock()) {
			ctx.setBlocked(false);
			ctx.setBlockedDamage(0f);
			ctx.setShieldDamage(0f);
			return;
		}

		float incoming = ctx.originalBlockedDamage();

		if (ShieldStrainHelper.isStrained(player)) {
			ctx.setBlocked(false);
			ctx.setBlockedDamage(0f);
			ctx.setShieldDamage(0f);
			return;
		}

		float blockChance = ToolProgressionHelper.getShieldBlockChance(shield);
		if (player.getRandom().nextFloat() > blockChance) {
			ctx.setBlocked(false);
			ctx.setBlockedDamage(0f);
			ctx.setShieldDamage(0f);
			return;
		}

		float reduction = ToolProgressionHelper.getShieldBlockReduction(shield);
		float blocked = incoming * reduction;

		if (!ShieldStrainHelper.addStrainFromBlock(player, shield, blocked)) {
			ctx.setBlocked(false);
			ctx.setBlockedDamage(0f);
			ctx.setShieldDamage(0f);
			return;
		}

		ctx.setBlocked(true);
		ctx.setBlockedDamage(blocked);
		ctx.setShieldDamage(0f);

		if (blocked > 0f) {
			var source = ctx.damageSource();
			var attacker = source.getEntity();
			if (attacker instanceof LivingEntity living) {
				float reflect = blocked * ShieldStrainHelper.getReflectRatio(shield);
				if (reflect > 0.01f) {
					living.hurt(source, reflect);
				}
				float riposte = TraitSystem.getRiposteDamage(shield);
				if (riposte > 0.01f) {
					living.hurt(source, riposte);
				}
			}
			int blockXp = Math.max(2, (int) Math.ceil(blocked));
			ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, shield, blockXp);
		}
	}

	public static void onDamagePost(TogLivingDamagePostContext ctx) {
		if (!(ctx.entity() instanceof Player player)) {
			return;
		}
		if (ctx.newDamage() > 0.01f || ctx.blockedDamage() <= 0f) {
			return;
		}
		ItemStack shield = player.getUseItem();
		if (shield.isEmpty() || shield.getItem() != ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return;
		}
		long now = player.level().getGameTime();
		suppressHurtVisualUntilTick.put(player.getUUID(), now + 2);
		player.hurtTime = 0;
		player.hurtDuration = 0;
	}
}
