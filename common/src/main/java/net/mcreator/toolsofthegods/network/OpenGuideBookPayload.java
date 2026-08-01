package net.mcreator.toolsofthegods.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

/**
 * Server → Client packet that tells the client to open the custom guide book screen.
 * Registered with playToClient so the handler is only ever invoked on the client side.
 */
public record OpenGuideBookPayload() implements CustomPacketPayload {

	public static final Type<OpenGuideBookPayload> TYPE =
		new Type<>(ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "open_guide_book"));

	public static final StreamCodec<FriendlyByteBuf, OpenGuideBookPayload> STREAM_CODEC =
		StreamCodec.unit(new OpenGuideBookPayload());

	private static final String GUIDE_BOOK_CLIENT = "net.mcreator.toolsofthegods.client.GuideBookClient";

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	/** Called on the CLIENT only (registered via playToClient). */
	public static void handleData(OpenGuideBookPayload msg, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			if (FMLEnvironment.dist != Dist.CLIENT) {
				return;
			}
			try {
				Class.forName(GUIDE_BOOK_CLIENT).getMethod("open").invoke(null);
			} catch (ReflectiveOperationException e) {
				ToolsOfTheGodsMod.LOGGER.error("Failed to open TOG guide book on client", e);
			}
		});
	}
}
