package net.mcreator.toolsofthegods.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.toolsofthegods.platform.fabric.FabricGameplayEvents;

@Mixin(Player.class)
public abstract class PlayerBreakSpeedMixin {
	@Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
	private void tools_of_the_gods$modifyBreakSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
		Player self = (Player) (Object) this;
		if (self.level.isClientSide) {
			return;
		}
		cir.setReturnValue(FabricGameplayEvents.dispatchBreakSpeed(self, state, null, cir.getReturnValue()));
	}
}
