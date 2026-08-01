package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;

import net.mcreator.toolsofthegods.init.TogContentCatalog;

public final class FabricTogItemComponents {
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
					if (item instanceof BowItem) {
						builder.set(DataComponents.MAX_DAMAGE, 384);
					}
					builder.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
				});
			}
		});
	}
}
