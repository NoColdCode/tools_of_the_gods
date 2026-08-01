package net.mcreator.toolsofthegods.platform.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.client.WingsResistHud;

/**
 * Swaps the vanilla XP bar/level for wing Resistance while fall-flying.
 */
@EventBusSubscriber(modid = TogModConstants.MODID, value = Dist.CLIENT)
public final class NeoForgeWingsResistHud {
	private NeoForgeWingsResistHud() {
	}

	@SubscribeEvent
	public static void onRenderGuiLayerPre(RenderGuiLayerEvent.Pre event) {
		if (event.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR)) {
			if (WingsResistHud.tryRender(event.getGuiGraphics())) {
				event.setCanceled(true);
			}
			return;
		}
		if (event.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL) && WingsResistHud.shouldReplaceXpBar()) {
			event.setCanceled(true);
		}
	}
}
