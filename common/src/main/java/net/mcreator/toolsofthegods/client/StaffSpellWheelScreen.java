package net.mcreator.toolsofthegods.client;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.network.SetStaffSpellPayload;
import net.mcreator.toolsofthegods.util.StaffSpell;
import net.mcreator.toolsofthegods.util.StaffSpellHelper;
import net.mcreator.toolsofthegods.util.UltimateToolMode;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;

import java.util.ArrayList;
import java.util.List;

public class StaffSpellWheelScreen extends Screen {
	private static final int WHEEL_RADIUS = 78;
	private static final int ICON_RADIUS = 62;
	private static final int ICON_SIZE = 20;

	private final List<StaffSpell> spells = new ArrayList<>();
	private int hoveredIndex = -1;
	private int selectedIndex = -1;
	private boolean closing;

	public StaffSpellWheelScreen() {
		super(Component.literal("Staff Spells"));
		ItemStack staff = getHeldStaffStack();
		spells.addAll(StaffSpellHelper.getUnlockedSpells(staff));
		StaffSpell current = StaffSpellHelper.getSelectedSpell(staff);
		selectedIndex = Math.max(0, spells.indexOf(current));
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
		hoveredIndex = getIndexAt(mouseX, mouseY, cx, cy, spells.size());

		gfx.fill(cx - WHEEL_RADIUS - 4, cy - WHEEL_RADIUS - 4, cx + WHEEL_RADIUS + 4, cy + WHEEL_RADIUS + 4, 0xCC1A2E1A);
		gfx.fill(cx - WHEEL_RADIUS, cy - WHEEL_RADIUS, cx + WHEEL_RADIUS, cy + WHEEL_RADIUS, 0xDD2A402A);

		if (spells.isEmpty()) {
			gfx.drawString(this.font, "No spells unlocked", cx - 50, cy - 4, 0xFFFFFF, false);
			return;
		}

		double slice = (Math.PI * 2.0d) / spells.size();
		for (int i = 0; i < spells.size(); i++) {
			double midAngle = -Math.PI / 2.0d + slice * i + slice / 2.0d;
			int iconX = cx + (int) (Math.cos(midAngle) * ICON_RADIUS) - ICON_SIZE / 2;
			int iconY = cy + (int) (Math.sin(midAngle) * ICON_RADIUS) - ICON_SIZE / 2;

			boolean hovered = i == hoveredIndex;
			boolean selected = i == selectedIndex;
			int bg = selected ? 0xFF4AD94A : (hovered ? 0xFF5ACD6A : 0xAA305030);
			gfx.fill(iconX - 3, iconY - 3, iconX + ICON_SIZE + 3, iconY + ICON_SIZE + 3, bg);

			ItemStack icon = new ItemStack(spells.get(i).iconItem());
			gfx.renderItem(icon, iconX, iconY);
			gfx.renderItemDecorations(this.font, icon, iconX, iconY);
		}

		StaffSpell labelSpell = hoveredIndex >= 0 ? spells.get(hoveredIndex)
			: (selectedIndex >= 0 ? spells.get(selectedIndex) : spells.get(0));
		String title = labelSpell.displayName();
		int tw = this.font.width(title);
		gfx.fill(cx - tw / 2 - 6, cy - 8, cx + tw / 2 + 6, cy + 10, 0xDD000000);
		gfx.drawString(this.font, title, cx - tw / 2, cy - 4, 0xFFFFFF, false);
		gfx.drawString(this.font, "Release G to confirm", cx - 52, cy + WHEEL_RADIUS + 14, 0xAAAAAA, false);
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
		if (spells.isEmpty()) {
			return;
		}
		int index = hoveredIndex >= 0 ? hoveredIndex : selectedIndex;
		if (index < 0 || index >= spells.size()) {
			return;
		}
		StaffSpell spell = spells.get(index);
		ItemStack staff = getHeldStaffStack();
		StaffSpellHelper.setSelectedSpell(staff, spell);
		PacketDistributor.sendToServer(new SetStaffSpellPayload(spell.ordinal()));
	}

	static ItemStack getHeldStaffStack() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return ItemStack.EMPTY;
		}
		ItemStack main = mc.player.getMainHandItem();
		if (main.is(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get())) {
			return main;
		}
		ItemStack off = mc.player.getOffhandItem();
		if (off.is(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get())) {
			return off;
		}
		if (main.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())
			&& UltimateToolModeHelper.getMode(main) == UltimateToolMode.STAFF) {
			return main;
		}
		if (off.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())
			&& UltimateToolModeHelper.getMode(off) == UltimateToolMode.STAFF) {
			return off;
		}
		return ItemStack.EMPTY;
	}

	static boolean isHoldingStaffSelector(Minecraft mc) {
		return !getHeldStaffStack().isEmpty();
	}

	private static int getIndexAt(int mouseX, int mouseY, int cx, int cy, int count) {
		if (count <= 0) {
			return -1;
		}
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
		int index = (int) (angle / (Math.PI * 2.0d / count));
		return Math.min(count - 1, Math.max(0, index));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
