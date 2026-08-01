package net.mcreator.toolsofthegods.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;

import net.mcreator.toolsofthegods.item.TogGuideBookItem;

import java.util.List;

/**
 * Custom guide book screen – larger, dark grimoire style, 1.5× text scale.
 */
public class TogGuideBookScreen extends Screen {

	// ── Layout constants ────────────────────────────────────────────────────
	private static final float SCALE       = 1.5f;
	private static final int   LINE_H      = 10;   // line height in pre-scale units
	private static final int   PAD         = 14;   // horizontal padding
	private static final int   TOP_BAR     = 22;   // height of header bar
	private static final int   BOT_BAR     = 28;   // height of footer bar

	// ── Colours ─────────────────────────────────────────────────────────────
	private static final int COL_BORDER    = 0xFF8B5E2A;
	private static final int COL_BG        = 0xFF1E1008;
	private static final int COL_BG_INNER  = 0xFF271509;
	private static final int COL_SEP       = 0xFF6B4A1E;
	private static final int COL_HEADER    = 0xFFD4A017;
	private static final int COL_TEXT      = 0xFFEEE0C0;

	// ── State ────────────────────────────────────────────────────────────────
	private final List<Component> pages;
	private int currentPage = 0;

	// Computed layout (recalculated on every init/resize)
	private int panelX, panelY, panelW, panelH;
	private int textX,  textY;
	private int textAreaPreW;   // text area width in pre-scale coords
	private int maxLines;
	private int textAreaLeft, textAreaTop, textAreaRight, textAreaBottom;
	private int scrollLine = 0;
	private int maxScroll = 0;

	// Lines rendered for the current page (used by mouse-click hit-testing)
	private List<FormattedCharSequence> renderedLines = List.of();

	public TogGuideBookScreen() {
		super(Component.literal("Tools of the Gods Guide"));
		this.pages = TogGuideBookItem.getGuidePages();
	}

	// ── Lifecycle ────────────────────────────────────────────────────────────

	@Override
	protected void init() {
		panelW = Math.min(width  - 40, 420);
		panelH = Math.min(height - 40, 270);
		panelX = (width  - panelW) / 2;
		panelY = (height - panelH) / 2;

		textX = panelX + PAD;
		textY = panelY + TOP_BAR;
		textAreaLeft = textX - 4;
		textAreaTop = panelY + TOP_BAR;
		textAreaRight = panelX + panelW - PAD + 4;
		textAreaBottom = panelY + panelH - BOT_BAR;

		// Reserve a few pixels on the right for the scrollbar.
		textAreaPreW = (int)((panelW - PAD * 2 - 12) / SCALE);

		// Pixels available for text, divided by scaled line height
		int textAreaH = panelH - TOP_BAR - BOT_BAR;
		maxLines = (int)(textAreaH / (LINE_H * SCALE));

		rebuildButtons();
	}

	private void rebuildButtons() {
		clearWidgets();

		int bY = panelY + panelH - BOT_BAR + 6;

		// Prev
		addRenderableWidget(Button.builder(Component.literal("◄ Prev"), b -> navigate(-1))
			.bounds(panelX + 8, bY, 58, 16).build());

		// Next
		addRenderableWidget(Button.builder(Component.literal("Next ►"), b -> navigate(1))
			.bounds(panelX + panelW - 66, bY, 58, 16).build());

		// Close  (top-right corner)
		addRenderableWidget(Button.builder(Component.literal("✕"), b -> onClose())
			.bounds(panelX + panelW - 16, panelY + 4, 12, 12).build());
	}

	private void navigate(int delta) {
		int next = currentPage + delta;
		if (next >= 0 && next < pages.size()) {
			currentPage = next;
			scrollLine = 0;
		}
	}

	private void clampScroll() {
		if (scrollLine < 0) {
			scrollLine = 0;
		}
		if (scrollLine > maxScroll) {
			scrollLine = maxScroll;
		}
	}

	// ── Background ───────────────────────────────────────────────────────────

	@Override
	public void renderBackground(GuiGraphics gfx, int mx, int my, float dt) {
		// Intentionally empty: no blur and no fullscreen darkening.
		// The world remains fully visible behind the custom panel.
	}

	// ── Rendering ────────────────────────────────────────────────────────────

	@Override
	public void render(GuiGraphics gfx, int mx, int my, float dt) {
		// Draw non-blurred, non-darkened background layer hook (no-op by design)
		renderBackground(gfx, mx, my, dt);

		// Outer border
		gfx.fill(panelX - 3, panelY - 3, panelX + panelW + 3, panelY + panelH + 3, COL_BORDER);
		// Main background
		gfx.fill(panelX, panelY, panelX + panelW, panelY + panelH, COL_BG);
		// Inner text area, slightly lighter
		gfx.fill(textAreaLeft, textAreaTop, textAreaRight, textAreaBottom, COL_BG_INNER);

		// Top separator
		gfx.fill(panelX + 6, panelY + TOP_BAR - 2, panelX + panelW - 6, panelY + TOP_BAR - 1, COL_SEP);
		// Bottom separator
		gfx.fill(panelX + 6, panelY + panelH - BOT_BAR + 2, panelX + panelW - 6, panelY + panelH - BOT_BAR + 3, COL_SEP);

		// Page counter
		String label = "Page " + (currentPage + 1) + " of " + pages.size();
		gfx.drawCenteredString(font, label, panelX + panelW / 2, panelY + 6, COL_HEADER);

		// Page text (1.5× scale)
		if (currentPage < pages.size()) {
			Component page = pages.get(currentPage);
			renderedLines = font.split(page, textAreaPreW);
			maxScroll = Math.max(0, renderedLines.size() - maxLines);
			clampScroll();

			gfx.pose().pushPose();
			gfx.pose().translate(textX, textY + 2, 0);
			gfx.pose().scale(SCALE, SCALE, 1.0f);

			int count = Math.min(renderedLines.size() - scrollLine, maxLines);
			for (int i = 0; i < count; i++) {
				gfx.drawString(font, renderedLines.get(scrollLine + i), 0, i * LINE_H, COL_TEXT, false);
			}
			gfx.pose().popPose();

			if (maxScroll > 0) {
				int trackX1 = textAreaRight - 4;
				int trackX2 = textAreaRight - 2;
				int trackY1 = textAreaTop + 2;
				int trackY2 = textAreaBottom - 2;
				int trackH = trackY2 - trackY1;
				gfx.fill(trackX1, trackY1, trackX2, trackY2, 0xAA5A3C19);

				int thumbH = Math.max(12, Math.round((float) maxLines / renderedLines.size() * trackH));
				int thumbY = trackY1 + Math.round((float) scrollLine / maxScroll * (trackH - thumbH));
				gfx.fill(trackX1 - 1, thumbY, trackX2 + 1, thumbY + thumbH, 0xFFD4A017);
			}
		}

		// Buttons (rendered on top of everything)
		super.render(gfx, mx, my, dt);
	}

	// ── Mouse click: support CHANGE_PAGE click events on index links ─────────

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		if (btn == 0 && !renderedLines.isEmpty()) {
			if (mx < textAreaLeft || mx > textAreaRight || my < textAreaTop || my > textAreaBottom) {
				return super.mouseClicked(mx, my, btn);
			}

			// Map screen coordinates back to pre-scale text space
			double rx = (mx - textX) / SCALE;
			double ry = (my - (textY + 2)) / SCALE;

			if (rx >= 0 && ry >= 0) {
				int line = scrollLine + (int)(ry / LINE_H);
				if (line >= 0 && line < renderedLines.size() && line < scrollLine + maxLines) {
					Style style = font.getSplitter()
						.componentStyleAtWidth(renderedLines.get(line), (int) rx);
					if (style != null && style.getClickEvent() != null) {
						ClickEvent ce = style.getClickEvent();
						if (ce.getAction() == ClickEvent.Action.CHANGE_PAGE) {
							try {
								int pg = Integer.parseInt(ce.getValue()) - 1;
								if (pg >= 0 && pg < pages.size()) {
									currentPage = pg;
									scrollLine = 0;
									return true;
								}
							} catch (NumberFormatException ignored) {}
						}
					}
				}
			}
		}
		return super.mouseClicked(mx, my, btn);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double deltaX, double deltaY) {
		if (mx >= textAreaLeft && mx <= textAreaRight && my >= textAreaTop && my <= textAreaBottom && maxScroll > 0) {
			scrollLine -= (int) Math.signum(deltaY);
			clampScroll();
			return true;
		}
		return super.mouseScrolled(mx, my, deltaX, deltaY);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_UP) {
			scrollLine -= 1;
			clampScroll();
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_DOWN) {
			scrollLine += 1;
			clampScroll();
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
			scrollLine -= Math.max(1, maxLines - 2);
			clampScroll();
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
			scrollLine += Math.max(1, maxLines - 2);
			clampScroll();
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_HOME) {
			scrollLine = 0;
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_END) {
			scrollLine = maxScroll;
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
