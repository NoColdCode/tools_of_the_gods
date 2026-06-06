package net.mcreator.toolsofthegods.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.client.TogGuideBookScreen;

/**
 * Server → Client packet that tells the client to open the custom guide book screen.
 * Registered with playToClient so the handler is only ever invoked on the client side.
 */
public record OpenGuideBookPayload() implements CustomPacketPayload {

	public static final Type<OpenGuideBookPayload> TYPE =
		new Type<>(ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "open_guide_book"));

	public static final StreamCodec<FriendlyByteBuf, OpenGuideBookPayload> STREAM_CODEC =
		StreamCodec.unit(new OpenGuideBookPayload());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	/** Called on the CLIENT only (registered via playToClient). */
	public static void handleData(OpenGuideBookPayload msg, IPayloadContext ctx) {
		ctx.enqueueWork(() -> Minecraft.getInstance().setScreen(new TogGuideBookScreen()));
	}
}
