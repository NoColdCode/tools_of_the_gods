package net.mcreator.toolsofthegods.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.util.XpBoostHelper;

import java.util.function.Consumer;

/** Consumable that grants flat XP to the TOG tool in the offhand. */
public class XpInfusionItem extends Item {
	private final int xpAmount;

	public XpInfusionItem(Properties properties, int xpAmount) {
		super(properties);
		this.xpAmount = xpAmount;
	}

	public int getXpAmount() {
		return xpAmount;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand == InteractionHand.OFF_HAND) {
			player.displayClientMessage(Component.literal("§cHold this in your main hand and a TOG tool in your offhand."), true);
			return InteractionResult.FAIL;
		}
		if (!XpBoostHelper.applyOffhandXp(level, player, xpAmount)) {
			player.displayClientMessage(Component.literal("§cHold a Tools of the Gods item in your offhand."), true);
			return InteractionResult.FAIL;
		}
		if (!level.isClientSide()) {
			level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.1f);
			player.displayClientMessage(Component.literal("§a+" + xpAmount + " XP infused into offhand tool."), true);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(Component.literal("§7Right-click while holding a TOG tool in offhand"));
		tooltipAdder.accept(Component.literal("§aGrants +" + xpAmount + " tool XP"));
	}
}
