package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class ShieldProgressionHandler {
	private static final Map<UUID, Float> preShieldDamage = new ConcurrentHashMap<>();

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void capturePreShieldDamage(LivingIncomingDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}
		ItemStack shield = getActiveShield(player);
		if (shield == null || !isBlockableDamage(event, player)) {
			return;
		}
		preShieldDamage.put(player.getUUID(), event.getAmount());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void resolveShieldBlock(LivingIncomingDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		Float original = preShieldDamage.remove(player.getUUID());
		if (original == null) {
			return;
		}

		ItemStack shield = getActiveShield(player);
		if (shield == null) {
			return;
		}

		float blockChance = ToolProgressionHelper.getShieldBlockChance(shield);
		if (player.getRandom().nextFloat() > blockChance) {
			event.setAmount(original);
			return;
		}

		float reduction = ToolProgressionHelper.getShieldBlockReduction(shield);
		float blocked = original * reduction;
		event.setAmount(Math.max(0f, original - blocked));

		int blockXp = Math.max(2, (int) Math.ceil(blocked));
		ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, shield, blockXp);
	}

	private static ItemStack getActiveShield(Player player) {
		if (!player.isBlocking()) {
			return ItemStack.EMPTY;
		}
		ItemStack shield = player.getUseItem();
		if (!shield.isEmpty() && shield.getItem() == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return shield;
		}
		shield = player.getOffhandItem();
		if (!shield.isEmpty() && shield.getItem() == ToolsOfTheGodsModItems.SHIELD_OF_THE_GODS.get()) {
			return shield;
		}
		return ItemStack.EMPTY;
	}

	private static boolean isBlockableDamage(LivingIncomingDamageEvent event, Player player) {
		var direct = event.getSource().getDirectEntity();
		if (direct instanceof AbstractArrow) {
			return isFacingAttacker(player, direct.position());
		}
		if (direct instanceof Projectile projectile) {
			return isFacingAttacker(player, projectile.position());
		}
		if (direct instanceof LivingEntity attacker) {
			return isFacingAttacker(player, attacker.position());
		}
		return false;
	}

	private static boolean isFacingAttacker(Player player, Vec3 attackerPos) {
		Vec3 look = player.getViewVector(1f).normalize();
		Vec3 toAttacker = attackerPos.subtract(player.position()).normalize();
		return look.dot(toAttacker) > 0.1;
	}
}
