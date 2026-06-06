package net.mcreator.toolsofthegods.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;

public final class WheelInputHelper {
	private WheelInputHelper() {
	}

	/** Raw GLFW check so the wheel stays open while G is held (avoids one-tick KeyMapping flicker). */
	public static boolean isModeWheelKeyHeld() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.getWindow() == null) {
			return ToolsOfTheGodsKeyMappings.ULTIMATE_MODE_WHEEL.isDown();
		}
		long window = mc.getWindow().getWindow();
		boolean glfwHeld = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS;
		boolean mappingHeld = ToolsOfTheGodsKeyMappings.ULTIMATE_MODE_WHEEL.isDown();
		boolean keyHeld = InputConstants.isKeyDown(window, ToolsOfTheGodsKeyMappings.ULTIMATE_MODE_WHEEL.getKey().getValue());
		return glfwHeld || mappingHeld || keyHeld;
	}
}
