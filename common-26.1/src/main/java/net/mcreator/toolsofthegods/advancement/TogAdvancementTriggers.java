package net.mcreator.toolsofthegods.advancement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.toolsofthegods.TogModConstants;

public final class TogAdvancementTriggers {
	private static final Identifier FIRST_TRAIT = Identifier.fromNamespaceAndPath(TogModConstants.MODID, "first_trait");

	private TogAdvancementTriggers() {
	}

	public static void triggerTraitBound(ServerPlayer player) {
		AdvancementHolder advancement = player.level().getServer().getAdvancements().get(FIRST_TRAIT);
		if (advancement != null) {
			player.getAdvancements().award(advancement, "trait_bound");
		}
	}
}
