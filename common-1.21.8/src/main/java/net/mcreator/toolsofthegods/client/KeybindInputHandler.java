package net.mcreator.toolsofthegods.client;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.network.ActivatePickaxePowerMessage;
import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TogFeatures;
import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID, value = Dist.CLIENT)
public class KeybindInputHandler {

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		while (ToolsOfTheGodsKeyMappings.ACTIVATE_PICKAXE_POWER.consumeClick()) {
			PacketDistributor.sendToServer(new ActivatePickaxePowerMessage());
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || !WheelInputHelper.isModeWheelKeyHeld()) {
			return;
		}

		if (mc.screen instanceof UltimateModeWheelScreen || mc.screen instanceof StaffSpellWheelScreen) {
			return;
		}
		if (mc.screen != null) {
			return;
		}

		if (isHoldingUltimate(mc)) {
			mc.setScreen(new UltimateModeWheelScreen());
		} else if (TogFeatures.extendedToolsEnabled() && StaffSpellWheelScreen.isHoldingStaffSelector(mc)) {
			mc.setScreen(new StaffSpellWheelScreen());
		}
	}

	private static boolean isHoldingUltimate(Minecraft mc) {
		ItemStack main = mc.player.getMainHandItem();
		if (main.is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get())) {
			return true;
		}
		return mc.player.getOffhandItem().is(ToolsOfTheGodsModItems.ULTIMATE_TOOL_OF_THE_GODS.get());
	}
}
