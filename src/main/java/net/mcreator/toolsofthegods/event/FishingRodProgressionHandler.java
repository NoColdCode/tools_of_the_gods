package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class FishingRodProgressionHandler {

	@SubscribeEvent
	public static void onItemFished(ItemFishedEvent event) {
		if (event.getEntity().level().isClientSide()) {
			return;
		}
		ItemStack rod = event.getEntity().getMainHandItem();
		if (ToolProgressionHelper.getToolType(rod) != ToolProgressionHelper.ToolType.FISHING_ROD) {
			rod = event.getEntity().getOffhandItem();
		}
		if (ToolProgressionHelper.getToolType(rod) == ToolProgressionHelper.ToolType.FISHING_ROD) {
			ToolProgressionHelper.gainXp(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(),
				event.getEntity().getZ(), event.getEntity(), rod, 4);
		}
	}
}
