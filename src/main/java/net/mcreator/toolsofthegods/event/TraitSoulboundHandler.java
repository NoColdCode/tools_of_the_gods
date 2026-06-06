package net.mcreator.toolsofthegods.event;

import java.util.ArrayList;
import java.util.List;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitSoulboundHandler {
	private static final String NBT_KEY = "togSoulbound";

	@SubscribeEvent
	public static void onPlayerDrops(LivingDropsEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		List<ItemEntity> toKeep = new ArrayList<>();
		for (ItemEntity drop : event.getDrops()) {
			ItemStack stack = drop.getItem();
			if (ToolProgressionHelper.isTogTool(stack) && TraitSystem.hasTrait(stack, TraitSystem.Trait.SOULBOUND)) {
				toKeep.add(drop);
			}
		}
		if (toKeep.isEmpty()) {
			return;
		}

		event.getDrops().removeAll(toKeep);

		var ops = player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
		ListTag list = new ListTag();
		for (ItemEntity ie : toKeep) {
			ItemStack.CODEC.encodeStart(ops, ie.getItem())
				.result()
				.ifPresent(list::add);
		}
		CompoundTag stored = new CompoundTag();
		stored.put("items", list);
		player.getPersistentData().put(NBT_KEY, stored);
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (!event.isWasDeath()) {
			return;
		}

		Player original = event.getOriginal();
		Player newPlayer = event.getEntity();

		if (!original.getPersistentData().contains(NBT_KEY)) {
			return;
		}

		CompoundTag stored = original.getPersistentData().getCompound(NBT_KEY);
		ListTag list = stored.getList("items", 10 /* TAG_COMPOUND */);

		var ops = newPlayer.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
		for (int i = 0; i < list.size(); i++) {
			Tag tag = list.get(i);
			ItemStack.CODEC.parse(ops, tag)
				.result()
				.ifPresent(stack -> {
					if (!stack.isEmpty()) {
						newPlayer.getInventory().add(stack);
					}
				});
		}

		original.getPersistentData().remove(NBT_KEY);
		newPlayer.getPersistentData().remove(NBT_KEY);
	}
}
