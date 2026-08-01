package net.mcreator.toolsofthegods.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.UltimateToolMode;
import net.mcreator.toolsofthegods.util.UltimateToolModeHelper;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public record SetUltimateToolModePayload(int modeOrdinal) implements CustomPacketPayload {
	public static final Type<SetUltimateToolModePayload> TYPE = new Type<>(
		ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "set_ultimate_tool_mode"));
	public static final StreamCodec<FriendlyByteBuf, SetUltimateToolModePayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, SetUltimateToolModePayload::modeOrdinal,
		SetUltimateToolModePayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handleData(final SetUltimateToolModePayload message, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (!(context.player() instanceof ServerPlayer player)) {
				return;
			}
			ItemStack stack = player.getMainHandItem();
			if (!stack.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
				stack = player.getOffhandItem();
			}
			if (!stack.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
				return;
			}
			UltimateToolMode mode = UltimateToolMode.fromOrdinal(message.modeOrdinal());
			UltimateToolModeHelper.setMode(stack, mode, true);
			player.displayClientMessage(
				net.minecraft.network.chat.Component.literal("\u00a7bTool Mode: \u00a7f" + mode.displayName()), true);
		});
	}
}
