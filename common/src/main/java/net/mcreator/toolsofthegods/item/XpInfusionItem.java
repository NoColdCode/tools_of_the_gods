package net.mcreator.toolsofthegods.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.util.XpBoostHelper;

import java.util.List;

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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand == InteractionHand.OFF_HAND) {
			player.displayClientMessage(Component.literal("§cHold this in your main hand and a TOG tool in your offhand."), true);
			return InteractionResultHolder.fail(stack);
		}
		if (!XpBoostHelper.applyOffhandXp(level, player, xpAmount)) {
			player.displayClientMessage(Component.literal("§cHold a Tools of the Gods item in your offhand."), true);
			return InteractionResultHolder.fail(stack);
		}
		if (!level.isClientSide()) {
			level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.8f, 1.1f);
			player.displayClientMessage(Component.literal("§a+" + xpAmount + " XP infused into offhand tool."), true);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal("§7Right-click while holding a TOG tool in offhand"));
		tooltip.add(Component.literal("§aGrants +" + xpAmount + " tool XP"));
	}
}
