package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.TraitFreezyHelper;
import net.mcreator.toolsofthegods.util.TraitPoisonHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.lang.reflect.Field;
import java.util.List;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitSpecialToolsHandler {
	private static Field fishingNibbleField;

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		if (event.getEntity().level().isClientSide()) {
			return;
		}
		ItemStack rod = findFishingRod(event.getEntity());
		if (rod.isEmpty()) {
			return;
		}

		float treasureChance = TraitSystem.getAnglerTreasureChance(rod);
		if (treasureChance > 0.0f && event.getEntity().level().random.nextFloat() <= treasureChance) {
			ItemStack bonus = switch (event.getEntity().level().random.nextInt(4)) {
				case 0 -> new ItemStack(Items.EMERALD, 1 + event.getEntity().level().random.nextInt(2));
				case 1 -> new ItemStack(Items.LAPIS_LAZULI, 2 + event.getEntity().level().random.nextInt(4));
				case 2 -> new ItemStack(Items.NAME_TAG);
				default -> new ItemStack(Items.ENCHANTED_BOOK);
			};
			ItemEntity drop = new ItemEntity(event.getEntity().level(), event.getEntity().getX(),
				event.getEntity().getY(), event.getEntity().getZ(), bonus);
			event.getEntity().level().addFreshEntity(drop);
		}

		if (TraitSystem.hasTrait(rod, TraitSystem.Trait.BOUNTIFUL_I)) {
			for (ItemStack stack : event.getDrops()) {
				if (!stack.isEmpty() && event.getEntity().level().random.nextFloat() <= 0.10f) {
					ItemEntity extra = new ItemEntity(event.getEntity().level(), event.getEntity().getX(),
						event.getEntity().getY(), event.getEntity().getZ(), stack.copy());
					event.getEntity().level().addFreshEntity(extra);
				}
			}
		}

		if (TraitSystem.hasTrait(rod, TraitSystem.Trait.SCAVENGER_I)) {
			for (ItemStack stack : event.getDrops()) {
				if (!stack.isEmpty()) {
					ItemStack copy = stack.copy();
					if (!event.getEntity().getInventory().add(copy)) {
						event.getEntity().drop(copy, false);
					}
				}
			}
			event.getDrops().clear();
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide()) {
			return;
		}

		ItemStack rod = findFishingRod(player);
		if (player.fishing != null && !rod.isEmpty()) {
			handleFishingHook(player.fishing, rod);
		}

		for (ThrownTrident trident : player.level().getEntitiesOfClass(ThrownTrident.class, player.getBoundingBox().inflate(64.0d))) {
			if (trident.getOwner() == player) {
				handleReturningTrident(trident);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		if (event.getEntity().level().isClientSide()) {
			return;
		}

		if (event.getEntity() instanceof Player player && event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
			ItemStack wings = TogEquipmentHelper.getTogWings(player);
			if (!wings.isEmpty()) {
				event.setAmount(event.getAmount() * TraitSystem.getFeatherfallDamageMultiplier(wings));
			}
			return;
		}

		Entity sourceEntity = event.getSource().getEntity();
		if (!(sourceEntity instanceof Player player)) {
			return;
		}

		Entity direct = event.getSource().getDirectEntity();
		float bonus = 0.0f;
		ItemStack weapon = ItemStack.EMPTY;
		boolean riptideBonus = false;

		if (direct instanceof AbstractArrow) {
			weapon = findCrossbow(player);
			if (!weapon.isEmpty()) {
				bonus = TraitSystem.getMarksmanDamageBonus(weapon);
			}
		} else if (direct instanceof ThrownTrident) {
			weapon = findTrident(player);
			if (ToolProgressionHelper.getToolType(weapon) == ToolProgressionHelper.ToolType.TRIDENT) {
				bonus = TraitSystem.getImpalerDamageBonus(weapon);
				riptideBonus = TraitSystem.hasRiptide(weapon) && isWetEnvironment(player);
			}
		} else if (direct instanceof SmallFireball) {
			weapon = findStaff(player);
			if (!weapon.isEmpty()) {
				bonus = (TraitSystem.getArcaneBoltDamageMultiplier(weapon) - 1.0f) * 3.0f;
				if (TraitSystem.hasChanneling(weapon) && player.level() instanceof ServerLevel serverLevel
					&& player.level().isThundering() && player.level().canSeeSky(BlockPos.containing(player.position()))) {
					LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
					if (bolt != null) {
						bolt.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
						serverLevel.addFreshEntity(bolt);
					}
				}
			}
		} else if (direct == player) {
			weapon = player.getMainHandItem();
			ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(weapon);
			if (type == ToolProgressionHelper.ToolType.TRIDENT) {
				bonus = TraitSystem.getImpalerDamageBonus(weapon);
			}
		}

		if (bonus > 0.0f) {
			event.setAmount(event.getAmount() + bonus);
		}
		if (riptideBonus) {
			event.setAmount(event.getAmount() * 1.5f);
		}

		if (direct instanceof AbstractArrow && !weapon.isEmpty()) {
			TraitPoisonHelper.applyMeleePoison(weapon, event.getEntity());
			TraitFreezyHelper.applyMeleeFreeze(weapon, event.getEntity());
		}
	}

	private static void handleFishingHook(FishingHook hook, ItemStack rod) {
		if (!TraitSystem.hasReel(rod)) {
			return;
		}
		if (hook.tickCount % 2 != 0) {
			return;
		}
		try {
			Field nibble = getFishingNibbleField();
			int value = nibble.getInt(hook);
			if (value > 0) {
				nibble.setInt(hook, value - 1);
			}
		} catch (ReflectiveOperationException ignored) {
		}
	}

	private static void handleReturningTrident(ThrownTrident trident) {
		if (!(trident.getOwner() instanceof Player owner)) {
			return;
		}
		ItemStack stack = findTrident(owner);
		if (stack.isEmpty() || !TraitSystem.hasReturning(stack)) {
			return;
		}
		if (trident.onGround() || trident.isNoPhysics()) {
			return;
		}
		Vec3 toOwner = owner.position().subtract(trident.position());
		if (toOwner.lengthSqr() < 1.0d) {
			return;
		}
		trident.setDeltaMovement(trident.getDeltaMovement().add(toOwner.normalize().scale(0.15d)));
	}

	private static Field getFishingNibbleField() throws NoSuchFieldException {
		if (fishingNibbleField == null) {
			fishingNibbleField = FishingHook.class.getDeclaredField("nibble");
			fishingNibbleField.setAccessible(true);
		}
		return fishingNibbleField;
	}

	private static boolean isWetEnvironment(Player player) {
		Level level = player.level();
		return level.isRainingAt(player.blockPosition()) || player.isInWaterOrRain();
	}

	public static ItemStack findFishingRod(Player player) {
		ItemStack main = player.getMainHandItem();
		if (ToolProgressionHelper.getToolType(main) == ToolProgressionHelper.ToolType.FISHING_ROD) {
			return main;
		}
		ItemStack off = player.getOffhandItem();
		if (ToolProgressionHelper.getToolType(off) == ToolProgressionHelper.ToolType.FISHING_ROD) {
			return off;
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findCrossbow(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.CROSSBOW_OF_THE_GODS.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findStaff(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack findTrident(Player player) {
		for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
			if (stack.is(ToolsOfTheGodsModItems.TRIDENT_OF_THE_GODS.get())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}
