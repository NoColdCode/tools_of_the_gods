package net.mcreator.toolsofthegods.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.logic.PickaxePowerLogic;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

public record ActivatePickaxePowerMessage() implements CustomPacketPayload {
	public static final Type<ActivatePickaxePowerMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "activate_pickaxe_power"));
	public static final StreamCodec<FriendlyByteBuf, ActivatePickaxePowerMessage> STREAM_CODEC = StreamCodec.unit(new ActivatePickaxePowerMessage());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handleData(final ActivatePickaxePowerMessage message, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				PickaxePowerLogic.activatePower(player);
			}
		});
	}
}
