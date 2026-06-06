package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitMagneticHandler {
	private static final double ATTRACT_RANGE = 5.0;
	private static final double ATTRACT_SPEED = 0.15;

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide()) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!TraitSystem.hasTrait(heldItem, TraitSystem.Trait.MAGNETIC_I)) {
			heldItem = player.getOffhandItem();
		}
		if (!ToolProgressionHelper.isTogTool(heldItem) || !TraitSystem.hasTrait(heldItem, TraitSystem.Trait.MAGNETIC_I)) {
			return;
		}

		Vec3 playerCenter = player.position().add(0, player.getBbHeight() * 0.5, 0);
		List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(ATTRACT_RANGE));

		for (ItemEntity itemEntity : items) {
			Vec3 diff = playerCenter.subtract(itemEntity.position());
			double dist = diff.length();
			if (dist < 0.5 || dist > ATTRACT_RANGE) {
				continue;
			}
			itemEntity.setDeltaMovement(diff.normalize().scale(ATTRACT_SPEED));
		}
	}
}
