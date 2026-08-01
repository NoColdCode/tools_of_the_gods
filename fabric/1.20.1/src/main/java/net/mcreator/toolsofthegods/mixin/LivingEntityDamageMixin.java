package net.mcreator.toolsofthegods.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.toolsofthegods.platform.fabric.FabricEventAdapters;
import net.mcreator.toolsofthegods.platform.fabric.FabricGameplayEvents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
	@ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float tools_of_the_gods$modifyIncomingDamage(float amount, DamageSource source) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.level().isClientSide()) {
			return amount;
		}
		return FabricGameplayEvents.dispatchIncomingDamage(FabricEventAdapters.incomingDamage(self, source, amount));
	}
}
