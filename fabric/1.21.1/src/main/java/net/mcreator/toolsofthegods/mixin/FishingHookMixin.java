package net.mcreator.toolsofthegods.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.platform.fabric.FabricGameplayEvents;

import java.util.ArrayList;
import java.util.List;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
	@Inject(method = "retrieve", at = @At("TAIL"))
	private void tools_of_the_gods$afterRetrieve(ItemStack usedRod, CallbackInfo ci) {
		FishingHook self = (FishingHook) (Object) this;
		if (!(self.getPlayerOwner() instanceof Player player) || player.level().isClientSide()) {
			return;
		}
		List<ItemStack> drops = new ArrayList<>();
		FabricGameplayEvents.dispatchItemFished(player, drops);
	}
}
