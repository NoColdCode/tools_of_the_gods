package net.mcreator.toolsofthegods.platform.neoforge;

import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.init.TogContentCatalog;

@EventBusSubscriber(modid = TogModConstants.MODID)
public final class NeoForgeTogItemComponents {
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
				if (item instanceof BowItem) {
					builder.set(DataComponents.MAX_DAMAGE, 384);
				}
				builder.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
			});
		}
	}
}
