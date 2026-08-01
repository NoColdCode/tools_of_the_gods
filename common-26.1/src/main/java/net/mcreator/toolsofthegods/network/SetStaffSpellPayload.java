package net.mcreator.toolsofthegods.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.StaffSpell;
import net.mcreator.toolsofthegods.util.StaffSpellHelper;
import net.mcreator.toolsofthegods.util.UltimateToolMode;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public record SetStaffSpellPayload(int spellOrdinal) implements CustomPacketPayload {
	public static final Type<SetStaffSpellPayload> TYPE = new Type<>(
		Identifier.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "set_staff_spell"));
	public static final StreamCodec<FriendlyByteBuf, SetStaffSpellPayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, SetStaffSpellPayload::spellOrdinal,
		SetStaffSpellPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handleData(final SetStaffSpellPayload message, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (!(context.player() instanceof ServerPlayer player)) {
				return;
			}
			ItemStack stack = findStaffStack(player);
			if (stack.isEmpty()) {
				return;
			}
			StaffSpell spell = StaffSpell.fromOrdinal(message.spellOrdinal());
			if (!spell.isUnlocked(StaffSpellHelper.getStaffLevel(stack))) {
				return;
			}
			StaffSpellHelper.setSelectedSpell(stack, spell);
			player.sendOverlayMessage(
				net.minecraft.network.chat.Component.literal("\u00a7dSpell: \u00a7f" + spell.displayName()));
		});
	}

	private static ItemStack findStaffStack(ServerPlayer player) {
		ItemStack main = player.getMainHandItem();
		if (main.is(ToolsOfTheGodsModItems.STAFF_OF_THE_GODS.get())) {
			return main;
		}
		ItemStack off = player.getOffhandItem();
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
}
