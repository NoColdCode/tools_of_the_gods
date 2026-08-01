package net.mcreator.toolsofthegods.platform.fabric;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.mcreator.toolsofthegods.TogModConstants;

public final class FabricPlayerAttachments {
	public static final AttachmentType<CompoundTag> PERSISTENT_DATA = AttachmentRegistry.createDefaulted(
		new ResourceLocation(TogModConstants.MODID, "persistent_data"),
		CompoundTag::new
	);

	private FabricPlayerAttachments() {
	}

	public static CompoundTag get(Player player) {
		return player.getAttachedOrCreate(PERSISTENT_DATA);
	}
}
