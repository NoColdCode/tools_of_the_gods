package net.mcreator.toolsofthegods.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** Drinkable elixir that applies a temporary tool-XP multiplier aura. */
public class XpElixirItem extends Item {
	private final Supplier<TogRegistryEntry<MobEffect>> effectEntry;
	private final int durationTicks;
	private final float multiplier;
	private final String effectLabel;

	public XpElixirItem(Properties properties, Supplier<TogRegistryEntry<MobEffect>> effectEntry,
		int durationTicks, float multiplier, String effectLabel) {
		super(properties);
		this.effectEntry = effectEntry;
		this.durationTicks = durationTicks;
		this.multiplier = multiplier;
		this.effectLabel = effectLabel;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			MobEffect effect = effectEntry.get().get();
			Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
			player.addEffect(new MobEffectInstance(holder, durationTicks, 0, false, true, true));
			level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.0f);
			player.displayClientMessage(Component.literal("§d" + effectLabel + " §7active — tools gain §a×"
				+ trim(multiplier) + " §7XP"), true);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResult.SUCCESS;
	}

	private static String trim(float value) {
		if (value == (int) value) {
			return Integer.toString((int) value);
		}
		return Float.toString(value);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(Component.literal("§7Drink to gain §a×" + trim(multiplier) + " §7tool XP"));
		tooltipAdder.accept(Component.literal("§8Duration: " + (durationTicks / 20) + "s — affects held/worn TOG gear"));
	}
}
