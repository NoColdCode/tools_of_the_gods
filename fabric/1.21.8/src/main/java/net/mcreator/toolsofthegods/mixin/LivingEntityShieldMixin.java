package net.mcreator.toolsofthegods.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.toolsofthegods.platform.fabric.FabricEventAdapters;
import net.mcreator.toolsofthegods.platform.fabric.FabricGameplayEvents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityShieldMixin {
	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"))
	private void tools_of_the_gods$beforeShield(DamageSource source, float amount, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.level().isClientSide()) {
			return;
		}
		var ctx = FabricEventAdapters.shieldBlock(self, source, true, amount);
		FabricGameplayEvents.dispatchShieldBlock(ctx);
	}
}
