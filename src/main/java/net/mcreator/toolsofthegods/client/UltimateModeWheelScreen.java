package net.mcreator.toolsofthegods.client;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.network.SetUltimateToolModePayload;
import net.mcreator.toolsofthegods.util.UltimateToolMode;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;

import java.util.ArrayList;
import java.util.List;

public class UltimateModeWheelScreen extends Screen {
	private static final int WHEEL_RADIUS = 78;
	private static final int ICON_RADIUS = 62;
	private static final int ICON_SIZE = 20;

	private final List<UltimateToolMode> modes = new ArrayList<>();
	private int hoveredIndex = -1;
	private int selectedIndex = -1;
	private boolean closing;

	public UltimateModeWheelScreen() {
		super(Component.literal("Ultimate Tool Mode"));
		for (UltimateToolMode mode : UltimateToolMode.values()) {
			modes.add(mode);
		}
		selectedIndex = UltimateToolModeHelper.getMode(getHeldUltimate()).ordinal();
	}

	@Override
	public void tick() {
		super.tick();
		if (!closing && !WheelInputHelper.isModeWheelKeyHeld()) {
			closing = true;
			confirmSelection();
			if (this.minecraft != null) {
				this.minecraft.setScreen(null);
			}
		}
	}

	@Override
	public void renderBackground(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
		gfx.fill(0, 0, this.width, this.height, 0x88000000);
	}

	@Override
	public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(gfx, mouseX, mouseY, partialTick);

		int cx = this.width / 2;
		int cy = this.height / 2;

		hoveredIndex = getIndexAt(mouseX, mouseY, cx, cy);

		// Outer ring
		gfx.fill(cx - WHEEL_RADIUS - 4, cy - WHEEL_RADIUS - 4, cx + WHEEL_RADIUS + 4, cy + WHEEL_RADIUS + 4, 0xCC1A1A2E);
		gfx.fill(cx - WHEEL_RADIUS, cy - WHEEL_RADIUS, cx + WHEEL_RADIUS, cy + WHEEL_RADIUS, 0xDD2A2A40);

		double slice = (Math.PI * 2.0d) / modes.size();
		for (int i = 0; i < modes.size(); i++) {
			double midAngle = -Math.PI / 2.0d + slice * i + slice / 2.0d;
			int iconX = cx + (int) (Math.cos(midAngle) * ICON_RADIUS) - ICON_SIZE / 2;
			int iconY = cy + (int) (Math.sin(midAngle) * ICON_RADIUS) - ICON_SIZE / 2;

			boolean hovered = i == hoveredIndex;
			boolean selected = i == selectedIndex;
			int bg = selected ? 0xFF4A90D9 : (hovered ? 0xFF6A5ACD : 0xAA303050);
			gfx.fill(iconX - 3, iconY - 3, iconX + ICON_SIZE + 3, iconY + ICON_SIZE + 3, bg);

			ItemStack icon = modes.get(i).createIconStack();
			gfx.renderItem(icon, iconX, iconY);
			gfx.renderItemDecorations(this.font, icon, iconX, iconY);
		}

		// Center label
		UltimateToolMode labelMode = hoveredIndex >= 0 ? modes.get(hoveredIndex)
			: (selectedIndex >= 0 ? modes.get(selectedIndex) : UltimateToolMode.PICKAXE);
		String title = labelMode.displayName();
		int tw = this.font.width(title);
		gfx.fill(cx - tw / 2 - 6, cy - 8, cx + tw / 2 + 6, cy + 10, 0xDD000000);
		gfx.drawString(this.font, title, cx - tw / 2, cy - 4, 0xFFFFFF, false);
		gfx.drawString(this.font, "Release key to confirm", cx - 52, cy + WHEEL_RADIUS + 14, 0xAAAAAA, false);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (hoveredIndex >= 0) {
			selectedIndex = hoveredIndex;
			confirmSelection();
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	public void confirmSelection() {
		int index = hoveredIndex >= 0 ? hoveredIndex : selectedIndex;
		if (index < 0 || index >= modes.size()) {
			return;
		}
		UltimateToolMode mode = modes.get(index);
		UltimateToolModeHelper.setMode(getHeldUltimate(), mode, true);
		PacketDistributor.sendToServer(new SetUltimateToolModePayload(mode.ordinal()));
	}

	private ItemStack getHeldUltimate() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return ItemStack.EMPTY;
		}
		ItemStack main = mc.player.getMainHandItem();
		if (!main.isEmpty()) {
			return main;
		}
		return mc.player.getOffhandItem();
	}

	private static int getIndexAt(int mouseX, int mouseY, int cx, int cy) {
		double dx = mouseX - cx;
		double dy = mouseY - cy;
		double dist = Math.sqrt(dx * dx + dy * dy);
		if (dist < 24 || dist > WHEEL_RADIUS + 12) {
			return -1;
		}
		double angle = Math.atan2(dy, dx) + Math.PI / 2.0d;
		if (angle < 0) {
			angle += Math.PI * 2.0d;
		}
		int count = UltimateToolMode.values().length;
		int index = (int) (angle / (Math.PI * 2.0d / count));
		return Math.min(count - 1, Math.max(0, index));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
