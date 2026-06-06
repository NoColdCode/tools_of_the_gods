package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitSilkyHandler {

	// HIGH priority so silk touch drops are placed before Autosmelt (NORMAL) can process them
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBlockDrops(BlockDropsEvent event) {
		if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
			return;
		}

		if (!(event.getBreaker() instanceof Player player)) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (!ToolProgressionHelper.isTogTool(heldItem) || !TraitSystem.isSilkyActive(heldItem)) {
			return;
		}

		// Build a silk touch copy of the tool for loot table calculation
		var silkHolder = serverLevel.registryAccess()
				.lookupOrThrow(Registries.ENCHANTMENT)
				.getOrThrow(Enchantments.SILK_TOUCH);
		ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		mutable.set(silkHolder, 1);
		ItemStack silkTool = heldItem.copyWithCount(1);
		silkTool.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());

		BlockPos pos = event.getPos();
		List<ItemStack> silkDrops = Block.getDrops(event.getState(), serverLevel, pos, event.getBlockEntity(), player, silkTool);

		List<ItemEntity> drops = event.getDrops();
		drops.clear();
		double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
		for (ItemStack silkDrop : silkDrops) {
			if (!silkDrop.isEmpty()) {
				drops.add(new ItemEntity(serverLevel, x, y, z, silkDrop));
			}
		}
	}
}
