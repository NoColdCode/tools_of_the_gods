package net.mcreator.toolsofthegods;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import net.minecraft.util.Tuple;

import net.mcreator.toolsofthegods.config.ToolsOfTheGodsCommonConfig;
import net.mcreator.toolsofthegods.network.TogForgeNetwork;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

/** Forge loader entry for shared mod lifecycle (config, deferred server work). */
public class ToolsOfTheGodsMod {
	public static final Logger LOGGER = LogManager.getLogger(ToolsOfTheGodsMod.class);
	public static final String MODID = "tools_of_the_gods";
	public static final String VERSION = "1.4.1";

	public static String getDisplayVersion() {
		return net.minecraftforge.fml.ModList.get().getModContainerById(MODID)
			.map(container -> container.getModInfo().getVersion().toString())
			.orElse(VERSION);
	}

	public ToolsOfTheGodsMod(IEventBus modEventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ToolsOfTheGodsCommonConfig.SPEC, "tools_of_the_gods-common.toml");
		modEventBus.addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(TogForgeNetwork::register);
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new Tuple<>(action, tick));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
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
