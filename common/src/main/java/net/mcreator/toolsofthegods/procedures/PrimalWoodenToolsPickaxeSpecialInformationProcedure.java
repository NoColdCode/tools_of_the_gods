package net.mcreator.toolsofthegods.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.util.ShieldStrainHelper;
import net.mcreator.toolsofthegods.util.TierSystem;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

public class PrimalWoodenToolsPickaxeSpecialInformationProcedure {
	public static String execute(ItemStack itemstack) {
		int currentXp = (int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("xp");
		ToolProgressionHelper.ToolType type = ToolProgressionHelper.getToolType(itemstack);
		ToolProgressionHelper.ensureInitialized(itemstack);
		int level = (int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level");
		int xpNeeded = ToolProgressionHelper.getXpForNextLevel(type, level);
		int tier = ToolProgressionHelper.getStoredTier(itemstack);
		int maxLevel = ToolProgressionHelper.getMaxLevel(type);
		boolean needsUpgrade = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("needsUpgrade");
		
		// Calculate filled bars for XP progress
		double filledBars = xpNeeded > 0 ? Math.round(((double) currentXp / xpNeeded) * 10) : 0;
		if (filledBars > 10) filledBars = 10;
		
		// Build progress bar
		StringBuilder bar = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			if (i < filledBars) {
				bar.append("§a■");
			} else {
				bar.append("§7□");
			}
		}
		
		// Calculate speed for relevant tools
		float miningSpeed = ToolProgressionHelper.getEffectiveMiningSpeed(itemstack);
		
		// Build tooltip
		StringBuilder tooltip = new StringBuilder();
		tooltip.append("§6§l").append(ToolProgressionHelper.getDisplayName(itemstack)).append("\n");
		tooltip.append("§7Level: §f").append(level).append(" §7/ §f").append(maxLevel).append("\n");
		
		if (needsUpgrade) {
			tooltip.append("§c§lNEEDS UPGRADE!").append("\n");
			tooltip.append("§e").append(TierSystem.getUpgradeMaterialMessage(tier + 1, ToolProgressionHelper.usesArmorProgression(type))).append("\n");
		} else if (level < maxLevel) {
			tooltip.append("§7XP: §f").append(currentXp).append(" §7/ §f").append(xpNeeded).append("\n");
			tooltip.append("§7Progress: ").append(bar).append("\n");
		} else {
			tooltip.append("§d§lMAX LEVEL").append("\n");
		}
		
		if (type == ToolProgressionHelper.ToolType.PICKAXE || type == ToolProgressionHelper.ToolType.HAMMER || type == ToolProgressionHelper.ToolType.AXE || type == ToolProgressionHelper.ToolType.SHOVEL || type == ToolProgressionHelper.ToolType.HOE) {
			tooltip.append("§7Tool Speed: §b").append(String.format("%.2fx", miningSpeed));
			tooltip.append("\n§7Attack Damage: §c").append(String.format("%.2f", ToolProgressionHelper.getEstimatedAttackDamage(itemstack)));
		} else if (type == ToolProgressionHelper.ToolType.SWORD) {
			tooltip.append("§7Attack Damage: §c").append(String.format("%.2f", ToolProgressionHelper.getEstimatedAttackDamage(itemstack)));
		} else if (type == ToolProgressionHelper.ToolType.FLAIL) {
			tooltip.append("§7Attack Damage: §c").append(String.format("%.2f", ToolProgressionHelper.getEstimatedAttackDamage(itemstack)));
			tooltip.append("\n§7Stun: §d").append(String.format("%.1f", net.mcreator.toolsofthegods.util.FlailCombatHelper.getStunSecondsForDisplay(level))).append("s");
			tooltip.append("\n§7Attack Speed: §b").append(String.format("%.2f", net.mcreator.toolsofthegods.util.FlailCombatHelper.getDisplayedAttackSpeed(level)));
		} else if (type == ToolProgressionHelper.ToolType.SPEAR) {
			tooltip.append("§7Attack Damage: §c").append(String.format("%.2f", ToolProgressionHelper.getEstimatedAttackDamage(itemstack)));
			tooltip.append(" §8(< sword)");
			tooltip.append("\n§7Thrust Reach: §b").append(String.format("%.1f", net.mcreator.toolsofthegods.util.SpearThrustHelper.getDisplayedReach(itemstack))).append(" blocks");
		} else if (type == ToolProgressionHelper.ToolType.ARMOR && itemstack.getItem() instanceof ArmorItem armorItem) {
			tooltip.append("§7Armor: §9").append(String.format("%.1f", ToolProgressionHelper.getArmorDefensePoints(itemstack, armorItem.getType())));
			float toughness = ToolProgressionHelper.getArmorToughnessValue(itemstack);
			if (toughness > 0f) {
				tooltip.append(" §7Toughness: §9").append(String.format("%.1f", toughness));
			}
			tooltip.append("\n§8Wear for slow XP · take hits for faster XP");
		} else if (type == ToolProgressionHelper.ToolType.SHIELD) {
			int shieldLevel = ToolProgressionHelper.getShieldLevel(itemstack);
			float chance = ToolProgressionHelper.getShieldBlockChance(itemstack) * 100f;
			float power = ToolProgressionHelper.getShieldBlockReduction(itemstack) * 100f;
			float strainCap = ShieldStrainHelper.getStrainCapacity(itemstack);
			int recSec = ShieldStrainHelper.getStrainRecoveryTicks(shieldLevel) / 20;
			float reflect = ShieldStrainHelper.getReflectRatio(itemstack) * 100f;
			tooltip.append("§7Block §b").append(String.format("%.0f%%", chance))
				.append(" §7· Power §b").append(String.format("%.0f%%", power)).append("\n");
			tooltip.append("§7Strain §c").append(String.format("%.0f", strainCap))
				.append(" §7· Recov §e").append(recSec).append("s")
				.append(" §7· Return §6").append(String.format("%.0f%%", reflect));
		} else if (type == ToolProgressionHelper.ToolType.WINGS) {
			tooltip.append("§7Mode: §d").append(net.mcreator.toolsofthegods.logic.WingsFlightLogic.getModeLabel(itemstack));
			tooltip.append("\n§7Glide: §b").append(String.format("%.0f%%", net.mcreator.toolsofthegods.logic.WingsFlightLogic.getGlide(itemstack) * 100f));
			tooltip.append(" §7· Resist: §e").append(String.format("%.0fs", net.mcreator.toolsofthegods.logic.WingsFlightLogic.getResistanceSeconds(itemstack)));
			tooltip.append("\n§7Turn: §b").append(String.format("%.0f%%", net.mcreator.toolsofthegods.logic.WingsFlightLogic.getTurnSpeed(itemstack) * 100f));
			float flyHeight = net.mcreator.toolsofthegods.logic.WingsFlightLogic.getFlyHeight(itemstack);
			if (flyHeight > 0.01f) {
				tooltip.append(" §7· Fly Height: §e").append(String.format("%.1f", flyHeight));
			} else {
				tooltip.append(" §7· Cape fall cushion");
			}
			if (level <= 20) {
				tooltip.append("\n§8Lv 0–20: slows falls, less fall damage");
			} else if (level <= 60) {
				tooltip.append("\n§8Lv 21–60: elytra glide (faster toward 60)");
			} else {
				tooltip.append("\n§8Lv 61–100: look up to climb (Icarus)");
			}
		}

		for (Component line : TraitSystem.getTraitTooltip(itemstack)) {
			tooltip.append("\n").append(line.getString());
		}
		
		return tooltip.toString();
	}
}