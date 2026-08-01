package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;

import net.mcreator.toolsofthegods.init.TogContentCatalog;

public final class FabricTogItemComponents {
	private static final Unbreakable UNBREAKABLE = new Unbreakable(false);

	private FabricTogItemComponents() {
	}

	public static void register() {
		DefaultItemComponentEvents.MODIFY.register(context -> {
			for (var holder : TogContentCatalog.allItems()) {
				Item item = holder.get();
				if (item instanceof BlockItem) {
					continue;
				}
				context.modify(item, builder -> {
					if (item instanceof ArmorItem armorItem) {
						int tier = armorItem.getMaterial().value().enchantmentValue();
						builder.set(DataComponents.MAX_DAMAGE, armorItem.getType().getDurability(tier));
					} else if (item instanceof BowItem) {
						builder.set(DataComponents.MAX_DAMAGE, 384);
					}
					builder.set(DataComponents.UNBREAKABLE, UNBREAKABLE);
				});
			}
		});
	}
}
