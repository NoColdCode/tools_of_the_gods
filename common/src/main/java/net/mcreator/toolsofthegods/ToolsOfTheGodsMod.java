package net.mcreator.toolsofthegods;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.util.Tuple;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModTabs;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMobEffects;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlocks;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModBlockEntities;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModMenus;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.network.ActivatePickaxePowerMessage;
import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class ToolsOfTheGodsMod {
	public static final Logger LOGGER = LogManager.getLogger(ToolsOfTheGodsMod.class);
	public static final String MODID = "tools_of_the_gods";
	public static final String VERSION = "1.4.1";

	/** Mod version from metadata when loaded, otherwise {@link #VERSION}. */
	public static String getDisplayVersion() {
		return net.neoforged.fml.ModList.get().getModContainerById(MODID)
			.map(container -> container.getModInfo().getVersion().toString())
			.orElse(VERSION);
	}

	public ToolsOfTheGodsMod(IEventBus modEventBus) {
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, ToolsOfTheGodsCommonConfig.SPEC, "tools_of_the_gods-common.toml");
		// Start of user code block mod constructor
		addNetworkMessage(ActivatePickaxePowerMessage.TYPE, ActivatePickaxePowerMessage.STREAM_CODEC, ActivatePickaxePowerMessage::handleData);
		addNetworkMessage(net.mcreator.toolsofthegods.network.SetUltimateToolModePayload.TYPE,
			net.mcreator.toolsofthegods.network.SetUltimateToolModePayload.STREAM_CODEC,
			net.mcreator.toolsofthegods.network.SetUltimateToolModePayload::handleData);
		addNetworkMessage(net.mcreator.toolsofthegods.network.SetStaffSpellPayload.TYPE,
			net.mcreator.toolsofthegods.network.SetStaffSpellPayload.STREAM_CODEC,
			net.mcreator.toolsofthegods.network.SetStaffSpellPayload::handleData);
		addS2CNetworkMessage(net.mcreator.toolsofthegods.network.OpenGuideBookPayload.TYPE, net.mcreator.toolsofthegods.network.OpenGuideBookPayload.STREAM_CODEC, net.mcreator.toolsofthegods.network.OpenGuideBookPayload::handleData);
		// End of user code block mod constructor
		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(this::registerNetworking);
		// End of user code block mod init
	}
	private static boolean networkingRegistered = false;
	private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();
	private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> S2C_MESSAGES = new HashMap<>();

	private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
	}

	public static <T extends CustomPacketPayload> void addNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
		if (networkingRegistered)
			throw new IllegalStateException("Cannot register new network messages after networking has been registered");
		MESSAGES.put(id, new NetworkMessage<>(reader, handler));
	}

	public static <T extends CustomPacketPayload> void addS2CNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
		if (networkingRegistered)
			throw new IllegalStateException("Cannot register new network messages after networking has been registered");
		S2C_MESSAGES.put(id, new NetworkMessage<>(reader, handler));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerNetworking(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MODID);
		MESSAGES.forEach((id, networkMessage) -> registrar.playToServer(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler()));
		S2C_MESSAGES.forEach((id, networkMessage) -> registrar.playToClient(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler()));
		networkingRegistered = true;
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new Tuple<>(action, tick));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}
}