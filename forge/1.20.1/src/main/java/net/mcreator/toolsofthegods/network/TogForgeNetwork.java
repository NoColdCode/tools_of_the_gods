package net.mcreator.toolsofthegods.network;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.client.GuideBookClient;

public final class TogForgeNetwork {
	private static final String PROTOCOL = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(TogModConstants.MODID, "main"),
		() -> PROTOCOL,
		PROTOCOL::equals,
		PROTOCOL::equals
	);

	private static int nextId;

	private TogForgeNetwork() {
	}

	public static void register() {
		CHANNEL.messageBuilder(OpenGuideBookPacket.class, nextId++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder((packet, buf) -> {
			})
			.decoder(buf -> OpenGuideBookPacket.INSTANCE)
			.consumerMainThread((packet, ctx) -> {
				ctx.get().enqueueWork(GuideBookClient::open);
				ctx.get().setPacketHandled(true);
			})
			.add();
	}
}
