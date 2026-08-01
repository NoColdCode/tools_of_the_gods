package net.mcreator.toolsofthegods.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.network.TraitSmithingTableMenu;
import net.mcreator.toolsofthegods.util.TraitSystem;

import java.util.ArrayList;
import java.util.List;

public class TraitSmithingTableScreen extends AbstractContainerScreen<TraitSmithingTableMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, "textures/screens/trait_binding_table.png");
	private static final int TEXTURE_WIDTH = 256;
	private static final int TEXTURE_HEIGHT = 256;
	private static final int PANEL_LEFT = 79;
	private static final int PANEL_TOP = 18;
	private static final int PANEL_RIGHT = 164;
	private static final int PANEL_BOTTOM = 90;
	private static final int PANEL_TEXT_X = PANEL_LEFT + 7;
	private static final int PANEL_TEXT_Y = PANEL_TOP + 7;
	private static final int PANEL_TEXT_WIDTH = PANEL_RIGHT - PANEL_LEFT - 12;
	private static final int PANEL_LINE_HEIGHT = 10;
	private static final int TRAIT_PIP_X = 8;
	private static final int TRAIT_PIP_Y = 68;
	private static final int TRAIT_PIP_SIZE = 8;
	private static final int TRAIT_PIP_GAP = 2;

	private String selectedTrait = "";
	private String traitPros = "";
	private String traitCons = "";
	private int emptyTraitSlots = 0;
	private int usedTraitSlots = 0;
	private Button applyButton;

	public TraitSmithingTableScreen(TraitSmithingTableMenu menu, Inventory playerInventory, Component component) {
		super(menu, playerInventory, component);
		this.imageWidth = 176;
		this.imageHeight = 205;
		this.inventoryLabelX = 8;
		this.inventoryLabelY = 110;
	}

	@Override
	protected void init() {
		super.init();
		int left = this.leftPos;
		int top = this.topPos;
		this.applyButton = this.addRenderableWidget(Button.builder(Component.literal("Bind"), button -> {
			if (this.minecraft != null && this.minecraft.gameMode != null) {
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, TraitSmithingTableMenu.APPLY_BUTTON_ID);
			}
		}).bounds(left + 12, top + 32, 42, 16).build());
		this.applyButton.active = this.menu.canApplySelectedTrait();
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
		int x = (this.width - this.imageWidth) / 2;
		int y = (this.height - this.imageHeight) / 2;

		guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		renderTraitSlotPips(guiGraphics, x, y);
	}

	private void renderTraitSlotPips(GuiGraphics guiGraphics, int guiLeft, int guiTop) {
		ItemStack tool = this.menu.getToolStack();
		if (tool.isEmpty()) {
			return;
		}

		int totalSlots = TraitSystem.getTotalTraitSlots(tool);
		if (totalSlots <= 0) {
			return;
		}

		TraitSystem.Trait nextTrait = TraitSystem.getNextBindableTrait(this.menu.getToolStack(), this.menu.getIngredientStack());
		int bindCost = TraitSystem.getAdditionalSlotCostForNextTrait(this.menu.getToolStack(), this.menu.getIngredientStack());
		int projectedUsed = this.usedTraitSlots + bindCost;

		for (int i = 0; i < totalSlots; i++) {
			int px = guiLeft + TRAIT_PIP_X + i * (TRAIT_PIP_SIZE + TRAIT_PIP_GAP);
			int py = guiTop + TRAIT_PIP_Y;
			int fillColor;
			if (i < this.usedTraitSlots) {
				fillColor = 0xFF4CAF50;
			} else if (nextTrait != null && i < projectedUsed) {
				fillColor = 0xFFFFC107;
			} else {
				fillColor = 0xFF5A5A5A;
			}
			guiGraphics.fill(px, py, px + TRAIT_PIP_SIZE, py + TRAIT_PIP_SIZE, fillColor);
			guiGraphics.fill(px, py, px + TRAIT_PIP_SIZE, py + 1, 0xFF2A2A2A);
			guiGraphics.fill(px, py + TRAIT_PIP_SIZE - 1, px + TRAIT_PIP_SIZE, py + TRAIT_PIP_SIZE, 0xFF2A2A2A);
			guiGraphics.fill(px, py, px + 1, py + TRAIT_PIP_SIZE, 0xFF2A2A2A);
			guiGraphics.fill(px + TRAIT_PIP_SIZE - 1, py, px + TRAIT_PIP_SIZE, py + TRAIT_PIP_SIZE, 0xFF2A2A2A);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
		refreshPreview();
		guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
		guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

		int rightY = PANEL_TEXT_Y;

		if (!selectedTrait.isEmpty()) {
			rightY = drawWrappedLines(guiGraphics, selectedTrait, PANEL_TEXT_X, rightY, 0xFFF2B5, true) + 2;

			if (!traitPros.isEmpty()) {
				for (String line : traitPros.split("\n")) {
					if (!line.isEmpty()) {
						rightY = drawWrappedLines(guiGraphics, line, PANEL_TEXT_X + 2, rightY, 0xC8FFC1, true);
					}
				}
			}

			if (!traitCons.isEmpty()) {
				rightY += 2;
				for (String line : traitCons.split("\n")) {
					if (!line.isEmpty()) {
						rightY = drawWrappedLines(guiGraphics, line, PANEL_TEXT_X + 2, rightY, 0xFFC3C3, true);
					}
				}
			}
		} else {
			rightY += 5;
			rightY = drawWrappedLines(guiGraphics, "Tool + Ingredient", PANEL_TEXT_X, rightY, 0xD0D0D0, false) + 1;
		}

		int totalSlots = this.menu.getToolStack().isEmpty() ? 0 : TraitSystem.getTotalTraitSlots(this.menu.getToolStack());
		TraitSystem.Trait nextTrait = TraitSystem.getNextBindableTrait(this.menu.getToolStack(), this.menu.getIngredientStack());
		int bindCost = TraitSystem.getAdditionalSlotCostForNextTrait(this.menu.getToolStack(), this.menu.getIngredientStack());
		int projectedUsed = usedTraitSlots + bindCost;
		boolean canApply = nextTrait != null && projectedUsed <= totalSlots;
		int slotsColor = canApply ? 0xA5F6A8 : 0xFF9B9B;
		String slotText = "Slots " + usedTraitSlots + "/" + totalSlots;
		if (nextTrait != null) {
			slotText += "  (Bind +" + bindCost + " -> " + projectedUsed + "/" + totalSlots + ")";
		}
		guiGraphics.drawString(this.font, slotText, PANEL_TEXT_X, PANEL_BOTTOM - 11, slotsColor, true);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		refreshPreview();
		this.renderBackground(guiGraphics, pMouseX, pMouseY, pPartialTick);
		super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(guiGraphics, pMouseX, pMouseY);
	}

	private void refreshPreview() {
		if (TraitSystem.isTraitPurgeIngredient(this.menu.getIngredientStack())) {
			TraitSystem.Trait purgeTarget = TraitSystem.getTraitToPurge(this.menu.getToolStack());
			if (!this.menu.getToolStack().isEmpty()) {
				this.usedTraitSlots = TraitSystem.getUsedTraitSlots(this.menu.getToolStack());
				this.emptyTraitSlots = TraitSystem.getEmptyTraitSlots(this.menu.getToolStack());
			} else {
				this.usedTraitSlots = 0;
				this.emptyTraitSlots = 0;
			}
			if (purgeTarget == null) {
				this.selectedTrait = "";
				this.traitPros = "";
				this.traitCons = "No traits to remove";
				if (this.applyButton != null) {
					this.applyButton.active = false;
					this.applyButton.setMessage(Component.literal("Purge"));
				}
				return;
			}
			this.selectedTrait = "Purge: " + purgeTarget.displayName();
			this.traitPros = "Removes the last bound trait";
			this.traitCons = "Clears toggle state for removed traits";
			if (this.applyButton != null) {
				this.applyButton.active = this.menu.canApplySelectedTrait();
				this.applyButton.setMessage(Component.literal("Purge"));
			}
			return;
		}

		if (this.applyButton != null) {
			this.applyButton.setMessage(Component.literal("Bind"));
		}

		TraitSystem.Trait nextTrait = TraitSystem.getNextBindableTrait(this.menu.getToolStack(), this.menu.getIngredientStack());
		if (!this.menu.getToolStack().isEmpty()) {
			this.emptyTraitSlots = TraitSystem.getEmptyTraitSlots(this.menu.getToolStack());
			this.usedTraitSlots = TraitSystem.getUsedTraitSlots(this.menu.getToolStack());
		} else {
			this.emptyTraitSlots = 0;
			this.usedTraitSlots = 0;
		}

		if (nextTrait == null) {
			this.selectedTrait = "";
			this.traitPros = "";
			this.traitCons = "Insert valid items";
			if (this.applyButton != null) {
				this.applyButton.active = false;
			}
			return;
		}

		this.selectedTrait = nextTrait.displayName();
		if (nextTrait == TraitSystem.Trait.AUTOSMELT) {
			this.traitPros = "Smelt blocks\nSmelt mob loot";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.SOULBOUND) {
			this.traitPros = "Kept on death";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.POISON_I) {
			this.traitPros = "Poison I for 5s";
			this.traitCons = "-20% Atk Spd";
		} else if (nextTrait == TraitSystem.Trait.POISON_II) {
			this.traitPros = "Poison I for 10s";
			this.traitCons = "-30% Atk Spd";
		} else if (nextTrait == TraitSystem.Trait.SUSTAINING_I) {
			this.traitPros = "+1 Food / 30s (held)\n+1 Saturation / 20s";
			this.traitCons = "Half speed on armor\n-20% Loots";
		} else if (nextTrait == TraitSystem.Trait.SUSTAINING_II) {
			this.traitPros = "+1 Food / 20s (held)\n+2 Saturation / 15s";
			this.traitCons = "Half speed on armor\n-35% Loots";
		} else if (nextTrait == TraitSystem.Trait.SUSTAINING_III) {
			this.traitPros = "+2 Food / 30s (held)\n+2 Saturation / 10s";
			this.traitCons = "Half speed on armor\n-50% Loots";
		} else if (nextTrait == TraitSystem.Trait.SHARPY_I) {
			this.traitPros = "+2 Damage";
			this.traitCons = "-30% Atk Spd\n-30% Mine Spd";
		} else if (nextTrait == TraitSystem.Trait.SHARPY_II) {
			this.traitPros = "+4 Damage";
			this.traitCons = "-50% Atk Spd\n-50% Mine Spd";
		} else if (nextTrait == TraitSystem.Trait.SHARPY_III) {
			this.traitPros = "+6 Damage";
			this.traitCons = "-80% Atk Spd\n-80% Mine Spd";
		} else if (nextTrait == TraitSystem.Trait.MODIFIABLE_I) {
			this.traitPros = "+1 Trait Slot";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.MAGNETIC_I) {
			this.traitPros = "Attract items 5 blocks";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.SILKY_I) {
			this.traitPros = "Silk touch always on";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.SILKY_II) {
			this.traitPros = "Silk touch toggleable";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.AUTOSMELT_II) {
			this.traitPros = "Autosmelt + toggleable";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.FREEZY_I) {
			this.traitPros = "Slowness I for 10s on hit";
			this.traitCons = "-0.5 Damage";
		} else if (nextTrait == TraitSystem.Trait.FREEZY_II) {
			this.traitPros = "Slowness II for 15s on hit";
			this.traitCons = "-1 Damage";
		} else if (nextTrait == TraitSystem.Trait.MOMENTUM_I) {
			this.traitPros = "+25% Mine Spd at max\n(50 blocks)";
			this.traitCons = "Resets on idle";
		} else if (nextTrait == TraitSystem.Trait.MOMENTUM_II) {
			this.traitPros = "+40% Mine Spd at max\n(40 blocks)";
			this.traitCons = "Resets on idle";
		} else if (nextTrait == TraitSystem.Trait.BROAD_TOUCH_I) {
			this.traitPros = "3x3/5x5 area mine\nFell tree (12 logs)";
			this.traitCons = "2 slot cost";
		} else if (nextTrait == TraitSystem.Trait.BROAD_TOUCH_II) {
			this.traitPros = "5x5/9x9 area mine\nFell tree (64 logs)\nMode selection";
			this.traitCons = "4 slot cost";
		} else if (nextTrait == TraitSystem.Trait.FRENZY_I) {
			this.traitPros = "+10% Atk Spd\n+6% Mine Spd";
			this.traitCons = "-4% Atk Dmg";
		} else if (nextTrait == TraitSystem.Trait.FRENZY_II) {
			this.traitPros = "+18% Atk Spd\n+12% Mine Spd";
			this.traitCons = "-8% Atk Dmg";
		} else if (nextTrait == TraitSystem.Trait.FRENZY_III) {
			this.traitPros = "+28% Atk Spd\n+18% Mine Spd";
			this.traitCons = "-12% Atk Dmg";
		} else if (nextTrait == TraitSystem.Trait.TITAN_I) {
			this.traitPros = "+2 Damage";
			this.traitCons = "-15% Atk Spd\n-10% Mine Spd";
		} else if (nextTrait == TraitSystem.Trait.TITAN_II) {
			this.traitPros = "+4 Damage";
			this.traitCons = "-30% Atk Spd\n-20% Mine Spd";
		} else if (nextTrait == TraitSystem.Trait.SCHOLAR_I) {
			this.traitPros = "+10% XP gain";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.SCHOLAR_II) {
			this.traitPros = "+20% XP gain";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.MOONLIT_I) {
			this.traitPros = "At night: +12% Mine Spd";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.MOONLIT_II) {
			this.traitPros = "At night: +20% Mine Spd";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.RANGER_I) {
			this.traitPros = "Bow handling focus\n+5% XP gain";
			this.traitCons = "Bow/Ultimate only";
		} else if (nextTrait == TraitSystem.Trait.RANGER_II) {
			this.traitPros = "Bow handling focus\n+10% XP gain";
			this.traitCons = "Bow/Ultimate only";
		} else if (nextTrait == TraitSystem.Trait.BOUNTIFUL_I) {
			this.traitPros = "Small chance to\nduplicate block drops";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.SCAVENGER_I) {
			this.traitPros = "Auto-collects drops\ninto inventory";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.PURIFYING_I) {
			this.traitPros = "Periodically removes\none negative effect";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.SWIFTSTEP_I) {
			this.traitPros = "Speed I while held/worn";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.BULWARK_I) {
			this.traitPros = "Resistance I while held/worn";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.THORNS_I) {
			this.traitPros = "Reflects 2 melee damage\nwhen you are hit";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.THORNS_II) {
			this.traitPros = "Reflects 5 melee damage\nwhen you are hit";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.FIREWARD_I) {
			this.traitPros = "Fire Resistance I\nwhile worn";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.FIREWARD_II) {
			this.traitPros = "Fire Resistance II\nwhile worn";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.GUARDIAN_I) {
			this.traitPros = "+12% block chance\n+10% block power";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.GUARDIAN_II) {
			this.traitPros = "+22% block chance\n+18% block power";
			this.traitCons = "";
		} else if (nextTrait == TraitSystem.Trait.REPULSE_I) {
			this.traitPros = "Knock back nearby foes\n(right-click shield)";
			this.traitCons = "";
		} else if (hasStatPreview(nextTrait)) {
			this.traitPros = String.format("+%.0f%% Atk Spd\n+%.0f%% Mine Spd", nextTrait.attackSpeedBonus() * 100.0f, nextTrait.miningSpeedBonus() * 100.0f);
			this.traitCons = String.format("-%.0f%% Atk Dmg\n-%.0f%% XP", nextTrait.attackDamagePenalty() * 100.0f, nextTrait.xpPenalty() * 100.0f);
		} else {
			this.traitPros = nextTrait.description();
			this.traitCons = "";
		}
		if (this.applyButton != null) {
			this.applyButton.active = this.menu.canApplySelectedTrait();
		}
	}

	private static boolean hasStatPreview(TraitSystem.Trait trait) {
		return trait.attackSpeedBonus() != 0.0f
			|| trait.miningSpeedBonus() != 0.0f
			|| trait.attackDamagePenalty() != 0.0f
			|| trait.attackDamageBonusFlat() != 0.0f;
	}

	private int drawWrappedLines(GuiGraphics guiGraphics, String text, int x, int y, int color, boolean shadow) {
		int currentY = y;
		for (String line : wrapText(text, PANEL_TEXT_WIDTH - (x - PANEL_TEXT_X))) {
			guiGraphics.drawString(this.font, line, x, currentY, color, shadow);
			currentY += PANEL_LINE_HEIGHT;
		}
		return currentY;
	}

	private List<String> wrapText(String text, int maxWidth) {
		List<String> lines = new ArrayList<>();
		for (String paragraph : text.split("\\n")) {
			if (paragraph.isEmpty()) {
				lines.add("");
				continue;
			}

			StringBuilder currentLine = new StringBuilder();
			for (String word : paragraph.split(" ")) {
				String candidate = currentLine.isEmpty() ? word : currentLine + " " + word;
				if (!currentLine.isEmpty() && this.font.width(candidate) > maxWidth) {
					lines.add(currentLine.toString());
					currentLine = new StringBuilder(word);
				} else {
					currentLine = new StringBuilder(candidate);
				}
			}
			if (!currentLine.isEmpty()) {
				lines.add(currentLine.toString());
			}
		}
		return lines;
	}
}
