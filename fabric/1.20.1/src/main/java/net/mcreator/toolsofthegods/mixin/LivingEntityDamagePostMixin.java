package net.mcreator.toolsofthegods.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.toolsofthegods.logic.ShieldProgressionLogic;
import net.mcreator.toolsofthegods.platform.fabric.FabricEventAdapters;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamagePostMixin {
	@Inject(method = "hurt", at = @At("TAIL"))
	private void tools_of_the_gods$afterDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.level().isClientSide() || !cir.getReturnValue()) {
			return;
		}
		boolean blocked = self.isBlocking() && amount > 0f;
		float damageTaken = blocked ? 0f : amount;
		ShieldProgressionLogic.onDamagePost(FabricEventAdapters.livingDamagePost(self, damageTaken, blocked ? amount : 0f));
	}
}
