package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

/**
 * TOG gear is unbreakable but must keep {@link DataComponents#MAX_DAMAGE} so the
 * enchanting table treats it as enchantable (see {@link Item#isEnchantable}).
 */
@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class TogItemComponents {
	private static final Unbreakable UNBREAKABLE = new Unbreakable(false);

	private TogItemComponents() {
	}

	@SubscribeEvent
	public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
		for (var holder : ToolsOfTheGodsModItems.REGISTRY.getEntries()) {
			Item item = holder.value();
			if (item instanceof BlockItem) {
				continue;
			}
			event.modify(item, builder -> {
				if (item instanceof ArmorItem armorItem) {
					int tier = armorItem.getMaterial().value().enchantmentValue();
					builder.set(DataComponents.MAX_DAMAGE, armorItem.getType().getDurability(tier));
				}
				builder.set(DataComponents.UNBREAKABLE, UNBREAKABLE);
			});
		}
	}
}
