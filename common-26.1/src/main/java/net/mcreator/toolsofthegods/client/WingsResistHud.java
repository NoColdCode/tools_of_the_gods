package net.mcreator.toolsofthegods.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.logic.WingsFlightLogic;

/**
 * Replaces the XP bar with a depleting wing Resistance (air-time) bar while the player
 * is fall-flying on Tools of the Gods wings (Elytra / Wings modes).
 */
public final class WingsResistHud {
	private static final ResourceLocation XP_BAR_BACKGROUND =
		ResourceLocation.withDefaultNamespace("hud/experience_bar_background");
	private static final ResourceLocation XP_BAR_PROGRESS =
		ResourceLocation.withDefaultNamespace("hud/experience_bar_progress");

	private static final int BAR_WIDTH = 182;
	private static final int BAR_HEIGHT = 5;

	private WingsResistHud() {
	}

	public static boolean shouldReplaceXpBar() {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null || mc.options.hideGui || player.isSpectator()) {
			return false;
		}
		if (!player.isFallFlying()) {
			return false;
		}
		ItemStack wings = WingsFlightLogic.getWornWings(player);
		if (wings.isEmpty()) {
			return false;
		}
		WingsFlightLogic.Mode mode = WingsFlightLogic.getMode(wings);
		// Level 100: infinite resist — keep normal XP bar.
		if (WingsFlightLogic.hasInfiniteResistance(wings)) {
			return false;
		}
		return mode == WingsFlightLogic.Mode.ELYTRA || mode == WingsFlightLogic.Mode.ICARUS;
	}

	/**
	 * @return {@code true} if the resist bar was drawn (caller should suppress the vanilla XP bar).
	 */
	public static boolean tryRender(GuiGraphics graphics) {
		if (!shouldReplaceXpBar()) {
			return false;
		}
		render(graphics);
		return true;
	}

	public static void render(GuiGraphics graphics) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null) {
			return;
		}
		ItemStack wings = WingsFlightLogic.getWornWings(player);
		if (wings.isEmpty()) {
			return;
		}

		float ratio = WingsFlightLogic.getStaminaRatio(wings);
		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();
		int left = screenWidth / 2 - 91;
		int top = screenHeight - 32 + 3;
		int filled = Mth.ceil(ratio * (BAR_WIDTH - 1));

		graphics.blitSprite(XP_BAR_BACKGROUND, left, top, BAR_WIDTH, BAR_HEIGHT);
		if (filled > 0) {
			// Sky-cyan tint so it reads as flight resist, not XP.
			graphics.setColor(0.25f, 0.85f, 1.0f, 1.0f);
			graphics.blitSprite(XP_BAR_PROGRESS, BAR_WIDTH, BAR_HEIGHT, 0, 0, left, top, filled, BAR_HEIGHT);
			graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}

		float remaining = WingsFlightLogic.getResistanceSeconds(wings) * ratio;
		String label = String.format("%.0fs", Math.max(0.0f, remaining));
		int labelX = screenWidth / 2 - mc.font.width(label) / 2;
		int labelY = top - 10;
		graphics.drawString(mc.font, label, labelX, labelY, 0x55FFFF, true);
	}
}
