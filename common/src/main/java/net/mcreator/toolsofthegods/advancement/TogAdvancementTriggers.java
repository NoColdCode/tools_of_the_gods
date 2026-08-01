package net.mcreator.toolsofthegods.advancement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.toolsofthegods.TogModConstants;

public final class TogAdvancementTriggers {
	private static final ResourceLocation FIRST_TRAIT = ResourceLocation.fromNamespaceAndPath(TogModConstants.MODID, "first_trait");

	private TogAdvancementTriggers() {
	}

	public static void triggerTraitBound(ServerPlayer player) {
		AdvancementHolder advancement = player.server.getAdvancements().get(FIRST_TRAIT);
		if (advancement != null) {
			player.getAdvancements().award(advancement, "trait_bound");
		}
	}
}
