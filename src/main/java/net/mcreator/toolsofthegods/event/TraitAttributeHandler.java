package net.mcreator.toolsofthegods.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.util.FlailCombatHelper;
import net.mcreator.toolsofthegods.util.TogEquipmentHelper;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

@EventBusSubscriber(modid = ToolsOfTheGodsMod.MODID)
public class TraitAttributeHandler {
	private static final ResourceLocation SPEEDY_ATTACK_SPEED_ID = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "speedy_attack_speed");
	private static final ResourceLocation SPEEDY_ATTACK_DAMAGE_ID = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "speedy_attack_damage");
	private static final ResourceLocation SHARPY_DAMAGE_ID = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "sharpy_damage");
	private static final ResourceLocation ARMOR_ATTACK_SPEED_ID = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "armor_attack_speed");
	private static final ResourceLocation ARMOR_ATTACK_DAMAGE_ID = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "armor_attack_damage");
	private static final ResourceLocation FLAIL_ATTACK_SPEED_ID = ResourceLocation.fromNamespaceAndPath(ToolsOfTheGodsMod.MODID, "flail_attack_speed");

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		ItemStack heldItem = player.getMainHandItem();

		AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);

		if (attackSpeed == null || attackDamage == null) {
			return;
		}

		attackSpeed.removeModifier(SPEEDY_ATTACK_SPEED_ID);
		attackDamage.removeModifier(SPEEDY_ATTACK_DAMAGE_ID);
		attackDamage.removeModifier(SHARPY_DAMAGE_ID);
		attackSpeed.removeModifier(ARMOR_ATTACK_SPEED_ID);
		attackDamage.removeModifier(ARMOR_ATTACK_DAMAGE_ID);
		attackSpeed.removeModifier(FLAIL_ATTACK_SPEED_ID);

		if (ToolProgressionHelper.isTogTool(heldItem)) {
			applyToolModifiers(attackSpeed, attackDamage, heldItem);
		}

		if (ToolProgressionHelper.getToolType(heldItem) == ToolProgressionHelper.ToolType.FLAIL) {
			int level = FlailCombatHelper.getLevel(heldItem);
			double flailSpeed = FlailCombatHelper.getAttackSpeedModifier(level);
			double baseItemSpeed = -3.5d;
			double extraSlow = flailSpeed - baseItemSpeed;
			if (extraSlow < 0.0d) {
				attackSpeed.addTransientModifier(new AttributeModifier(FLAIL_ATTACK_SPEED_ID, extraSlow, AttributeModifier.Operation.ADD_VALUE));
			}
		}

		float armorSpeedBonus = Math.min(1.0f, TogEquipmentHelper.sumWornArmorFloat(player, TraitSystem::getAttackSpeedBonus));
		float armorDamagePenalty = Math.min(0.9f, TogEquipmentHelper.sumWornArmorFloat(player, TraitSystem::getAttackDamagePenalty));

		if (armorSpeedBonus > 0.0f) {
			attackSpeed.addTransientModifier(new AttributeModifier(ARMOR_ATTACK_SPEED_ID, armorSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		if (armorDamagePenalty > 0.0f) {
			attackDamage.addTransientModifier(new AttributeModifier(ARMOR_ATTACK_DAMAGE_ID, -armorDamagePenalty, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
	}

	private static void applyToolModifiers(AttributeInstance attackSpeed, AttributeInstance attackDamage, ItemStack heldItem) {
		double attackSpeedBonus = TraitSystem.getAttackSpeedBonus(heldItem);
		double attackDamagePenalty = TraitSystem.getAttackDamagePenalty(heldItem);
		double sharpyDamageBonus = TraitSystem.getAttackDamageBonusFlat(heldItem);

		if (attackSpeedBonus != 0.0d) {
			attackSpeed.addTransientModifier(new AttributeModifier(SPEEDY_ATTACK_SPEED_ID, attackSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		if (attackDamagePenalty > 0.0d) {
			attackDamage.addTransientModifier(new AttributeModifier(SPEEDY_ATTACK_DAMAGE_ID, -attackDamagePenalty, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		if (sharpyDamageBonus != 0.0d) {
			attackDamage.addTransientModifier(new AttributeModifier(SHARPY_DAMAGE_ID, sharpyDamageBonus, AttributeModifier.Operation.ADD_VALUE));
		}
	}
}
