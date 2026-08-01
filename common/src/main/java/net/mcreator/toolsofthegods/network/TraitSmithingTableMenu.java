package net.mcreator.toolsofthegods.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public class TraitSmithingTableMenu extends AbstractContainerMenu {
	public static final int TRAIT_SLOT = 0;
	public static final int INGREDIENT_SLOT = 1;
	public static final int TOTAL_SLOTS = 2;
	public static final int APPLY_BUTTON_ID = 0;
	private static final int TOOL_SLOT_X = 27;
	private static final int TOOL_SLOT_Y = 53;
	private static final int INGREDIENT_SLOT_X = 76;
	private static final int INGREDIENT_SLOT_Y = 53;
	private static final int PLAYER_INV_START_X = 8;
	private static final int PLAYER_INV_START_Y = 123;
	private static final int HOTBAR_START_Y = 181;

	private final Container container;

	public TraitSmithingTableMenu(int windowId, Inventory playerInventory) {
		this(windowId, playerInventory, new SimpleContainer(TOTAL_SLOTS));
	}

	public TraitSmithingTableMenu(int windowId, Inventory playerInventory, Container container) {
		super(ToolsOfTheGodsModMenus.TRAIT_SMITHING_TABLE_MENU.get(), windowId);
		checkContainerSize(container, TOTAL_SLOTS);
		this.container = container;

		container.startOpen(playerInventory.player);

		// Tool slot
		this.addSlot(new Slot(container, TRAIT_SLOT, TOOL_SLOT_X, TOOL_SLOT_Y) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return ToolProgressionHelper.isTogTool(stack);
			}
		});

		// Trait ingredient slot
		this.addSlot(new Slot(container, INGREDIENT_SLOT, INGREDIENT_SLOT_X, INGREDIENT_SLOT_Y) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return TraitSystem.isValidTraitIngredient(stack);
			}
		});

		// Player inventory
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, PLAYER_INV_START_X + col * 18, PLAYER_INV_START_Y + row * 18));
			}
		}

		// Player hotbar
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInventory, col, PLAYER_INV_START_X + col * 18, HOTBAR_START_Y));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(pIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemStack = slotStack.copy();

			if (pIndex < TOTAL_SLOTS) {
				// Move from block slots to player inventory
				if (!this.moveItemStackTo(slotStack, TOTAL_SLOTS, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				// Move from player inventory to block slots
				if (ToolProgressionHelper.isTogTool(slotStack)) {
					if (!this.moveItemStackTo(slotStack, TRAIT_SLOT, TRAIT_SLOT + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (TraitSystem.isValidTraitIngredient(slotStack)) {
					if (!this.moveItemStackTo(slotStack, INGREDIENT_SLOT, INGREDIENT_SLOT + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					return ItemStack.EMPTY;
				}
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemStack;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return this.container.stillValid(pPlayer);
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id != APPLY_BUTTON_ID) {
			return super.clickMenuButton(player, id);
		}

		if (!canApplySelectedTrait()) {
			return false;
		}

		ItemStack toolStack = this.container.getItem(TRAIT_SLOT);
		ItemStack ingredientStack = this.container.getItem(INGREDIENT_SLOT);
		boolean applied;
		if (TraitSystem.isTraitPurgeIngredient(ingredientStack)) {
			applied = TraitSystem.purgeTrait(toolStack, ingredientStack);
		} else {
			applied = TraitSystem.bindNextTrait(toolStack, ingredientStack);
			if (applied && player instanceof ServerPlayer serverPlayer) {
				net.mcreator.toolsofthegods.advancement.TogAdvancementTriggers.triggerTraitBound(serverPlayer);
			}
		}
		if (applied) {
			this.broadcastChanges();
		}
		return applied;
	}

	public boolean canApplySelectedTrait() {
		if (TraitSystem.isTraitPurgeIngredient(getIngredientStack())) {
			return TraitSystem.canPurgeTrait(getToolStack());
		}
		return TraitSystem.getNextBindableTrait(getToolStack(), getIngredientStack()) != null;
	}

	public ItemStack getToolStack() {
		return this.container.getItem(TRAIT_SLOT);
	}

	public ItemStack getIngredientStack() {
		return this.container.getItem(INGREDIENT_SLOT);
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}
}
