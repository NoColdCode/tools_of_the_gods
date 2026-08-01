package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;
import net.mcreator.toolsofthegods.logic.context.TogPlayerCloneContext;

import java.util.ArrayList;
import java.util.List;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.TogPlayerData;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public final class TraitSoulboundLogic {
	private static final String NBT_KEY = "togSoulbound";

	public static void onPlayerDrops(TogLivingDropsContext ctx) {
		if (!(ctx.entity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		List<ItemEntity> toKeep = new ArrayList<>();
		for (ItemEntity drop : ctx.drops()) {
			ItemStack stack = drop.getItem();
			if (ToolProgressionHelper.isTogTool(stack) && TraitSystem.hasTrait(stack, TraitSystem.Trait.SOULBOUND)) {
				toKeep.add(drop);
			}
		}
		if (toKeep.isEmpty()) {
			return;
		}

		ctx.drops().removeAll(toKeep);

		var ops = player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
		ListTag list = new ListTag();
		for (ItemEntity ie : toKeep) {
			ItemStack.CODEC.encodeStart(ops, ie.getItem())
				.result()
				.ifPresent(list::add);
		}
		CompoundTag stored = new CompoundTag();
		stored.put("items", list);
		TogPlayerData.get(player).put(NBT_KEY, stored);
	}

	public static void onPlayerClone(TogPlayerCloneContext ctx) {
		if (!ctx.wasDeath()) {
			return;
		}

		Player original = ctx.original();
		Player newPlayer = ctx.newPlayer();

		if (!TogPlayerData.get(original).contains(NBT_KEY)) {
			return;
		}

		CompoundTag stored = TogPlayerData.get(original).getCompoundOrEmpty(NBT_KEY);
		ListTag list = stored.getListOrEmpty("items");

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

		TogPlayerData.get(original).remove(NBT_KEY);
		TogPlayerData.get(newPlayer).remove(NBT_KEY);
	}
}
