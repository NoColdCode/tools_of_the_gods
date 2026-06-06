package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.item.TogGuideBookItem;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class GuideBookSpawnHandler {
	private static final String NBT_GIVEN = "guide_book_received";
	private static final int GIVE_DELAY_TICKS = 5;
	private static final Set<UUID> pendingGive = ConcurrentHashMap.newKeySet();

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			scheduleGive(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerJoinLevel(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide() || !(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		scheduleGive(player);
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (!event.isWasDeath()) {
			return;
		}
		CompoundTag original = readModData(event.getOriginal());
		if (original.getBoolean(NBT_GIVEN)) {
			writeModData(event.getEntity(), original);
		}
	}

	private static void scheduleGive(ServerPlayer player) {
		if (!pendingGive.add(player.getUUID())) {
			return;
		}
		player.server.tell(new TickTask(player.server.getTickCount() + GIVE_DELAY_TICKS, () -> {
			pendingGive.remove(player.getUUID());
			tryGiveGuideBook(player);
		}));
	}

	private static void tryGiveGuideBook(ServerPlayer player) {
		if (hasReceivedGuide(player)) {
			return;
		}
		if (playerHasGuide(player)) {
			markGuideReceived(player);
			return;
		}

		ItemStack guide = TogGuideBookItem.createPopulatedStack();
		if (!player.getInventory().add(guide)) {
			player.drop(guide, false);
		}
		markGuideReceived(player);
		ToolsOfTheGodsMod.LOGGER.debug("Gave TOG guide book to {}", player.getName().getString());
	}

	private static boolean hasReceivedGuide(Player player) {
		if (readModData(player).getBoolean(NBT_GIVEN)) {
			return true;
		}
		// Migrate legacy flag from earlier versions
		if (player.getPersistentData().getBoolean("togGuideBookGiven")) {
			markGuideReceived(player);
			player.getPersistentData().remove("togGuideBookGiven");
			return true;
		}
		return false;
	}

	private static void markGuideReceived(Player player) {
		CompoundTag mod = readModData(player);
		mod.putBoolean(NBT_GIVEN, true);
		writeModData(player, mod);
	}

	private static boolean playerHasGuide(Player player) {
		Inventory inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.is(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK.get())) {
				return true;
			}
		}
		return false;
	}

	private static CompoundTag readModData(Player player) {
		CompoundTag root = player.getPersistentData();
		if (root.contains(ToolsOfTheGodsMod.MODID, Tag.TAG_COMPOUND)) {
			return root.getCompound(ToolsOfTheGodsMod.MODID);
		}
		return new CompoundTag();
	}

	private static void writeModData(Player player, CompoundTag mod) {
		player.getPersistentData().put(ToolsOfTheGodsMod.MODID, mod);
	}
}
