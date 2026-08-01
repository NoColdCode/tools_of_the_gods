package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.init.TogContentCatalog;

/**
 * TOG gear is unbreakable but must keep {@link DataComponents#MAX_DAMAGE} so the
 * enchanting table treats it as enchantable (see {@link Item#isEnchantable}).
 */
@EventBusSubscriber(modid = TogModConstants.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class NeoForgeTogItemComponents {
	private static final Unbreakable UNBREAKABLE = new Unbreakable(false);

	private NeoForgeTogItemComponents() {
	}

	@SubscribeEvent
	public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
		for (var holder : TogContentCatalog.allItems()) {
			Item item = holder.get();
			if (item instanceof BlockItem) {
				continue;
			}
			event.modify(item, builder -> {
				if (item instanceof ArmorItem armorItem) {
					int tier = armorItem.getMaterial().value().enchantmentValue();
					builder.set(DataComponents.MAX_DAMAGE, armorItem.getType().getDurability(tier));
				} else if (item instanceof BowItem) {
					builder.set(DataComponents.MAX_DAMAGE, 384);
				}
				builder.set(DataComponents.UNBREAKABLE, UNBREAKABLE);
			});
		}
	}
}
