package net.mcreator.toolsofthegods.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

import net.mcreator.toolsofthegods.logic.context.TogLivingDropsContext;
import net.mcreator.toolsofthegods.platform.fabric.FabricEventAdapters;
import net.mcreator.toolsofthegods.platform.fabric.FabricGameplayEvents;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingDropsMixin {
	@Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
	private void tools_of_the_gods$beforeDeathLoot(DamageSource damageSource, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.level.isClientSide) {
			return;
		}
		List<ItemEntity> drops = new ArrayList<>();
		TogLivingDropsContext ctx = FabricEventAdapters.livingDrops(self, damageSource, drops);
		FabricGameplayEvents.dispatchLivingDrops(ctx);
		FabricGameplayEvents.dispatchLivingDeath(self, damageSource);
	}
}
