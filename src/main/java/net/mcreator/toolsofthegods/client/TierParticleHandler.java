package net.mcreator.toolsofthegods.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;

/**
 * Optional tier particles (off by default). Re-enable via {@code tierParticlesEnabled} in config.
 */
@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, value = Dist.CLIENT)
public class TierParticleHandler {

	@SubscribeEvent
	public static void onClientTick(PlayerTickEvent.Post event) {
		if (!ToolsOfTheGodsCommonConfig.TIER_PARTICLES_ENABLED.get()) {
			return;
		}
		// Particle visuals removed by default; enable in config to restore later if desired.
	}
}
