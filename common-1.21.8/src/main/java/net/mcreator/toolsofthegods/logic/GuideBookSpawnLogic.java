package net.mcreator.toolsofthegods.logic;
import net.mcreator.toolsofthegods.logic.context.TogPlayerCloneContext;









import net.minecraft.nbt.CompoundTag;

import net.minecraft.nbt.Tag;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.TickTask;

import net.minecraft.world.entity.player.Inventory;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;



import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.util.TogPlayerData;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;

import net.mcreator.toolsofthegods.item.TogGuideBookItem;



import java.util.Set;

import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;




public final class GuideBookSpawnLogic {

	private static final String NBT_GIVEN = "guide_book_received";

	private static final int GIVE_DELAY_TICKS = 5;

	private static final Set<UUID> pendingGive = ConcurrentHashMap.newKeySet();




	public static void onPlayerLogin(ServerPlayer player) {
		scheduleGive(player);
	}

	public static void onPlayerJoinLevel(ServerPlayer player) {
		if (player.level().isClientSide()) {
			return;
		}
		scheduleGive(player);
	}




	public static void onPlayerClone(TogPlayerCloneContext ctx) {

		if (!ctx.wasDeath()) {

			return;

		}

		CompoundTag original = readModData(ctx.original());

		if (original.getBooleanOr(NBT_GIVEN, false)) {

			writeModData(ctx.newPlayer(), original);

		}

	}



	private static void scheduleGive(ServerPlayer player) {

		if (!pendingGive.add(player.getUUID())) {

			return;

		}

		player.getServer().schedule(new TickTask(player.getServer().getTickCount() + GIVE_DELAY_TICKS, () -> {

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

		TogModConstants.LOGGER.debug("Gave TOG guide book to {}", player.getName().getString());

	}



	private static boolean hasReceivedGuide(Player player) {

		if (readModData(player).getBooleanOr(NBT_GIVEN, false)) {

			return true;

		}

		// Migrate legacy flag from earlier versions

		if (TogPlayerData.get(player).getBooleanOr("togGuideBookGiven", false)) {

			markGuideReceived(player);

			TogPlayerData.get(player).remove("togGuideBookGiven");

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

		CompoundTag root = TogPlayerData.get(player);

		if (root.contains(TogModConstants.MODID)) {

			return root.getCompoundOrEmpty(TogModConstants.MODID);

		}

		return new CompoundTag();

	}



	private static void writeModData(Player player, CompoundTag mod) {

		TogPlayerData.get(player).put(TogModConstants.MODID, mod);

	}

}

