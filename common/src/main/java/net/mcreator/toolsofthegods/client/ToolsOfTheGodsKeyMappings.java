package net.mcreator.toolsofthegods.client;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.KeyMapping;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ToolsOfTheGodsKeyMappings {
	public static final KeyMapping ACTIVATE_PICKAXE_POWER = new KeyMapping(
		"key.tools_of_the_gods.activate_pickaxe_power",
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_V,
		"key.categories.tools_of_the_gods"
	);

	public static final KeyMapping ULTIMATE_MODE_WHEEL = new KeyMapping(
		"key.tools_of_the_gods.ultimate_mode_wheel",
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_G,
		"key.categories.tools_of_the_gods"
	);

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(ACTIVATE_PICKAXE_POWER);
		event.register(ULTIMATE_MODE_WHEEL);
	}
}
