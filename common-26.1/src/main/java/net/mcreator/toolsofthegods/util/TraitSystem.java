package net.mcreator.toolsofthegods.util;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TraitSystem {
	private static final String NBT_TRAITS = "togTraits";

	public enum Trait {
		SPEEDY_I("speedy_i", "Speedy I", "Atk speed +20%, mining speed +15%, atk damage -10%, XP -10%", 0.20f, 0.15f, 0.10f, 0.10f, 0.0f, 1),
		SPEEDY_II("speedy_ii", "Speedy II", "Atk speed +40%, mining speed +30%, atk damage -15%, XP -15%", 0.40f, 0.30f, 0.15f, 0.15f, 0.0f, 2),
		SPEEDY_III("speedy_iii", "Speedy III", "Atk speed +65%, mining speed +50%, atk damage -20%, XP -20%", 0.65f, 0.50f, 0.20f, 0.20f, 0.0f, 3),
		POISON_I("poison_i", "Poison I", "Applies Poison I for 5s on hit, atk speed -20%", -0.20f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		POISON_II("poison_ii", "Poison II", "Applies Poison I for 10s on hit, atk speed -30%", -0.30f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		SUSTAINING_I("sustaining_i", "Sustaining I", "Held: +1 food /30s, +1 sat /20s. Worn armor: half speed. Loot -20%", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SUSTAINING_II("sustaining_ii", "Sustaining II", "Held: +1 food /20s, +2 sat /15s. Worn armor: half speed. Loot -35%", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		SUSTAINING_III("sustaining_iii", "Sustaining III", "Held: +2 food /30s, +2 sat /10s. Worn armor: half speed. Loot -50%", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 3),
		AUTOSMELT("autosmelt", "Autosmelt", "Smelts broken blocks and earned loot automatically", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		AUTOSMELT_II("autosmelt_ii", "Autosmelt II", "Autosmelt, toggleable with Sneak+Right-Click", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SOULBOUND("soulbound", "Soulbound", "Tool is kept on death", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SILKY_I("silky_i", "Silky I", "Always mines with silk touch", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SILKY_II("silky_ii", "Silky II", "Silk touch, toggleable with Sneak+Right-Click", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SHARPY_I("sharpy_i", "Sharpy I", "+2 damage, -30% atk speed, -30% mining speed", -0.30f, -0.30f, 0.0f, 0.0f, 2.0f, 1),
		SHARPY_II("sharpy_ii", "Sharpy II", "+4 damage, -50% atk speed, -50% mining speed", -0.50f, -0.50f, 0.0f, 0.0f, 4.0f, 2),
		SHARPY_III("sharpy_iii", "Sharpy III", "+6 damage, -80% atk speed, -80% mining speed", -0.80f, -0.80f, 0.0f, 0.0f, 6.0f, 3),
		MODIFIABLE_I("modifiable_i", "Modifiable I", "+1 trait slot", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0),
		MAGNETIC_I("magnetic_i", "Magnetic I", "Attracts items within 5 blocks", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		FREEZY_I("freezy_i", "Freezy I", "Slowness I for 10s on hit, -0.5 damage", 0.0f, 0.0f, 0.0f, 0.0f, -0.5f, 1),
		FREEZY_II("freezy_ii", "Freezy II", "Slowness II for 15s on hit, -1 damage", 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 2),
		MOMENTUM_I("momentum_i", "Momentum I", "Mine 50 blocks to reach +25% mining speed; resets on idle", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		MOMENTUM_II("momentum_ii", "Momentum II", "Mine 40 blocks to reach +40% mining speed; resets on idle", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		BROAD_TOUCH_I("broad_touch_i", "Broad Touch I", "3x3 mine (pickaxe/shovel), 5x5 (hammer), fell tree 12 (axe)", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		BROAD_TOUCH_II("broad_touch_ii", "Broad Touch II", "Larger area with mode selection; fell tree 64 (axe)", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4),
		FRENZY_I("frenzy_i", "Frenzy I", "+10% atk speed, +6% mining speed, -4% atk damage", 0.10f, 0.06f, 0.04f, 0.0f, 0.0f, 1),
		FRENZY_II("frenzy_ii", "Frenzy II", "+18% atk speed, +12% mining speed, -8% atk damage", 0.18f, 0.12f, 0.08f, 0.0f, 0.0f, 2),
		FRENZY_III("frenzy_iii", "Frenzy III", "+28% atk speed, +18% mining speed, -12% atk damage", 0.28f, 0.18f, 0.12f, 0.0f, 0.0f, 3),
		TITAN_I("titan_i", "Titan I", "+2 damage, -15% atk speed, -10% mining speed", -0.15f, -0.10f, 0.0f, 0.0f, 2.0f, 1),
		TITAN_II("titan_ii", "Titan II", "+4 damage, -30% atk speed, -20% mining speed", -0.30f, -0.20f, 0.0f, 0.0f, 4.0f, 2),
		SCHOLAR_I("scholar_i", "Scholar I", "+10% XP gain", 0.0f, 0.0f, 0.0f, -0.10f, 0.0f, 1),
		SCHOLAR_II("scholar_ii", "Scholar II", "+20% XP gain", 0.0f, 0.0f, 0.0f, -0.20f, 0.0f, 2),
		INSIGHT_I("insight_i", "Insight I", "×1.5 tool XP gain", 0.0f, 0.0f, 0.0f, -0.5f, 0.0f, 1),
		INSIGHT_II("insight_ii", "Insight II", "×2 tool XP gain", 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 2),
		INSIGHT_III("insight_iii", "Insight III", "×3 tool XP gain", 0.0f, 0.0f, 0.0f, -2.0f, 0.0f, 3),
		MOONLIT_I("moonlit_i", "Moonlit I", "At night: +12% mining speed", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		MOONLIT_II("moonlit_ii", "Moonlit II", "At night: +20% mining speed", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		RANGER_I("ranger_i", "Ranger I", "Bow-focused handling, +5% XP gain", 0.0f, 0.0f, 0.0f, -0.05f, 0.0f, 1),
		RANGER_II("ranger_ii", "Ranger II", "Bow-focused handling, +10% XP gain", 0.0f, 0.0f, 0.0f, -0.10f, 0.0f, 2),
		BOUNTIFUL_I("bountiful_i", "Bountiful I", "Small chance to duplicate block drops", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SCAVENGER_I("scavenger_i", "Scavenger I", "Auto-collects drops into inventory", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		PURIFYING_I("purifying_i", "Purifying I", "Periodically clears one negative effect", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SWIFTSTEP_I("swiftstep_i", "Swiftstep I", "Grants Speed I while held or worn", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		BULWARK_I("bulwark_i", "Bulwark I", "Grants Resistance I while held or worn", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		THORNS_I("thorns_i", "Thorns I", "Reflects 2 damage to melee attackers when hit", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		THORNS_II("thorns_ii", "Thorns II", "Reflects 5 damage to melee attackers when hit", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		FIREWARD_I("fireward_i", "Fireward I", "Fire Resistance I while worn", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		FIREWARD_II("fireward_ii", "Fireward II", "Fire Resistance II while worn", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		GUARDIAN_I("guardian_i", "Guardian I", "+12% block chance, +10% block power", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		GUARDIAN_II("guardian_ii", "Guardian II", "+22% block chance, +18% block power", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		REPULSE_I("repulse_i", "Repulse I", "Right-click to knock back nearby foes", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		ANGLER_I("angler_i", "Angler I", "Better fishing loot, +10% fishing XP", 0.0f, 0.0f, 0.0f, -0.10f, 0.0f, 1),
		ANGLER_II("angler_ii", "Angler II", "Much better fishing loot, +20% fishing XP", 0.0f, 0.0f, 0.0f, -0.20f, 0.0f, 2),
		REEL_I("reel_i", "Reel I", "Fish bite ~40% faster", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		MARKSMAN_I("marksman_i", "Marksman I", "+1 bolt damage, +5% crossbow XP", 0.0f, 0.0f, 0.0f, -0.05f, 1.0f, 1),
		MARKSMAN_II("marksman_ii", "Marksman II", "+2 bolt damage, +10% crossbow XP", 0.0f, 0.0f, 0.0f, -0.10f, 2.0f, 2),
		QUICK_LOAD_I("quick_load_i", "Quick Load I", "25% faster crossbow reload", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		RETURNING_I("returning_i", "Returning I", "Thrown trident returns faster", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		RIPTIDE_I("riptide_i", "Riptide I", "+50% thrown damage in rain/water", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		IMPALER_I("impaler_i", "Impaler I", "+2 trident damage", 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 1),
		IMPALER_II("impaler_ii", "Impaler II", "+4 trident damage", 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 2),
		ARCANE_I("arcane_i", "Arcane I", "Stronger staff bolt, 20% shorter cooldown", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		ARCANE_II("arcane_ii", "Arcane II", "Much stronger bolt, 35% shorter cooldown", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		CHANNELING_I("channeling_i", "Channeling I", "Staff bolts call lightning in storms", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		AERODYNAMIC_I("aerodynamic_i", "Aerodynamic I", "Faster glide, +50% glide XP", 0.0f, 0.0f, 0.0f, -0.05f, 0.0f, 1),
		AERODYNAMIC_II("aerodynamic_ii", "Aerodynamic II", "Much faster glide, +100% glide XP", 0.0f, 0.0f, 0.0f, -0.10f, 0.0f, 2),
		FEATHERFALL_I("featherfall_i", "Featherfall I", "50% less fall damage while worn", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		ADAPTIVE_I("adaptive_i", "Adaptive I", "Auto-selects sword, pickaxe, shovel, rod… for context", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		HEMORRHAGE_I("hemorrhage_i", "Hemorrhage I", "Wither I for 3s on hit", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		HEMORRHAGE_II("hemorrhage_ii", "Hemorrhage II", "Wither I for 6s on hit", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		VOLLEY_I("volley_i", "Volley I", "15% chance to fire an extra projectile", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		VOLLEY_II("volley_ii", "Volley II", "30% chance to fire an extra projectile", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		HARVEST_I("harvest_i", "Harvest I", "12% bonus crop drops", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		HARVEST_II("harvest_ii", "Harvest II", "25% bonus crop drops", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		STEADFAST_I("steadfast_i", "Steadfast I", "25% less shield strain per block", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		STEADFAST_II("steadfast_ii", "Steadfast II", "50% less shield strain per block", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		SEARING_I("searing_i", "Searing I", "Sets targets on fire 3s on hit", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		SEARING_II("searing_ii", "Searing II", "Sets targets on fire 6s on hit", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		VITALITY_I("vitality_i", "Vitality I", "Heal 1 heart on kill", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		VITALITY_II("vitality_ii", "Vitality II", "Heal 2 hearts on kill", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		RIME_I("rime_i", "Rime I", "Slowness I on hit; +15% damage vs burning foes", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		RIME_II("rime_ii", "Rime II", "Slowness II on hit; +30% damage vs burning foes", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		EXECUTIONER_I("executioner_i", "Executioner I", "+25% damage vs foes below 35% health", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		EXECUTIONER_II("executioner_ii", "Executioner II", "+50% damage vs foes below 50% health", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		REAVING_I("reaving_i", "Reaving I", "+20% XP from kills", 0.0f, 0.0f, 0.0f, -0.20f, 0.0f, 1),
		REAVING_II("reaving_ii", "Reaving II", "+40% XP from kills", 0.0f, 0.0f, 0.0f, -0.40f, 0.0f, 2),
		STALKER_I("stalker_i", "Stalker I", "+20% projectile damage while sneaking", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		STALKER_II("stalker_ii", "Stalker II", "+40% projectile damage while sneaking", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		RIPOSTE_I("riposte_i", "Riposte I", "Reflects 3 damage when blocking", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		RIPOSTE_II("riposte_ii", "Riposte II", "Reflects 6 damage when blocking", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		CRUSHING_I("crushing_i", "Crushing I", "+12% mining speed on stone blocks", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1),
		CRUSHING_II("crushing_ii", "Crushing II", "+25% mining speed on stone blocks", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2),
		ANCIENT_I("ancient_i", "Ancient I", "+15% XP from broken blocks", 0.0f, 0.0f, 0.0f, -0.15f, 0.0f, 1),
		ANCIENT_II("ancient_ii", "Ancient II", "+30% XP from broken blocks", 0.0f, 0.0f, 0.0f, -0.30f, 0.0f, 2);

		private final String id;
		private final String name;
		private final String description;
		private final float attackSpeedBonus;
		private final float miningSpeedBonus;
		private final float attackDamagePenalty;
		private final float xpPenalty;
		private final float attackDamageBonusFlat;
		private final int slotCost;

		Trait(String id, String name, String description, float attackSpeedBonus, float miningSpeedBonus, float attackDamagePenalty, float xpPenalty, float attackDamageBonusFlat, int slotCost) {
			this.id = id;
			this.name = name;
			this.description = description;
			this.attackSpeedBonus = attackSpeedBonus;
			this.miningSpeedBonus = miningSpeedBonus;
			this.attackDamagePenalty = attackDamagePenalty;
			this.xpPenalty = xpPenalty;
			this.attackDamageBonusFlat = attackDamageBonusFlat;
			this.slotCost = slotCost;
		}

		public String id() {
			return id;
		}

		public String displayName() {
			return name;
		}

		public String description() {
			return description;
		}

		public float attackSpeedBonus() {
			return attackSpeedBonus;
		}

		public float miningSpeedBonus() {
			return miningSpeedBonus;
		}

		public float attackDamagePenalty() {
			return attackDamagePenalty;
		}

		public float xpPenalty() {
			return xpPenalty;
		}

		public float attackDamageBonusFlat() {
			return attackDamageBonusFlat;
		}

		public int slotCost() {
			return slotCost;
		}

		public static Trait byId(String id) {
			for (Trait trait : values()) {
				if (trait.id.equalsIgnoreCase(id)) {
					return trait;
				}
			}
			return null;
		}
	}

	public static int getTraitSlotsForTier(int tier) {
		if (tier <= 0) {
			return 0;
		}
		if (tier <= 2) {
			return 1;
		}
		if (tier <= 4) {
			return 2;
		}
		if (tier == 5) {
			return 3;
		}
		if (tier <= 7) {
			return 4;
		}
		if (tier == 8) {
			return 5;
		}
		return 6;
	}

	public static int getBonusTraitSlots(ItemStack stack) {
		return hasTrait(stack, Trait.MODIFIABLE_I) ? 1 : 0;
	}

	public static int getTotalTraitSlots(ItemStack stack) {
		int base = ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.ULTIMATE
			? 8 : getTraitSlotsForTier(ToolProgressionHelper.getStoredTier(stack));
		return base + getBonusTraitSlots(stack);
	}

	public static int getEmptyTraitSlots(ItemStack stack) {
		return Math.max(0, getTotalTraitSlots(stack) - getUsedTraitSlots(stack));
	}

	/** Slot cost for UI and limits; rank-II replacements keep the same family slot total. */
	public static int getBillableSlotCost(Trait trait) {
		return trait.slotCost();
	}

	public static int getUsedTraitSlots(ItemStack stack) {
		int used = 0;
		for (Trait trait : getTraits(stack)) {
			used += getBillableSlotCost(trait);
		}
		return used;
	}

	public static List<Trait> getTraits(ItemStack stack) {
		String raw = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(NBT_TRAITS).orElse("");
		if (raw == null || raw.isEmpty()) {
			return new ArrayList<>();
		}

		int bestSpeedyRank = 0;
		int bestPoisonRank = 0;
		int bestSustainingRank = 0;
		int bestSharpyRank = 0;
		int bestSilkyRank = 0;
		int bestFreezyRank = 0;
		int bestMomentumRank = 0;
		int bestBroadTouchRank = 0;
		int bestFrenzyRank = 0;
		int bestTitanRank = 0;
		int bestScholarRank = 0;
		int bestInsightRank = 0;
		int bestMoonlitRank = 0;
		int bestRangerRank = 0;
		boolean hasBountiful = false;
		boolean hasScavenger = false;
		boolean hasPurifying = false;
		boolean hasSwiftstep = false;
		boolean hasBulwark = false;
		int bestThornsRank = 0;
		int bestFirewardRank = 0;
		int bestGuardianRank = 0;
		boolean hasRepulse = false;
		int bestAnglerRank = 0;
		boolean hasReel = false;
		int bestMarksmanRank = 0;
		boolean hasQuickLoad = false;
		boolean hasReturning = false;
		boolean hasRiptide = false;
		int bestImpalerRank = 0;
		int bestArcaneRank = 0;
		boolean hasChanneling = false;
		int bestAerodynamicRank = 0;
		boolean hasFeatherfall = false;
		boolean hasAdaptive = false;
		int bestHemorrhageRank = 0;
		int bestVolleyRank = 0;
		int bestHarvestRank = 0;
		int bestSteadfastRank = 0;
		int bestSearingRank = 0;
		int bestVitalityRank = 0;
		int bestRimeRank = 0;
		int bestExecutionerRank = 0;
		int bestReavingRank = 0;
		int bestStalkerRank = 0;
		int bestRiposteRank = 0;
		int bestCrushingRank = 0;
		int bestAncientRank = 0;
		boolean hasAutosmelt = false;
		boolean hasAutosmeltII = false;
		boolean hasSoulbound = false;
		boolean hasModifiable = false;
		boolean hasMagnetic = false;
		for (String id : raw.split(",")) {
			Trait trait = Trait.byId(id.trim().toLowerCase(Locale.ROOT));
			if (trait == null) {
				continue;
			}
			if (trait == Trait.AUTOSMELT) {
				hasAutosmelt = true;
			} else if (trait == Trait.AUTOSMELT_II) {
				hasAutosmeltII = true;
			} else if (trait == Trait.SILKY_I) {
				bestSilkyRank = Math.max(bestSilkyRank, 1);
			} else if (trait == Trait.SILKY_II) {
				bestSilkyRank = Math.max(bestSilkyRank, 2);
			} else if (trait == Trait.SOULBOUND) {
				hasSoulbound = true;
			} else if (trait == Trait.MODIFIABLE_I) {
				hasModifiable = true;
			} else if (trait == Trait.MAGNETIC_I) {
				hasMagnetic = true;
			} else if (trait == Trait.POISON_I) {
				bestPoisonRank = Math.max(bestPoisonRank, 1);
			} else if (trait == Trait.POISON_II) {
				bestPoisonRank = Math.max(bestPoisonRank, 2);
			} else if (trait == Trait.SUSTAINING_I) {
				bestSustainingRank = Math.max(bestSustainingRank, 1);
			} else if (trait == Trait.SUSTAINING_II) {
				bestSustainingRank = Math.max(bestSustainingRank, 2);
			} else if (trait == Trait.SUSTAINING_III) {
				bestSustainingRank = Math.max(bestSustainingRank, 3);
			} else if (trait == Trait.SPEEDY_I) {
				bestSpeedyRank = Math.max(bestSpeedyRank, 1);
			} else if (trait == Trait.SPEEDY_II) {
				bestSpeedyRank = Math.max(bestSpeedyRank, 2);
			} else if (trait == Trait.SPEEDY_III) {
				bestSpeedyRank = Math.max(bestSpeedyRank, 3);
			} else if (trait == Trait.SHARPY_I) {
				bestSharpyRank = Math.max(bestSharpyRank, 1);
			} else if (trait == Trait.SHARPY_II) {
				bestSharpyRank = Math.max(bestSharpyRank, 2);
			} else if (trait == Trait.SHARPY_III) {
				bestSharpyRank = Math.max(bestSharpyRank, 3);
			} else if (trait == Trait.FREEZY_I) {
				bestFreezyRank = Math.max(bestFreezyRank, 1);
			} else if (trait == Trait.FREEZY_II) {
				bestFreezyRank = Math.max(bestFreezyRank, 2);
			} else if (trait == Trait.MOMENTUM_I) {
				bestMomentumRank = Math.max(bestMomentumRank, 1);
			} else if (trait == Trait.MOMENTUM_II) {
				bestMomentumRank = Math.max(bestMomentumRank, 2);
			} else if (trait == Trait.BROAD_TOUCH_I) {
				bestBroadTouchRank = Math.max(bestBroadTouchRank, 1);
			} else if (trait == Trait.BROAD_TOUCH_II) {
				bestBroadTouchRank = Math.max(bestBroadTouchRank, 2);
			} else if (trait == Trait.FRENZY_I) {
				bestFrenzyRank = Math.max(bestFrenzyRank, 1);
			} else if (trait == Trait.FRENZY_II) {
				bestFrenzyRank = Math.max(bestFrenzyRank, 2);
			} else if (trait == Trait.FRENZY_III) {
				bestFrenzyRank = Math.max(bestFrenzyRank, 3);
			} else if (trait == Trait.TITAN_I) {
				bestTitanRank = Math.max(bestTitanRank, 1);
			} else if (trait == Trait.TITAN_II) {
				bestTitanRank = Math.max(bestTitanRank, 2);
			} else if (trait == Trait.SCHOLAR_I) {
				bestScholarRank = Math.max(bestScholarRank, 1);
			} else if (trait == Trait.SCHOLAR_II) {
				bestScholarRank = Math.max(bestScholarRank, 2);
			} else if (trait == Trait.INSIGHT_I) {
				bestInsightRank = Math.max(bestInsightRank, 1);
			} else if (trait == Trait.INSIGHT_II) {
				bestInsightRank = Math.max(bestInsightRank, 2);
			} else if (trait == Trait.INSIGHT_III) {
				bestInsightRank = Math.max(bestInsightRank, 3);
			} else if (trait == Trait.MOONLIT_I) {
				bestMoonlitRank = Math.max(bestMoonlitRank, 1);
			} else if (trait == Trait.MOONLIT_II) {
				bestMoonlitRank = Math.max(bestMoonlitRank, 2);
			} else if (trait == Trait.RANGER_I) {
				bestRangerRank = Math.max(bestRangerRank, 1);
			} else if (trait == Trait.RANGER_II) {
				bestRangerRank = Math.max(bestRangerRank, 2);
			} else if (trait == Trait.BOUNTIFUL_I) {
				hasBountiful = true;
			} else if (trait == Trait.SCAVENGER_I) {
				hasScavenger = true;
			} else if (trait == Trait.PURIFYING_I) {
				hasPurifying = true;
			} else if (trait == Trait.SWIFTSTEP_I) {
				hasSwiftstep = true;
			} else if (trait == Trait.BULWARK_I) {
				hasBulwark = true;
			} else if (trait == Trait.THORNS_I) {
				bestThornsRank = Math.max(bestThornsRank, 1);
			} else if (trait == Trait.THORNS_II) {
				bestThornsRank = Math.max(bestThornsRank, 2);
			} else if (trait == Trait.FIREWARD_I) {
				bestFirewardRank = Math.max(bestFirewardRank, 1);
			} else if (trait == Trait.FIREWARD_II) {
				bestFirewardRank = Math.max(bestFirewardRank, 2);
			} else if (trait == Trait.GUARDIAN_I) {
				bestGuardianRank = Math.max(bestGuardianRank, 1);
			} else if (trait == Trait.GUARDIAN_II) {
				bestGuardianRank = Math.max(bestGuardianRank, 2);
			} else if (trait == Trait.REPULSE_I) {
				hasRepulse = true;
			} else if (trait == Trait.ANGLER_I) {
				bestAnglerRank = Math.max(bestAnglerRank, 1);
			} else if (trait == Trait.ANGLER_II) {
				bestAnglerRank = Math.max(bestAnglerRank, 2);
			} else if (trait == Trait.REEL_I) {
				hasReel = true;
			} else if (trait == Trait.MARKSMAN_I) {
				bestMarksmanRank = Math.max(bestMarksmanRank, 1);
			} else if (trait == Trait.MARKSMAN_II) {
				bestMarksmanRank = Math.max(bestMarksmanRank, 2);
			} else if (trait == Trait.QUICK_LOAD_I) {
				hasQuickLoad = true;
			} else if (trait == Trait.RETURNING_I) {
				hasReturning = true;
			} else if (trait == Trait.RIPTIDE_I) {
				hasRiptide = true;
			} else if (trait == Trait.IMPALER_I) {
				bestImpalerRank = Math.max(bestImpalerRank, 1);
			} else if (trait == Trait.IMPALER_II) {
				bestImpalerRank = Math.max(bestImpalerRank, 2);
			} else if (trait == Trait.ARCANE_I) {
				bestArcaneRank = Math.max(bestArcaneRank, 1);
			} else if (trait == Trait.ARCANE_II) {
				bestArcaneRank = Math.max(bestArcaneRank, 2);
			} else if (trait == Trait.CHANNELING_I) {
				hasChanneling = true;
			} else if (trait == Trait.AERODYNAMIC_I) {
				bestAerodynamicRank = Math.max(bestAerodynamicRank, 1);
			} else if (trait == Trait.AERODYNAMIC_II) {
				bestAerodynamicRank = Math.max(bestAerodynamicRank, 2);
			} else if (trait == Trait.FEATHERFALL_I) {
				hasFeatherfall = true;
			} else if (trait == Trait.ADAPTIVE_I) {
				hasAdaptive = true;
			} else if (trait == Trait.HEMORRHAGE_I) {
				bestHemorrhageRank = Math.max(bestHemorrhageRank, 1);
			} else if (trait == Trait.HEMORRHAGE_II) {
				bestHemorrhageRank = Math.max(bestHemorrhageRank, 2);
			} else if (trait == Trait.VOLLEY_I) {
				bestVolleyRank = Math.max(bestVolleyRank, 1);
			} else if (trait == Trait.VOLLEY_II) {
				bestVolleyRank = Math.max(bestVolleyRank, 2);
			} else if (trait == Trait.HARVEST_I) {
				bestHarvestRank = Math.max(bestHarvestRank, 1);
			} else if (trait == Trait.HARVEST_II) {
				bestHarvestRank = Math.max(bestHarvestRank, 2);
			} else if (trait == Trait.STEADFAST_I) {
				bestSteadfastRank = Math.max(bestSteadfastRank, 1);
			} else if (trait == Trait.STEADFAST_II) {
				bestSteadfastRank = Math.max(bestSteadfastRank, 2);
			} else if (trait == Trait.SEARING_I) {
				bestSearingRank = Math.max(bestSearingRank, 1);
			} else if (trait == Trait.SEARING_II) {
				bestSearingRank = Math.max(bestSearingRank, 2);
			} else if (trait == Trait.VITALITY_I) {
				bestVitalityRank = Math.max(bestVitalityRank, 1);
			} else if (trait == Trait.VITALITY_II) {
				bestVitalityRank = Math.max(bestVitalityRank, 2);
			} else if (trait == Trait.RIME_I) {
				bestRimeRank = Math.max(bestRimeRank, 1);
			} else if (trait == Trait.RIME_II) {
				bestRimeRank = Math.max(bestRimeRank, 2);
			} else if (trait == Trait.EXECUTIONER_I) {
				bestExecutionerRank = Math.max(bestExecutionerRank, 1);
			} else if (trait == Trait.EXECUTIONER_II) {
				bestExecutionerRank = Math.max(bestExecutionerRank, 2);
			} else if (trait == Trait.REAVING_I) {
				bestReavingRank = Math.max(bestReavingRank, 1);
			} else if (trait == Trait.REAVING_II) {
				bestReavingRank = Math.max(bestReavingRank, 2);
			} else if (trait == Trait.STALKER_I) {
				bestStalkerRank = Math.max(bestStalkerRank, 1);
			} else if (trait == Trait.STALKER_II) {
				bestStalkerRank = Math.max(bestStalkerRank, 2);
			} else if (trait == Trait.RIPOSTE_I) {
				bestRiposteRank = Math.max(bestRiposteRank, 1);
			} else if (trait == Trait.RIPOSTE_II) {
				bestRiposteRank = Math.max(bestRiposteRank, 2);
			} else if (trait == Trait.CRUSHING_I) {
				bestCrushingRank = Math.max(bestCrushingRank, 1);
			} else if (trait == Trait.CRUSHING_II) {
				bestCrushingRank = Math.max(bestCrushingRank, 2);
			} else if (trait == Trait.ANCIENT_I) {
				bestAncientRank = Math.max(bestAncientRank, 1);
			} else if (trait == Trait.ANCIENT_II) {
				bestAncientRank = Math.max(bestAncientRank, 2);
			}
		}
		List<Trait> traits = new ArrayList<>();
		if (bestSpeedyRank == 1) {
			traits.add(Trait.SPEEDY_I);
		} else if (bestSpeedyRank == 2) {
			traits.add(Trait.SPEEDY_II);
		} else if (bestSpeedyRank >= 3) {
			traits.add(Trait.SPEEDY_III);
		}
		if (bestPoisonRank == 1) {
			traits.add(Trait.POISON_I);
		} else if (bestPoisonRank >= 2) {
			traits.add(Trait.POISON_II);
		}
		if (bestSustainingRank == 1) {
			traits.add(Trait.SUSTAINING_I);
		} else if (bestSustainingRank == 2) {
			traits.add(Trait.SUSTAINING_II);
		} else if (bestSustainingRank >= 3) {
			traits.add(Trait.SUSTAINING_III);
		}
		if (bestSharpyRank == 1) {
			traits.add(Trait.SHARPY_I);
		} else if (bestSharpyRank == 2) {
			traits.add(Trait.SHARPY_II);
		} else if (bestSharpyRank >= 3) {
			traits.add(Trait.SHARPY_III);
		}
		if (bestSilkyRank == 1) {
			traits.add(Trait.SILKY_I);
		} else if (bestSilkyRank >= 2) {
			traits.add(Trait.SILKY_II);
		}
		if (bestFreezyRank == 1) {
			traits.add(Trait.FREEZY_I);
		} else if (bestFreezyRank >= 2) {
			traits.add(Trait.FREEZY_II);
		}
		if (bestMomentumRank == 1) {
			traits.add(Trait.MOMENTUM_I);
		} else if (bestMomentumRank >= 2) {
			traits.add(Trait.MOMENTUM_II);
		}
		if (bestBroadTouchRank == 1) {
			traits.add(Trait.BROAD_TOUCH_I);
		} else if (bestBroadTouchRank >= 2) {
			traits.add(Trait.BROAD_TOUCH_II);
		}
		if (bestFrenzyRank == 1) {
			traits.add(Trait.FRENZY_I);
		} else if (bestFrenzyRank == 2) {
			traits.add(Trait.FRENZY_II);
		} else if (bestFrenzyRank >= 3) {
			traits.add(Trait.FRENZY_III);
		}
		if (bestTitanRank == 1) {
			traits.add(Trait.TITAN_I);
		} else if (bestTitanRank >= 2) {
			traits.add(Trait.TITAN_II);
		}
		if (bestScholarRank == 1) {
			traits.add(Trait.SCHOLAR_I);
		} else if (bestScholarRank >= 2) {
			traits.add(Trait.SCHOLAR_II);
		}
		if (bestInsightRank == 1) {
			traits.add(Trait.INSIGHT_I);
		} else if (bestInsightRank == 2) {
			traits.add(Trait.INSIGHT_II);
		} else if (bestInsightRank >= 3) {
			traits.add(Trait.INSIGHT_III);
		}
		if (bestMoonlitRank == 1) {
			traits.add(Trait.MOONLIT_I);
		} else if (bestMoonlitRank >= 2) {
			traits.add(Trait.MOONLIT_II);
		}
		if (bestRangerRank == 1) {
			traits.add(Trait.RANGER_I);
		} else if (bestRangerRank >= 2) {
			traits.add(Trait.RANGER_II);
		}
		if (hasBountiful) {
			traits.add(Trait.BOUNTIFUL_I);
		}
		if (hasScavenger) {
			traits.add(Trait.SCAVENGER_I);
		}
		if (hasPurifying) {
			traits.add(Trait.PURIFYING_I);
		}
		if (hasSwiftstep) {
			traits.add(Trait.SWIFTSTEP_I);
		}
		if (hasBulwark) {
			traits.add(Trait.BULWARK_I);
		}
		if (bestThornsRank == 1) {
			traits.add(Trait.THORNS_I);
		} else if (bestThornsRank >= 2) {
			traits.add(Trait.THORNS_II);
		}
		if (bestFirewardRank == 1) {
			traits.add(Trait.FIREWARD_I);
		} else if (bestFirewardRank >= 2) {
			traits.add(Trait.FIREWARD_II);
		}
		if (bestGuardianRank == 1) {
			traits.add(Trait.GUARDIAN_I);
		} else if (bestGuardianRank >= 2) {
			traits.add(Trait.GUARDIAN_II);
		}
		if (hasRepulse) {
			traits.add(Trait.REPULSE_I);
		}
		if (bestAnglerRank == 1) {
			traits.add(Trait.ANGLER_I);
		} else if (bestAnglerRank >= 2) {
			traits.add(Trait.ANGLER_II);
		}
		if (hasReel) {
			traits.add(Trait.REEL_I);
		}
		if (bestMarksmanRank == 1) {
			traits.add(Trait.MARKSMAN_I);
		} else if (bestMarksmanRank >= 2) {
			traits.add(Trait.MARKSMAN_II);
		}
		if (hasQuickLoad) {
			traits.add(Trait.QUICK_LOAD_I);
		}
		if (hasReturning) {
			traits.add(Trait.RETURNING_I);
		}
		if (hasRiptide) {
			traits.add(Trait.RIPTIDE_I);
		}
		if (bestImpalerRank == 1) {
			traits.add(Trait.IMPALER_I);
		} else if (bestImpalerRank >= 2) {
			traits.add(Trait.IMPALER_II);
		}
		if (bestArcaneRank == 1) {
			traits.add(Trait.ARCANE_I);
		} else if (bestArcaneRank >= 2) {
			traits.add(Trait.ARCANE_II);
		}
		if (hasChanneling) {
			traits.add(Trait.CHANNELING_I);
		}
		if (bestAerodynamicRank == 1) {
			traits.add(Trait.AERODYNAMIC_I);
		} else if (bestAerodynamicRank >= 2) {
			traits.add(Trait.AERODYNAMIC_II);
		}
		if (hasFeatherfall) {
			traits.add(Trait.FEATHERFALL_I);
		}
		if (hasAdaptive) {
			traits.add(Trait.ADAPTIVE_I);
		}
		if (bestHemorrhageRank == 1) {
			traits.add(Trait.HEMORRHAGE_I);
		} else if (bestHemorrhageRank >= 2) {
			traits.add(Trait.HEMORRHAGE_II);
		}
		if (bestVolleyRank == 1) {
			traits.add(Trait.VOLLEY_I);
		} else if (bestVolleyRank >= 2) {
			traits.add(Trait.VOLLEY_II);
		}
		if (bestHarvestRank == 1) {
			traits.add(Trait.HARVEST_I);
		} else if (bestHarvestRank >= 2) {
			traits.add(Trait.HARVEST_II);
		}
		if (bestSteadfastRank == 1) {
			traits.add(Trait.STEADFAST_I);
		} else if (bestSteadfastRank >= 2) {
			traits.add(Trait.STEADFAST_II);
		}
		if (bestSearingRank == 1) {
			traits.add(Trait.SEARING_I);
		} else if (bestSearingRank >= 2) {
			traits.add(Trait.SEARING_II);
		}
		if (bestVitalityRank == 1) {
			traits.add(Trait.VITALITY_I);
		} else if (bestVitalityRank >= 2) {
			traits.add(Trait.VITALITY_II);
		}
		if (bestRimeRank == 1) {
			traits.add(Trait.RIME_I);
		} else if (bestRimeRank >= 2) {
			traits.add(Trait.RIME_II);
		}
		if (bestExecutionerRank == 1) {
			traits.add(Trait.EXECUTIONER_I);
		} else if (bestExecutionerRank >= 2) {
			traits.add(Trait.EXECUTIONER_II);
		}
		if (bestReavingRank == 1) {
			traits.add(Trait.REAVING_I);
		} else if (bestReavingRank >= 2) {
			traits.add(Trait.REAVING_II);
		}
		if (bestStalkerRank == 1) {
			traits.add(Trait.STALKER_I);
		} else if (bestStalkerRank >= 2) {
			traits.add(Trait.STALKER_II);
		}
		if (bestRiposteRank == 1) {
			traits.add(Trait.RIPOSTE_I);
		} else if (bestRiposteRank >= 2) {
			traits.add(Trait.RIPOSTE_II);
		}
		if (bestCrushingRank == 1) {
			traits.add(Trait.CRUSHING_I);
		} else if (bestCrushingRank >= 2) {
			traits.add(Trait.CRUSHING_II);
		}
		if (bestAncientRank == 1) {
			traits.add(Trait.ANCIENT_I);
		} else if (bestAncientRank >= 2) {
			traits.add(Trait.ANCIENT_II);
		}
		if (hasAutosmelt) {
			traits.add(Trait.AUTOSMELT);
		}
		if (hasAutosmeltII) {
			traits.add(Trait.AUTOSMELT_II);
		}
		if (hasSoulbound) {
			traits.add(Trait.SOULBOUND);
		}
		if (hasModifiable) {
			traits.add(Trait.MODIFIABLE_I);
		}
		if (hasMagnetic) {
			traits.add(Trait.MAGNETIC_I);
		}
		return traits;
	}

	public static boolean hasTrait(ItemStack stack, Trait trait) {
		return getTraits(stack).contains(trait);
	}

	public static Trait getNextBindableTrait(ItemStack toolStack, ItemStack ingredientStack) {
		if (!ToolProgressionHelper.isTogTool(toolStack)) {
			return null;
		}

		ToolProgressionHelper.ToolType toolType = ToolProgressionHelper.getToolType(toolStack);
		List<Trait> owned = getTraits(toolStack);
		if (ingredientStack.is(Items.REDSTONE_BLOCK)) {
			if (owned.contains(Trait.SPEEDY_III)) {
				return null;
			}

			int additionalCost = 1;
			if (!owned.contains(Trait.SPEEDY_I) && !owned.contains(Trait.SPEEDY_II)) {
				additionalCost = 1;
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.SPEEDY_I;
			}

			if (owned.contains(Trait.SPEEDY_I)) {
				additionalCost = Trait.SPEEDY_II.slotCost() - Trait.SPEEDY_I.slotCost();
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.SPEEDY_II;
			}

			if (owned.contains(Trait.SPEEDY_II)) {
				additionalCost = Trait.SPEEDY_III.slotCost() - Trait.SPEEDY_II.slotCost();
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.SPEEDY_III;
			}
		}

		if (ingredientStack.is(Items.FERMENTED_SPIDER_EYE) && isTraitAllowedForTool(Trait.POISON_I, toolType)) {
			if (owned.contains(Trait.POISON_II)) {
				return null;
			}

			int additionalCost = 1;
			if (!owned.contains(Trait.POISON_I)) {
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.POISON_I;
			}

			additionalCost = Trait.POISON_II.slotCost() - Trait.POISON_I.slotCost();
			if (getEmptyTraitSlots(toolStack) < additionalCost) {
				return null;
			}
			return Trait.POISON_II;
		}

		if (ingredientStack.is(Items.BREAD)) {
			if (owned.contains(Trait.SUSTAINING_III)) {
				return null;
			}

			int additionalCost = 1;
			if (!owned.contains(Trait.SUSTAINING_I) && !owned.contains(Trait.SUSTAINING_II)) {
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.SUSTAINING_I;
			}

			if (owned.contains(Trait.SUSTAINING_I)) {
				additionalCost = Trait.SUSTAINING_II.slotCost() - Trait.SUSTAINING_I.slotCost();
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.SUSTAINING_II;
			}

			if (owned.contains(Trait.SUSTAINING_II)) {
				additionalCost = Trait.SUSTAINING_III.slotCost() - Trait.SUSTAINING_II.slotCost();
				if (getEmptyTraitSlots(toolStack) < additionalCost) {
					return null;
				}
				return Trait.SUSTAINING_III;
			}
		}

		if (ingredientStack.is(Items.MAGMA_BLOCK)) {
			if (owned.contains(Trait.AUTOSMELT_II)) {
				return null;
			}
			if (!owned.contains(Trait.AUTOSMELT)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.AUTOSMELT.slotCost()) {
					return Trait.AUTOSMELT;
				}
				return null;
			}
			if (owned.contains(Trait.AUTOSMELT)) {
				int cost = Trait.AUTOSMELT_II.slotCost() - Trait.AUTOSMELT.slotCost();
				if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
					return Trait.AUTOSMELT_II;
				}
			}
			return null;
		}

		if (ingredientStack.is(Items.NETHER_STAR) && !owned.contains(Trait.SOULBOUND) && getEmptyTraitSlots(toolStack) >= Trait.SOULBOUND.slotCost()) {
			return Trait.SOULBOUND;
		}

		if (ingredientStack.is(Items.IRON_INGOT) && isTraitAllowedForTool(Trait.SHARPY_I, toolType)) {
			if (owned.contains(Trait.SHARPY_III)) {
				return null;
			}

			if (!owned.contains(Trait.SHARPY_I) && !owned.contains(Trait.SHARPY_II)) {
				if (getEmptyTraitSlots(toolStack) < 1) {
					return null;
				}
				return Trait.SHARPY_I;
			}

			if (owned.contains(Trait.SHARPY_I)) {
				if (getEmptyTraitSlots(toolStack) < Trait.SHARPY_II.slotCost() - Trait.SHARPY_I.slotCost()) {
					return null;
				}
				return Trait.SHARPY_II;
			}

			if (owned.contains(Trait.SHARPY_II)) {
				if (getEmptyTraitSlots(toolStack) < Trait.SHARPY_III.slotCost() - Trait.SHARPY_II.slotCost()) {
					return null;
				}
				return Trait.SHARPY_III;
			}
		}

		if (ingredientStack.is(Items.DIAMOND) && !owned.contains(Trait.MODIFIABLE_I)) {
			return Trait.MODIFIABLE_I;
		}

		if (ingredientStack.is(Items.COPPER_INGOT) && !owned.contains(Trait.MAGNETIC_I) && getEmptyTraitSlots(toolStack) >= Trait.MAGNETIC_I.slotCost()) {
			return Trait.MAGNETIC_I;
		}

		if (ingredientStack.is(Items.STRING) && isTraitAllowedForTool(Trait.SILKY_I, toolType)) {
			if (owned.contains(Trait.SILKY_II)) {
				return null;
			}
			if (!owned.contains(Trait.SILKY_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.SILKY_I.slotCost()) {
					return Trait.SILKY_I;
				}
				return null;
			}
			if (owned.contains(Trait.SILKY_I)) {
				int cost = Trait.SILKY_II.slotCost() - Trait.SILKY_I.slotCost();
				if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
					return Trait.SILKY_II;
				}
			}
			return null;
		}

		if (ingredientStack.is(Items.ICE) && isTraitAllowedForTool(Trait.FREEZY_I, toolType)) {
			if (owned.contains(Trait.FREEZY_II)) {
				return null;
			}
			if (!owned.contains(Trait.FREEZY_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.FREEZY_I.slotCost()) {
					return Trait.FREEZY_I;
				}
				return null;
			}
			if (owned.contains(Trait.FREEZY_I)) {
				int cost = Trait.FREEZY_II.slotCost() - Trait.FREEZY_I.slotCost();
				if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
					return Trait.FREEZY_II;
				}
			}
			return null;
		}

		if (ingredientStack.is(Items.SUGAR) && isTraitAllowedForTool(Trait.MOMENTUM_I, toolType)) {
			if (owned.contains(Trait.MOMENTUM_II)) {
				return null;
			}
			if (!owned.contains(Trait.MOMENTUM_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.MOMENTUM_I.slotCost()) {
					return Trait.MOMENTUM_I;
				}
				return null;
			}
			if (owned.contains(Trait.MOMENTUM_I)) {
				int cost = Trait.MOMENTUM_II.slotCost() - Trait.MOMENTUM_I.slotCost();
				if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
					return Trait.MOMENTUM_II;
				}
			}
			return null;
		}

		if (ingredientStack.is(Items.SLIME_BALL) && isTraitAllowedForTool(Trait.BROAD_TOUCH_I, toolType)) {
			if (owned.contains(Trait.BROAD_TOUCH_II)) {
				return null;
			}
			if (!owned.contains(Trait.BROAD_TOUCH_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.BROAD_TOUCH_I.slotCost()) {
					return Trait.BROAD_TOUCH_I;
				}
				return null;
			}
			if (owned.contains(Trait.BROAD_TOUCH_I)) {
				int cost = Trait.BROAD_TOUCH_II.slotCost() - Trait.BROAD_TOUCH_I.slotCost();
				if (getEmptyTraitSlots(toolStack) >= cost) {
					return Trait.BROAD_TOUCH_II;
				}
			}
			return null;
		}

		if (ingredientStack.is(Items.BLAZE_POWDER) && isTraitAllowedForTool(Trait.FRENZY_I, toolType)) {
			if (owned.contains(Trait.FRENZY_III)) {
				return null;
			}

			if (!owned.contains(Trait.FRENZY_I) && !owned.contains(Trait.FRENZY_II)) {
				if (getEmptyTraitSlots(toolStack) < Trait.FRENZY_I.slotCost()) {
					return null;
				}
				return Trait.FRENZY_I;
			}

			if (owned.contains(Trait.FRENZY_I)) {
				int cost = Trait.FRENZY_II.slotCost() - Trait.FRENZY_I.slotCost();
				if (getEmptyTraitSlots(toolStack) < cost) {
					return null;
				}
				return Trait.FRENZY_II;
			}

			if (owned.contains(Trait.FRENZY_II)) {
				int cost = Trait.FRENZY_III.slotCost() - Trait.FRENZY_II.slotCost();
				if (getEmptyTraitSlots(toolStack) < cost) {
					return null;
				}
				return Trait.FRENZY_III;
			}
		}

		if (ingredientStack.is(Items.ANVIL) && isTraitAllowedForTool(Trait.TITAN_I, toolType)) {
			if (owned.contains(Trait.TITAN_II)) {
				return null;
			}
			if (!owned.contains(Trait.TITAN_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.TITAN_I.slotCost()) {
					return Trait.TITAN_I;
				}
				return null;
			}
			int cost = Trait.TITAN_II.slotCost() - Trait.TITAN_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.TITAN_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.LAPIS_LAZULI)) {
			if (owned.contains(Trait.SCHOLAR_II)) {
				return null;
			}
			if (!owned.contains(Trait.SCHOLAR_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.SCHOLAR_I.slotCost()) {
					return Trait.SCHOLAR_I;
				}
				return null;
			}
			int cost = Trait.SCHOLAR_II.slotCost() - Trait.SCHOLAR_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.SCHOLAR_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.BOOK)
			&& !owned.contains(Trait.INSIGHT_I) && !owned.contains(Trait.INSIGHT_II) && !owned.contains(Trait.INSIGHT_III)
			&& getEmptyTraitSlots(toolStack) >= Trait.INSIGHT_I.slotCost()) {
			return Trait.INSIGHT_I;
		}
		if (ingredientStack.is(Items.BOOKSHELF)) {
			if (owned.contains(Trait.INSIGHT_II) || owned.contains(Trait.INSIGHT_III)) {
				return null;
			}
			if (!owned.contains(Trait.INSIGHT_I)) {
				return null;
			}
			int cost = Trait.INSIGHT_II.slotCost() - Trait.INSIGHT_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.INSIGHT_II;
			}
			return null;
		}
		if (ingredientStack.is(Items.ENCHANTING_TABLE)) {
			if (owned.contains(Trait.INSIGHT_III)) {
				return null;
			}
			if (!owned.contains(Trait.INSIGHT_II)) {
				return null;
			}
			int cost = Trait.INSIGHT_III.slotCost() - Trait.INSIGHT_II.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.INSIGHT_III;
			}
			return null;
		}

		if (ingredientStack.is(Items.PHANTOM_MEMBRANE) && isTraitAllowedForTool(Trait.MOONLIT_I, toolType)) {
			if (owned.contains(Trait.MOONLIT_II)) {
				return null;
			}
			if (!owned.contains(Trait.MOONLIT_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.MOONLIT_I.slotCost()) {
					return Trait.MOONLIT_I;
				}
				return null;
			}
			int cost = Trait.MOONLIT_II.slotCost() - Trait.MOONLIT_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.MOONLIT_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.FLINT) && isTraitAllowedForTool(Trait.RANGER_I, toolType)) {
			if (owned.contains(Trait.RANGER_II)) {
				return null;
			}
			if (!owned.contains(Trait.RANGER_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.RANGER_I.slotCost()) {
					return Trait.RANGER_I;
				}
				return null;
			}
			int cost = Trait.RANGER_II.slotCost() - Trait.RANGER_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.RANGER_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.EMERALD) && !owned.contains(Trait.BOUNTIFUL_I)) {
			if (getEmptyTraitSlots(toolStack) >= Trait.BOUNTIFUL_I.slotCost()) {
				return Trait.BOUNTIFUL_I;
			}
			return null;
		}

		if (ingredientStack.is(Items.HOPPER) && !owned.contains(Trait.SCAVENGER_I)) {
			if (getEmptyTraitSlots(toolStack) >= Trait.SCAVENGER_I.slotCost()) {
				return Trait.SCAVENGER_I;
			}
			return null;
		}

		if (ingredientStack.is(Items.GLOWSTONE_DUST) && !owned.contains(Trait.PURIFYING_I)) {
			if (getEmptyTraitSlots(toolStack) >= Trait.PURIFYING_I.slotCost()) {
				return Trait.PURIFYING_I;
			}
			return null;
		}

		if (ingredientStack.is(Items.FEATHER) && !owned.contains(Trait.SWIFTSTEP_I)) {
			if (getEmptyTraitSlots(toolStack) >= Trait.SWIFTSTEP_I.slotCost()) {
				return Trait.SWIFTSTEP_I;
			}
			return null;
		}

		if (ingredientStack.is(Items.BRICK) && !owned.contains(Trait.BULWARK_I)) {
			if (getEmptyTraitSlots(toolStack) >= Trait.BULWARK_I.slotCost()) {
				return Trait.BULWARK_I;
			}
			return null;
		}

		if (ingredientStack.is(Items.CACTUS) && isTraitAllowedForTool(Trait.THORNS_I, toolType)) {
			if (owned.contains(Trait.THORNS_II)) {
				return null;
			}
			if (!owned.contains(Trait.THORNS_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.THORNS_I.slotCost()) {
					return Trait.THORNS_I;
				}
				return null;
			}
			int cost = Trait.THORNS_II.slotCost() - Trait.THORNS_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.THORNS_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.MAGMA_CREAM) && isTraitAllowedForTool(Trait.FIREWARD_I, toolType)) {
			if (owned.contains(Trait.FIREWARD_II)) {
				return null;
			}
			if (!owned.contains(Trait.FIREWARD_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.FIREWARD_I.slotCost()) {
					return Trait.FIREWARD_I;
				}
				return null;
			}
			int cost = Trait.FIREWARD_II.slotCost() - Trait.FIREWARD_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.FIREWARD_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.IRON_BLOCK) && isTraitAllowedForTool(Trait.GUARDIAN_I, toolType)) {
			if (owned.contains(Trait.GUARDIAN_II)) {
				return null;
			}
			if (!owned.contains(Trait.GUARDIAN_I)) {
				if (getEmptyTraitSlots(toolStack) >= Trait.GUARDIAN_I.slotCost()) {
					return Trait.GUARDIAN_I;
				}
				return null;
			}
			int cost = Trait.GUARDIAN_II.slotCost() - Trait.GUARDIAN_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.GUARDIAN_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.OBSIDIAN) && isTraitAllowedForTool(Trait.GUARDIAN_I, toolType)
			&& owned.contains(Trait.GUARDIAN_I) && !owned.contains(Trait.GUARDIAN_II)) {
			int cost = Trait.GUARDIAN_II.slotCost() - Trait.GUARDIAN_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.GUARDIAN_II;
			}
		}

		if (ingredientStack.is(Items.PISTON) && isTraitAllowedForTool(Trait.REPULSE_I, toolType)
			&& !owned.contains(Trait.REPULSE_I) && getEmptyTraitSlots(toolStack) >= Trait.REPULSE_I.slotCost()) {
			return Trait.REPULSE_I;
		}

		if (ingredientStack.is(Items.BLAZE_ROD) && isTraitAllowedForTool(Trait.FIREWARD_I, toolType)
			&& owned.contains(Trait.FIREWARD_I) && !owned.contains(Trait.FIREWARD_II)) {
			int cost = Trait.FIREWARD_II.slotCost() - Trait.FIREWARD_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.FIREWARD_II;
			}
		}

		if (ingredientStack.is(Items.NAUTILUS_SHELL) && isTraitAllowedForTool(Trait.ANGLER_I, toolType)
			&& !owned.contains(Trait.ANGLER_I) && !owned.contains(Trait.ANGLER_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.ANGLER_I.slotCost()) {
			return Trait.ANGLER_I;
		}

		if (ingredientStack.is(Items.HEART_OF_THE_SEA) && isTraitAllowedForTool(Trait.ANGLER_I, toolType)
			&& owned.contains(Trait.ANGLER_I) && !owned.contains(Trait.ANGLER_II)) {
			int cost = Trait.ANGLER_II.slotCost() - Trait.ANGLER_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.ANGLER_II;
			}
		}

		if (ingredientStack.is(Items.COD) && isTraitAllowedForTool(Trait.REEL_I, toolType)
			&& !owned.contains(Trait.REEL_I) && getEmptyTraitSlots(toolStack) >= Trait.REEL_I.slotCost()) {
			return Trait.REEL_I;
		}

		if (ingredientStack.is(Items.SPECTRAL_ARROW) && isTraitAllowedForTool(Trait.MARKSMAN_I, toolType)) {
			if (owned.contains(Trait.MARKSMAN_II)) {
				return null;
			}
			if (!owned.contains(Trait.MARKSMAN_I) && getEmptyTraitSlots(toolStack) >= Trait.MARKSMAN_I.slotCost()) {
				return Trait.MARKSMAN_I;
			}
			int cost = Trait.MARKSMAN_II.slotCost() - Trait.MARKSMAN_I.slotCost();
			if (owned.contains(Trait.MARKSMAN_I) && getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.MARKSMAN_II;
			}
		}

		if (ingredientStack.is(Items.ARROW) && isTraitAllowedForTool(Trait.MARKSMAN_I, toolType)
			&& owned.contains(Trait.MARKSMAN_I) && !owned.contains(Trait.MARKSMAN_II)) {
			int cost = Trait.MARKSMAN_II.slotCost() - Trait.MARKSMAN_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.MARKSMAN_II;
			}
		}

		if (ingredientStack.is(Items.FIREWORK_ROCKET) && isTraitAllowedForTool(Trait.QUICK_LOAD_I, toolType)
			&& !owned.contains(Trait.QUICK_LOAD_I) && getEmptyTraitSlots(toolStack) >= Trait.QUICK_LOAD_I.slotCost()) {
			return Trait.QUICK_LOAD_I;
		}

		if (ingredientStack.is(Items.ENDER_PEARL) && isTraitAllowedForTool(Trait.RETURNING_I, toolType)
			&& !owned.contains(Trait.RETURNING_I) && getEmptyTraitSlots(toolStack) >= Trait.RETURNING_I.slotCost()) {
			return Trait.RETURNING_I;
		}

		if (ingredientStack.is(Items.PRISMARINE_SHARD) && isTraitAllowedForTool(Trait.RIPTIDE_I, toolType)
			&& !owned.contains(Trait.RIPTIDE_I) && getEmptyTraitSlots(toolStack) >= Trait.RIPTIDE_I.slotCost()) {
			return Trait.RIPTIDE_I;
		}

		if (ingredientStack.is(Items.PRISMARINE_CRYSTALS) && isTraitAllowedForTool(Trait.IMPALER_I, toolType)) {
			if (owned.contains(Trait.IMPALER_II)) {
				return null;
			}
			if (!owned.contains(Trait.IMPALER_I) && getEmptyTraitSlots(toolStack) >= Trait.IMPALER_I.slotCost()) {
				return Trait.IMPALER_I;
			}
			int cost = Trait.IMPALER_II.slotCost() - Trait.IMPALER_I.slotCost();
			if (owned.contains(Trait.IMPALER_I) && getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.IMPALER_II;
			}
		}

		if (ingredientStack.is(Items.NETHERITE_INGOT) && isTraitAllowedForTool(Trait.IMPALER_I, toolType)
			&& owned.contains(Trait.IMPALER_I) && !owned.contains(Trait.IMPALER_II)) {
			int cost = Trait.IMPALER_II.slotCost() - Trait.IMPALER_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.IMPALER_II;
			}
		}

		if (ingredientStack.is(Items.AMETHYST_SHARD) && isTraitAllowedForTool(Trait.ARCANE_I, toolType)) {
			if (owned.contains(Trait.ARCANE_II)) {
				return null;
			}
			if (!owned.contains(Trait.ARCANE_I) && getEmptyTraitSlots(toolStack) >= Trait.ARCANE_I.slotCost()) {
				return Trait.ARCANE_I;
			}
			int cost = Trait.ARCANE_II.slotCost() - Trait.ARCANE_I.slotCost();
			if (owned.contains(Trait.ARCANE_I) && getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.ARCANE_II;
			}
		}

		if (ingredientStack.is(Items.DRAGON_BREATH) && isTraitAllowedForTool(Trait.ARCANE_I, toolType)
			&& owned.contains(Trait.ARCANE_I) && !owned.contains(Trait.ARCANE_II)) {
			int cost = Trait.ARCANE_II.slotCost() - Trait.ARCANE_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.ARCANE_II;
			}
		}

		if (ingredientStack.is(Items.LIGHTNING_ROD) && isTraitAllowedForTool(Trait.CHANNELING_I, toolType)
			&& !owned.contains(Trait.CHANNELING_I) && getEmptyTraitSlots(toolStack) >= Trait.CHANNELING_I.slotCost()) {
			return Trait.CHANNELING_I;
		}

		if (ingredientStack.is(Items.GHAST_TEAR) && isTraitAllowedForTool(Trait.AERODYNAMIC_I, toolType)) {
			if (owned.contains(Trait.AERODYNAMIC_II)) {
				return null;
			}
			if (!owned.contains(Trait.AERODYNAMIC_I) && getEmptyTraitSlots(toolStack) >= Trait.AERODYNAMIC_I.slotCost()) {
				return Trait.AERODYNAMIC_I;
			}
			int cost = Trait.AERODYNAMIC_II.slotCost() - Trait.AERODYNAMIC_I.slotCost();
			if (owned.contains(Trait.AERODYNAMIC_I) && getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.AERODYNAMIC_II;
			}
		}

		if (ingredientStack.is(Items.ELYTRA) && isTraitAllowedForTool(Trait.AERODYNAMIC_I, toolType)
			&& owned.contains(Trait.AERODYNAMIC_I) && !owned.contains(Trait.AERODYNAMIC_II)) {
			int cost = Trait.AERODYNAMIC_II.slotCost() - Trait.AERODYNAMIC_I.slotCost();
			if (getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.AERODYNAMIC_II;
			}
		}

		if (ingredientStack.is(Items.TURTLE_SCUTE) && isTraitAllowedForTool(Trait.FEATHERFALL_I, toolType)
			&& !owned.contains(Trait.FEATHERFALL_I) && getEmptyTraitSlots(toolStack) >= Trait.FEATHERFALL_I.slotCost()) {
			return Trait.FEATHERFALL_I;
		}

		if (ingredientStack.is(Items.COMPASS) && isTraitAllowedForTool(Trait.ADAPTIVE_I, toolType)
			&& !owned.contains(Trait.ADAPTIVE_I) && getEmptyTraitSlots(toolStack) >= Trait.ADAPTIVE_I.slotCost()) {
			return Trait.ADAPTIVE_I;
		}

		if (ingredientStack.is(Items.COBWEB) && isTraitAllowedForTool(Trait.HEMORRHAGE_I, toolType)) {
			if (owned.contains(Trait.HEMORRHAGE_II)) {
				return null;
			}
			if (!owned.contains(Trait.HEMORRHAGE_I) && getEmptyTraitSlots(toolStack) >= Trait.HEMORRHAGE_I.slotCost()) {
				return Trait.HEMORRHAGE_I;
			}
			if (owned.contains(Trait.HEMORRHAGE_I) && getEmptyTraitSlots(toolStack) >= Trait.HEMORRHAGE_II.slotCost() - Trait.HEMORRHAGE_I.slotCost()) {
				return Trait.HEMORRHAGE_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.PAPER) && isTraitAllowedForTool(Trait.VOLLEY_I, toolType)) {
			if (owned.contains(Trait.VOLLEY_II)) {
				return null;
			}
			if (!owned.contains(Trait.VOLLEY_I) && getEmptyTraitSlots(toolStack) >= Trait.VOLLEY_I.slotCost()) {
				return Trait.VOLLEY_I;
			}
			if (owned.contains(Trait.VOLLEY_I) && getEmptyTraitSlots(toolStack) >= Trait.VOLLEY_II.slotCost() - Trait.VOLLEY_I.slotCost()) {
				return Trait.VOLLEY_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.WHEAT_SEEDS) && isTraitAllowedForTool(Trait.HARVEST_I, toolType)
			&& !owned.contains(Trait.HARVEST_I) && !owned.contains(Trait.HARVEST_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.HARVEST_I.slotCost()) {
			return Trait.HARVEST_I;
		}
		if (ingredientStack.is(Items.WHEAT) && isTraitAllowedForTool(Trait.HARVEST_II, toolType)) {
			if (owned.contains(Trait.HARVEST_II)) {
				return null;
			}
			if (!owned.contains(Trait.HARVEST_I) && getEmptyTraitSlots(toolStack) >= Trait.HARVEST_II.slotCost()) {
				return null;
			}
			if (owned.contains(Trait.HARVEST_I) && getEmptyTraitSlots(toolStack) >= Trait.HARVEST_II.slotCost() - Trait.HARVEST_I.slotCost()) {
				return Trait.HARVEST_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.IRON_CHAIN) && isTraitAllowedForTool(Trait.STEADFAST_I, toolType)
			&& !owned.contains(Trait.STEADFAST_I) && !owned.contains(Trait.STEADFAST_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.STEADFAST_I.slotCost()) {
			return Trait.STEADFAST_I;
		}
		if (ingredientStack.is(Items.IRON_NUGGET) && isTraitAllowedForTool(Trait.STEADFAST_II, toolType)) {
			if (owned.contains(Trait.STEADFAST_II)) {
				return null;
			}
			if (!owned.contains(Trait.STEADFAST_I) && getEmptyTraitSlots(toolStack) >= Trait.STEADFAST_II.slotCost()) {
				return null;
			}
			if (owned.contains(Trait.STEADFAST_I) && getEmptyTraitSlots(toolStack) >= Trait.STEADFAST_II.slotCost() - Trait.STEADFAST_I.slotCost()) {
				return Trait.STEADFAST_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.FIRE_CHARGE) && isTraitAllowedForTool(Trait.SEARING_I, toolType)) {
			if (owned.contains(Trait.SEARING_II)) {
				return null;
			}
			if (!owned.contains(Trait.SEARING_I) && getEmptyTraitSlots(toolStack) >= Trait.SEARING_I.slotCost()) {
				return Trait.SEARING_I;
			}
			if (owned.contains(Trait.SEARING_I) && getEmptyTraitSlots(toolStack) >= Trait.SEARING_II.slotCost() - Trait.SEARING_I.slotCost()) {
				return Trait.SEARING_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.GOLDEN_APPLE) && isTraitAllowedForTool(Trait.VITALITY_I, toolType)
			&& !owned.contains(Trait.VITALITY_I) && !owned.contains(Trait.VITALITY_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.VITALITY_I.slotCost()) {
			return Trait.VITALITY_I;
		}
		if (ingredientStack.is(Items.ENCHANTED_GOLDEN_APPLE) && isTraitAllowedForTool(Trait.VITALITY_II, toolType)) {
			if (owned.contains(Trait.VITALITY_II)) {
				return null;
			}
			if (!owned.contains(Trait.VITALITY_I) && getEmptyTraitSlots(toolStack) >= Trait.VITALITY_II.slotCost()) {
				return null;
			}
			if (owned.contains(Trait.VITALITY_I) && getEmptyTraitSlots(toolStack) >= Trait.VITALITY_II.slotCost() - Trait.VITALITY_I.slotCost()) {
				return Trait.VITALITY_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.PACKED_ICE) && isTraitAllowedForTool(Trait.RIME_I, toolType)
			&& !owned.contains(Trait.RIME_I) && !owned.contains(Trait.RIME_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.RIME_I.slotCost()) {
			return Trait.RIME_I;
		}
		if (ingredientStack.is(Items.BLUE_ICE) && isTraitAllowedForTool(Trait.RIME_II, toolType)
			&& owned.contains(Trait.RIME_I) && !owned.contains(Trait.RIME_II)) {
			int cost = Trait.RIME_II.slotCost() - Trait.RIME_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.RIME_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.BONE) && isTraitAllowedForTool(Trait.EXECUTIONER_I, toolType)
			&& !owned.contains(Trait.EXECUTIONER_I) && !owned.contains(Trait.EXECUTIONER_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.EXECUTIONER_I.slotCost()) {
			return Trait.EXECUTIONER_I;
		}
		if (ingredientStack.is(Items.WITHER_SKELETON_SKULL) && isTraitAllowedForTool(Trait.EXECUTIONER_II, toolType)
			&& owned.contains(Trait.EXECUTIONER_I) && !owned.contains(Trait.EXECUTIONER_II)) {
			int cost = Trait.EXECUTIONER_II.slotCost() - Trait.EXECUTIONER_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.EXECUTIONER_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.EXPERIENCE_BOTTLE) && isTraitAllowedForTool(Trait.REAVING_I, toolType)
			&& !owned.contains(Trait.REAVING_I) && !owned.contains(Trait.REAVING_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.REAVING_I.slotCost()) {
			return Trait.REAVING_I;
		}
		if (ingredientStack.is(Items.ECHO_SHARD) && isTraitAllowedForTool(Trait.REAVING_II, toolType)
			&& owned.contains(Trait.REAVING_I) && !owned.contains(Trait.REAVING_II)) {
			int cost = Trait.REAVING_II.slotCost() - Trait.REAVING_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.REAVING_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.RABBIT_FOOT) && isTraitAllowedForTool(Trait.STALKER_I, toolType)
			&& !owned.contains(Trait.STALKER_I) && !owned.contains(Trait.STALKER_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.STALKER_I.slotCost()) {
			return Trait.STALKER_I;
		}
		if (ingredientStack.is(Items.SPIDER_EYE) && isTraitAllowedForTool(Trait.STALKER_II, toolType)
			&& owned.contains(Trait.STALKER_I) && !owned.contains(Trait.STALKER_II)) {
			int cost = Trait.STALKER_II.slotCost() - Trait.STALKER_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.STALKER_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.GOLD_NUGGET) && isTraitAllowedForTool(Trait.RIPOSTE_I, toolType)
			&& !owned.contains(Trait.RIPOSTE_I) && !owned.contains(Trait.RIPOSTE_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.RIPOSTE_I.slotCost()) {
			return Trait.RIPOSTE_I;
		}
		if (ingredientStack.is(Items.GOLD_INGOT) && isTraitAllowedForTool(Trait.RIPOSTE_II, toolType)
			&& owned.contains(Trait.RIPOSTE_I) && !owned.contains(Trait.RIPOSTE_II)) {
			int cost = Trait.RIPOSTE_II.slotCost() - Trait.RIPOSTE_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.RIPOSTE_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.COBBLESTONE) && isTraitAllowedForTool(Trait.CRUSHING_I, toolType)
			&& !owned.contains(Trait.CRUSHING_I) && !owned.contains(Trait.CRUSHING_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.CRUSHING_I.slotCost()) {
			return Trait.CRUSHING_I;
		}
		if (ingredientStack.is(Items.DEEPSLATE) && isTraitAllowedForTool(Trait.CRUSHING_II, toolType)
			&& owned.contains(Trait.CRUSHING_I) && !owned.contains(Trait.CRUSHING_II)) {
			int cost = Trait.CRUSHING_II.slotCost() - Trait.CRUSHING_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.CRUSHING_II;
			}
			return null;
		}

		if (ingredientStack.is(Items.LAPIS_LAZULI) && isTraitAllowedForTool(Trait.ANCIENT_I, toolType)
			&& !owned.contains(Trait.ANCIENT_I) && !owned.contains(Trait.ANCIENT_II)
			&& getEmptyTraitSlots(toolStack) >= Trait.ANCIENT_I.slotCost()) {
			return Trait.ANCIENT_I;
		}
		if (ingredientStack.is(Items.LAPIS_BLOCK) && isTraitAllowedForTool(Trait.ANCIENT_II, toolType)
			&& owned.contains(Trait.ANCIENT_I) && !owned.contains(Trait.ANCIENT_II)) {
			int cost = Trait.ANCIENT_II.slotCost() - Trait.ANCIENT_I.slotCost();
			if (cost <= 0 || getEmptyTraitSlots(toolStack) >= cost) {
				return Trait.ANCIENT_II;
			}
			return null;
		}

		return null;
	}

	public static boolean isValidTraitIngredient(ItemStack ingredientStack) {
		return ingredientStack.is(Items.REDSTONE_BLOCK)
			|| ingredientStack.is(Items.FERMENTED_SPIDER_EYE)
			|| ingredientStack.is(Items.BREAD)
			|| ingredientStack.is(Items.MAGMA_BLOCK)
			|| ingredientStack.is(Items.NETHER_STAR)
			|| ingredientStack.is(Items.IRON_INGOT)
			|| ingredientStack.is(Items.DIAMOND)
			|| ingredientStack.is(Items.COPPER_INGOT)
			|| ingredientStack.is(Items.STRING)
			|| ingredientStack.is(Items.ICE)
			|| ingredientStack.is(Items.SUGAR)
			|| ingredientStack.is(Items.SLIME_BALL)
			|| ingredientStack.is(Items.BLAZE_POWDER)
			|| ingredientStack.is(Items.ANVIL)
			|| ingredientStack.is(Items.LAPIS_LAZULI)
			|| ingredientStack.is(Items.BOOK)
			|| ingredientStack.is(Items.BOOKSHELF)
			|| ingredientStack.is(Items.ENCHANTING_TABLE)
			|| ingredientStack.is(Items.PHANTOM_MEMBRANE)
			|| ingredientStack.is(Items.FLINT)
			|| ingredientStack.is(Items.EMERALD)
			|| ingredientStack.is(Items.HOPPER)
			|| ingredientStack.is(Items.GLOWSTONE_DUST)
			|| ingredientStack.is(Items.FEATHER)
			|| ingredientStack.is(Items.BRICK)
			|| ingredientStack.is(Items.CACTUS)
			|| ingredientStack.is(Items.MAGMA_CREAM)
			|| ingredientStack.is(Items.BLAZE_ROD)
			|| ingredientStack.is(Items.IRON_BLOCK)
			|| ingredientStack.is(Items.OBSIDIAN)
			|| ingredientStack.is(Items.PISTON)
			|| ingredientStack.is(Items.NAUTILUS_SHELL)
			|| ingredientStack.is(Items.HEART_OF_THE_SEA)
			|| ingredientStack.is(Items.COD)
			|| ingredientStack.is(Items.SPECTRAL_ARROW)
			|| ingredientStack.is(Items.FIREWORK_ROCKET)
			|| ingredientStack.is(Items.ENDER_PEARL)
			|| ingredientStack.is(Items.PRISMARINE_SHARD)
			|| ingredientStack.is(Items.PRISMARINE_CRYSTALS)
			|| ingredientStack.is(Items.AMETHYST_SHARD)
			|| ingredientStack.is(Items.DRAGON_BREATH)
			|| ingredientStack.is(Items.LIGHTNING_ROD)
			|| ingredientStack.is(Items.GHAST_TEAR)
			|| ingredientStack.is(Items.ELYTRA)
			|| ingredientStack.is(Items.TURTLE_SCUTE)
			|| ingredientStack.is(Items.COMPASS)
			|| ingredientStack.is(Items.COBWEB)
			|| ingredientStack.is(Items.PAPER)
			|| ingredientStack.is(Items.WHEAT_SEEDS)
			|| ingredientStack.is(Items.WHEAT)
			|| ingredientStack.is(Items.IRON_CHAIN)
			|| ingredientStack.is(Items.IRON_NUGGET)
			|| ingredientStack.is(Items.FIRE_CHARGE)
			|| ingredientStack.is(Items.GOLDEN_APPLE)
			|| ingredientStack.is(Items.ENCHANTED_GOLDEN_APPLE)
			|| ingredientStack.is(Items.PACKED_ICE)
			|| ingredientStack.is(Items.BLUE_ICE)
			|| ingredientStack.is(Items.BONE)
			|| ingredientStack.is(Items.WITHER_SKELETON_SKULL)
			|| ingredientStack.is(Items.EXPERIENCE_BOTTLE)
			|| ingredientStack.is(Items.ECHO_SHARD)
			|| ingredientStack.is(Items.RABBIT_FOOT)
			|| ingredientStack.is(Items.SPIDER_EYE)
			|| ingredientStack.is(Items.GOLD_NUGGET)
			|| ingredientStack.is(Items.GOLD_INGOT)
			|| ingredientStack.is(Items.COBBLESTONE)
			|| ingredientStack.is(Items.DEEPSLATE)
			|| ingredientStack.is(Items.LAPIS_LAZULI)
			|| ingredientStack.is(Items.LAPIS_BLOCK)
			|| ingredientStack.is(ToolsOfTheGodsOrbItems.TRAIT_REMOVER.get());
	}

	public static boolean isTraitPurgeIngredient(ItemStack stack) {
		return stack.is(ToolsOfTheGodsOrbItems.TRAIT_REMOVER.get());
	}

	public static boolean canPurgeTrait(ItemStack toolStack) {
		return !getTraits(toolStack).isEmpty();
	}

	public static Trait getTraitToPurge(ItemStack toolStack) {
		List<Trait> traits = getTraits(toolStack);
		if (traits.isEmpty()) {
			return null;
		}
		return traits.get(traits.size() - 1);
	}

	public static boolean purgeTrait(ItemStack toolStack, ItemStack ingredientStack) {
		if (!isTraitPurgeIngredient(ingredientStack) || !canPurgeTrait(toolStack)) {
			return false;
		}

		List<Trait> owned = new ArrayList<>(getTraits(toolStack));
		Trait removed = owned.remove(owned.size() - 1);
		writeTraits(toolStack, owned);
		clearToggleStateForRemovedTrait(toolStack, removed);
		ingredientStack.shrink(1);
		return true;
	}

	private static void clearToggleStateForRemovedTrait(ItemStack stack, Trait trait) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			if (trait == Trait.SILKY_II) {
				tag.remove("togSilkActive");
			} else if (trait == Trait.AUTOSMELT_II) {
				tag.remove("togAutosmeltActive");
			} else if (trait == Trait.BROAD_TOUCH_I || trait == Trait.BROAD_TOUCH_II) {
				tag.remove("togBroadMode");
			}
		});
	}

	private static final String NBT_TOGGLE_CYCLE = "togToggleCycle";

	public static int advanceToggleCycle(ItemStack stack, int optionCount) {
		if (optionCount <= 0) {
			return 0;
		}
		int index = getToggleCycle(stack) % optionCount;
		int next = (index + 1) % optionCount;
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(NBT_TOGGLE_CYCLE, next));
		return index;
	}

	private static int getToggleCycle(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBT_TOGGLE_CYCLE, 0);
	}

	private static boolean sameFamily(Trait a, Trait b) {
		if (a == null || b == null) return false;
		return (a.name().startsWith("SPEEDY") && b.name().startsWith("SPEEDY"))
			|| (a.name().startsWith("POISON") && b.name().startsWith("POISON"))
			|| (a.name().startsWith("SUSTAINING") && b.name().startsWith("SUSTAINING"))
			|| (a.name().startsWith("SHARPY") && b.name().startsWith("SHARPY"))
			|| (a.name().startsWith("SILKY") && b.name().startsWith("SILKY"))
			|| (a.name().startsWith("AUTOSMELT") && b.name().startsWith("AUTOSMELT"))
			|| (a.name().startsWith("FREEZY") && b.name().startsWith("FREEZY"))
			|| (a.name().startsWith("MOMENTUM") && b.name().startsWith("MOMENTUM"))
			|| (a.name().startsWith("BROAD_TOUCH") && b.name().startsWith("BROAD_TOUCH"))
			|| (a.name().startsWith("FRENZY") && b.name().startsWith("FRENZY"))
			|| (a.name().startsWith("TITAN") && b.name().startsWith("TITAN"))
			|| (a.name().startsWith("SCHOLAR") && b.name().startsWith("SCHOLAR"))
			|| (a.name().startsWith("INSIGHT") && b.name().startsWith("INSIGHT"))
			|| (a.name().startsWith("MOONLIT") && b.name().startsWith("MOONLIT"))
			|| (a.name().startsWith("RANGER") && b.name().startsWith("RANGER"))
			|| (a.name().startsWith("ANGLER") && b.name().startsWith("ANGLER"))
			|| (a.name().startsWith("MARKSMAN") && b.name().startsWith("MARKSMAN"))
			|| (a.name().startsWith("IMPALER") && b.name().startsWith("IMPALER"))
			|| (a.name().startsWith("ARCANE") && b.name().startsWith("ARCANE"))
			|| (a.name().startsWith("AERODYNAMIC") && b.name().startsWith("AERODYNAMIC"))
			|| (a.name().startsWith("FIREWARD") && b.name().startsWith("FIREWARD"))
			|| (a.name().startsWith("THORNS") && b.name().startsWith("THORNS"))
			|| (a.name().startsWith("GUARDIAN") && b.name().startsWith("GUARDIAN"))
			|| (a.name().startsWith("HEMORRHAGE") && b.name().startsWith("HEMORRHAGE"))
			|| (a.name().startsWith("VOLLEY") && b.name().startsWith("VOLLEY"))
			|| (a.name().startsWith("HARVEST") && b.name().startsWith("HARVEST"))
			|| (a.name().startsWith("STEADFAST") && b.name().startsWith("STEADFAST"))
			|| (a.name().startsWith("SEARING") && b.name().startsWith("SEARING"))
			|| (a.name().startsWith("VITALITY") && b.name().startsWith("VITALITY"))
			|| (a.name().startsWith("RIME") && b.name().startsWith("RIME"))
			|| (a.name().startsWith("EXECUTIONER") && b.name().startsWith("EXECUTIONER"))
			|| (a.name().startsWith("REAVING") && b.name().startsWith("REAVING"))
			|| (a.name().startsWith("STALKER") && b.name().startsWith("STALKER"))
			|| (a.name().startsWith("RIPOSTE") && b.name().startsWith("RIPOSTE"))
			|| (a.name().startsWith("CRUSHING") && b.name().startsWith("CRUSHING"))
			|| (a.name().startsWith("ANCIENT") && b.name().startsWith("ANCIENT"));
	}

	public static int getAdditionalSlotCostForNextTrait(ItemStack toolStack, ItemStack ingredientStack) {
		Trait nextTrait = getNextBindableTrait(toolStack, ingredientStack);
		if (nextTrait == null) {
			return 0;
		}
		int currentFamilyCost = 0;
		for (Trait owned : getTraits(toolStack)) {
			if (sameFamily(owned, nextTrait)) {
				currentFamilyCost = Math.max(currentFamilyCost, getBillableSlotCost(owned));
			}
		}
		return Math.max(0, getBillableSlotCost(nextTrait) - currentFamilyCost);
	}

	public static boolean bindNextTrait(ItemStack toolStack, ItemStack ingredientStack) {
		Trait nextTrait = getNextBindableTrait(toolStack, ingredientStack);
		if (nextTrait == null) {
			return false;
		}

		List<Trait> owned = getTraits(toolStack);
		if (nextTrait == Trait.SPEEDY_II || nextTrait == Trait.SPEEDY_III) {
			owned.remove(Trait.SPEEDY_I);
			owned.remove(Trait.SPEEDY_II);
			owned.remove(Trait.SPEEDY_III);
		}
		if (nextTrait == Trait.POISON_I || nextTrait == Trait.POISON_II) {
			owned.remove(Trait.POISON_I);
			owned.remove(Trait.POISON_II);
		}
		if (nextTrait == Trait.SUSTAINING_I || nextTrait == Trait.SUSTAINING_II || nextTrait == Trait.SUSTAINING_III) {
			owned.remove(Trait.SUSTAINING_I);
			owned.remove(Trait.SUSTAINING_II);
			owned.remove(Trait.SUSTAINING_III);
		}
		if (nextTrait == Trait.SHARPY_I || nextTrait == Trait.SHARPY_II || nextTrait == Trait.SHARPY_III) {
			owned.remove(Trait.SHARPY_I);
			owned.remove(Trait.SHARPY_II);
			owned.remove(Trait.SHARPY_III);
		}
		if (nextTrait == Trait.AUTOSMELT_II) {
			owned.remove(Trait.AUTOSMELT);
		}
		if (nextTrait == Trait.SILKY_II) {
			owned.remove(Trait.SILKY_I);
		}
		if (nextTrait == Trait.FREEZY_I || nextTrait == Trait.FREEZY_II) {
			owned.remove(Trait.FREEZY_I);
			owned.remove(Trait.FREEZY_II);
		}
		if (nextTrait == Trait.MOMENTUM_II) {
			owned.remove(Trait.MOMENTUM_I);
		}
		if (nextTrait == Trait.BROAD_TOUCH_II) {
			owned.remove(Trait.BROAD_TOUCH_I);
		}
		if (nextTrait == Trait.FRENZY_I || nextTrait == Trait.FRENZY_II || nextTrait == Trait.FRENZY_III) {
			owned.remove(Trait.FRENZY_I);
			owned.remove(Trait.FRENZY_II);
			owned.remove(Trait.FRENZY_III);
		}
		if (nextTrait == Trait.TITAN_I || nextTrait == Trait.TITAN_II) {
			owned.remove(Trait.TITAN_I);
			owned.remove(Trait.TITAN_II);
		}
		if (nextTrait == Trait.SCHOLAR_I || nextTrait == Trait.SCHOLAR_II) {
			owned.remove(Trait.SCHOLAR_I);
			owned.remove(Trait.SCHOLAR_II);
		}
		if (nextTrait == Trait.INSIGHT_I || nextTrait == Trait.INSIGHT_II || nextTrait == Trait.INSIGHT_III) {
			owned.remove(Trait.INSIGHT_I);
			owned.remove(Trait.INSIGHT_II);
			owned.remove(Trait.INSIGHT_III);
		}
		if (nextTrait == Trait.MOONLIT_I || nextTrait == Trait.MOONLIT_II) {
			owned.remove(Trait.MOONLIT_I);
			owned.remove(Trait.MOONLIT_II);
		}
		if (nextTrait == Trait.RANGER_I || nextTrait == Trait.RANGER_II) {
			owned.remove(Trait.RANGER_I);
			owned.remove(Trait.RANGER_II);
		}
		if (nextTrait == Trait.ANGLER_I || nextTrait == Trait.ANGLER_II) {
			owned.remove(Trait.ANGLER_I);
			owned.remove(Trait.ANGLER_II);
		}
		if (nextTrait == Trait.MARKSMAN_I || nextTrait == Trait.MARKSMAN_II) {
			owned.remove(Trait.MARKSMAN_I);
			owned.remove(Trait.MARKSMAN_II);
		}
		if (nextTrait == Trait.IMPALER_I || nextTrait == Trait.IMPALER_II) {
			owned.remove(Trait.IMPALER_I);
			owned.remove(Trait.IMPALER_II);
		}
		if (nextTrait == Trait.ARCANE_I || nextTrait == Trait.ARCANE_II) {
			owned.remove(Trait.ARCANE_I);
			owned.remove(Trait.ARCANE_II);
		}
		if (nextTrait == Trait.AERODYNAMIC_I || nextTrait == Trait.AERODYNAMIC_II) {
			owned.remove(Trait.AERODYNAMIC_I);
			owned.remove(Trait.AERODYNAMIC_II);
		}
		if (nextTrait == Trait.FIREWARD_I || nextTrait == Trait.FIREWARD_II) {
			owned.remove(Trait.FIREWARD_I);
			owned.remove(Trait.FIREWARD_II);
		}
		if (nextTrait == Trait.THORNS_I || nextTrait == Trait.THORNS_II) {
			owned.remove(Trait.THORNS_I);
			owned.remove(Trait.THORNS_II);
		}
		if (nextTrait == Trait.GUARDIAN_I || nextTrait == Trait.GUARDIAN_II) {
			owned.remove(Trait.GUARDIAN_I);
			owned.remove(Trait.GUARDIAN_II);
		}
		owned.removeIf(t -> sameFamily(t, nextTrait));
		owned.add(nextTrait);
		writeTraits(toolStack, owned);
		ingredientStack.shrink(1);
		return true;
	}

	public static void ensureSlotLimit(ItemStack stack) {
		int slots = ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.ULTIMATE
			? 8 + getBonusTraitSlots(stack)
			: getTraitSlotsForTier(ToolProgressionHelper.getStoredTier(stack)) + getBonusTraitSlots(stack);
		List<Trait> traits = getTraits(stack);
		if (getUsedTraitSlots(stack) <= slots) {
			return;
		}

		List<Trait> trimmed = new ArrayList<>();
		int used = 0;
		for (Trait trait : traits) {
			int cost = getBillableSlotCost(trait);
			if (used + cost <= slots) {
				trimmed.add(trait);
				used += cost;
			}
		}

		traits = trimmed;
		writeTraits(stack, traits);
	}

	public static float getAttackSpeedBonus(ItemStack stack) {
		float total = 0.0f;
		for (Trait trait : getTraits(stack)) {
			total += trait.attackSpeedBonus();
		}
		total += getSynergyAttackSpeedBonus(stack);
		return total;
	}

	public static float getMiningSpeedBonus(ItemStack stack) {
		float total = 0.0f;
		for (Trait trait : getTraits(stack)) {
			total += trait.miningSpeedBonus();
		}
		return total;
	}

	public static float getAttackDamagePenalty(ItemStack stack) {
		float total = 0.0f;
		for (Trait trait : getTraits(stack)) {
			total += trait.attackDamagePenalty();
		}
		return total;
	}

	public static float getXpPenalty(ItemStack stack) {
		float total = 0.0f;
		for (Trait trait : getTraits(stack)) {
			total += trait.xpPenalty();
		}
		return total;
	}

	public static float getMiningSpeedMultiplier(ItemStack stack) {
		return Math.max(0.05f, 1.0f + getMiningSpeedBonus(stack) + getCrushingMiningBonus(stack) + getSynergyMiningSpeedBonus(stack));
	}

	public static float getAttackDamageMultiplier(ItemStack stack) {
		return Math.max(0.05f, 1.0f - getAttackDamagePenalty(stack));
	}

	public static float getXpMultiplier(ItemStack stack) {
		return Math.max(0.05f, 1.0f - getXpPenalty(stack) + getSynergyXpBonus(stack));
	}

	public static List<Component> getTraitTooltip(ItemStack stack) {
		int tier = ToolProgressionHelper.getStoredTier(stack);
		List<Trait> traits = getTraits(stack);

		int base = ToolProgressionHelper.getToolType(stack) == ToolProgressionHelper.ToolType.ULTIMATE
			? 8 : getTraitSlotsForTier(tier);
		int totalSlots = base + getBonusTraitSlots(stack);
		List<Component> lines = new ArrayList<>();
		lines.add(Component.literal("§6Traits: §f" + getUsedTraitSlots(stack) + "§7/§f" + totalSlots));
		if (traits.isEmpty()) {
			lines.add(Component.literal("§8No traits bound"));
			lines.add(Component.literal("§8Use valid trait ingredients (Redstone, Bread, Eye, etc.)"));
			return lines;
		}
		for (Trait trait : traits) {
			int billable = getBillableSlotCost(trait);
			lines.add(Component.literal("§d- " + trait.displayName() + " §8(" + billable + " slot" + (billable > 1 ? "s" : "") + ")§7: " + trait.description()));
		}
		List<Synergy> synergies = getActiveSynergies(stack);
		if (!synergies.isEmpty()) {
			lines.add(Component.literal("§eSynergies:"));
			for (Synergy syn : synergies) {
				lines.add(Component.literal("§6✦ §e" + syn.displayName() + " §8- §7" + syn.description()));
			}
		}
		return lines;
	}

	public static void writeTraits(ItemStack stack, List<Trait> traits) {
		String raw = String.join(",", traits.stream().map(Trait::id).toList());
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(NBT_TRAITS, raw));
	}

	public static float getAttackDamageBonusFlat(ItemStack stack) {
		float total = 0.0f;
		for (Trait trait : getTraits(stack)) {
			total += trait.attackDamageBonusFlat();
		}
		total += getSynergyAttackDamageFlatBonus(stack);
		return total;
	}

	public static int getPoisonDurationTicks(ItemStack stack) {
		if (hasTrait(stack, Trait.POISON_II)) {
			return 200;
		}
		if (hasTrait(stack, Trait.POISON_I)) {
			return 100;
		}
		return 0;
	}

	public static int getSustainingFoodIntervalTicks(ItemStack stack) {
		if (hasTrait(stack, Trait.SUSTAINING_I)) {
			return 600;
		}
		if (hasTrait(stack, Trait.SUSTAINING_II)) {
			return 400;
		}
		if (hasTrait(stack, Trait.SUSTAINING_III)) {
			return 600;
		}
		return 0;
	}

	public static int getSustainingFoodAmount(ItemStack stack) {
		if (hasTrait(stack, Trait.SUSTAINING_III)) {
			return 2;
		}
		if (hasTrait(stack, Trait.SUSTAINING_I) || hasTrait(stack, Trait.SUSTAINING_II)) {
			return 1;
		}
		return 0;
	}

	public static int getSustainingSaturationIntervalTicks(ItemStack stack) {
		if (hasTrait(stack, Trait.SUSTAINING_I)) {
			return 400;
		}
		if (hasTrait(stack, Trait.SUSTAINING_II)) {
			return 300;
		}
		if (hasTrait(stack, Trait.SUSTAINING_III)) {
			return 200;
		}
		return 0;
	}

	public static float getSustainingSaturationAmount(ItemStack stack) {
		if (hasTrait(stack, Trait.SUSTAINING_I)) {
			return 1.0f;
		}
		if (hasTrait(stack, Trait.SUSTAINING_II) || hasTrait(stack, Trait.SUSTAINING_III)) {
			return 2.0f;
		}
		return 0.0f;
	}

	public static float getLootPenalty(ItemStack stack) {
		if (hasTrait(stack, Trait.SUSTAINING_I)) {
			return 0.20f;
		}
		if (hasTrait(stack, Trait.SUSTAINING_II)) {
			return 0.35f;
		}
		if (hasTrait(stack, Trait.SUSTAINING_III)) {
			return 0.50f;
		}
		return 0.0f;
	}

	private static boolean isMiningTrait(Trait trait) {
		return trait == Trait.MOMENTUM_I || trait == Trait.MOMENTUM_II
			|| trait == Trait.BROAD_TOUCH_I || trait == Trait.BROAD_TOUCH_II
			|| trait == Trait.SILKY_I || trait == Trait.SILKY_II
			|| trait == Trait.AUTOSMELT || trait == Trait.AUTOSMELT_II
			|| trait == Trait.MOONLIT_I || trait == Trait.MOONLIT_II
			|| trait == Trait.MAGNETIC_I
			|| trait == Trait.CRUSHING_I || trait == Trait.CRUSHING_II
			|| trait == Trait.ANCIENT_I || trait == Trait.ANCIENT_II;
	}

	private static boolean isUtilityTrait(Trait trait) {
		return trait == Trait.SCHOLAR_I || trait == Trait.SCHOLAR_II
			|| trait == Trait.INSIGHT_I || trait == Trait.INSIGHT_II || trait == Trait.INSIGHT_III
			|| trait == Trait.SOULBOUND || trait == Trait.MODIFIABLE_I
			|| trait == Trait.SUSTAINING_I || trait == Trait.SUSTAINING_II || trait == Trait.SUSTAINING_III
			|| trait == Trait.BOUNTIFUL_I || trait == Trait.SCAVENGER_I
			|| trait == Trait.PURIFYING_I || trait == Trait.SWIFTSTEP_I || trait == Trait.BULWARK_I;
	}

	private static boolean isMeleeCombatTrait(Trait trait) {
		return trait == Trait.POISON_I || trait == Trait.POISON_II
			|| trait == Trait.FREEZY_I || trait == Trait.FREEZY_II
			|| trait == Trait.SHARPY_I || trait == Trait.SHARPY_II || trait == Trait.SHARPY_III
			|| trait == Trait.TITAN_I || trait == Trait.TITAN_II
			|| trait == Trait.HEMORRHAGE_I || trait == Trait.HEMORRHAGE_II
			|| trait == Trait.SEARING_I || trait == Trait.SEARING_II
			|| trait == Trait.VITALITY_I || trait == Trait.VITALITY_II
			|| trait == Trait.RIME_I || trait == Trait.RIME_II
			|| trait == Trait.EXECUTIONER_I || trait == Trait.EXECUTIONER_II
			|| trait == Trait.REAVING_I || trait == Trait.REAVING_II;
	}

	private static boolean isMeleeOnlyCombatTrait(Trait trait) {
		return trait == Trait.SHARPY_I || trait == Trait.SHARPY_II || trait == Trait.SHARPY_III
			|| trait == Trait.TITAN_I || trait == Trait.TITAN_II;
	}

	private static boolean isRangedCombatTrait(Trait trait) {
		return trait == Trait.POISON_I || trait == Trait.POISON_II
			|| trait == Trait.FREEZY_I || trait == Trait.FREEZY_II
			|| trait == Trait.RANGER_I || trait == Trait.RANGER_II
			|| trait == Trait.VOLLEY_I || trait == Trait.VOLLEY_II
			|| trait == Trait.SEARING_I || trait == Trait.SEARING_II
			|| trait == Trait.RIME_I || trait == Trait.RIME_II
			|| trait == Trait.STALKER_I || trait == Trait.STALKER_II
			|| trait == Trait.REAVING_I || trait == Trait.REAVING_II;
	}

	public static boolean isTraitAllowedForTool(Trait trait, ToolProgressionHelper.ToolType type) {
		if (type == ToolProgressionHelper.ToolType.NONE) {
			return false;
		}
		// Ultimate tool can use all traits
		if (type == ToolProgressionHelper.ToolType.ULTIMATE) {
			return true;
		}
		if (trait == Trait.THORNS_I || trait == Trait.THORNS_II || trait == Trait.FIREWARD_I || trait == Trait.FIREWARD_II) {
			return type == ToolProgressionHelper.ToolType.ARMOR;
		}
		if (trait == Trait.GUARDIAN_I || trait == Trait.GUARDIAN_II || trait == Trait.REPULSE_I) {
			return type == ToolProgressionHelper.ToolType.SHIELD;
		}
		if (trait == Trait.ANGLER_I || trait == Trait.ANGLER_II || trait == Trait.REEL_I) {
			return type == ToolProgressionHelper.ToolType.FISHING_ROD || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.MARKSMAN_I || trait == Trait.MARKSMAN_II || trait == Trait.QUICK_LOAD_I) {
			return type == ToolProgressionHelper.ToolType.CROSSBOW || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.RETURNING_I || trait == Trait.RIPTIDE_I || trait == Trait.IMPALER_I || trait == Trait.IMPALER_II) {
			return type == ToolProgressionHelper.ToolType.TRIDENT || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.ARCANE_I || trait == Trait.ARCANE_II || trait == Trait.CHANNELING_I) {
			return type == ToolProgressionHelper.ToolType.STAFF || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.AERODYNAMIC_I || trait == Trait.AERODYNAMIC_II || trait == Trait.FEATHERFALL_I) {
			return type == ToolProgressionHelper.ToolType.WINGS || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.ADAPTIVE_I) {
			return type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.HARVEST_I || trait == Trait.HARVEST_II) {
			return type == ToolProgressionHelper.ToolType.HOE || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.STEADFAST_I || trait == Trait.STEADFAST_II) {
			return type == ToolProgressionHelper.ToolType.SHIELD || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.VOLLEY_I || trait == Trait.VOLLEY_II) {
			return type == ToolProgressionHelper.ToolType.BOW || type == ToolProgressionHelper.ToolType.CROSSBOW
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.HEMORRHAGE_I || trait == Trait.HEMORRHAGE_II) {
			return type == ToolProgressionHelper.ToolType.SWORD || type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.SPEAR || type == ToolProgressionHelper.ToolType.FLAIL
				|| type == ToolProgressionHelper.ToolType.TRIDENT || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.SEARING_I || trait == Trait.SEARING_II) {
			return type == ToolProgressionHelper.ToolType.SWORD || type == ToolProgressionHelper.ToolType.BOW
				|| type == ToolProgressionHelper.ToolType.CROSSBOW || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.VITALITY_I || trait == Trait.VITALITY_II) {
			return type == ToolProgressionHelper.ToolType.SWORD || type == ToolProgressionHelper.ToolType.SPEAR
				|| type == ToolProgressionHelper.ToolType.FLAIL || type == ToolProgressionHelper.ToolType.TRIDENT
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.RIME_I || trait == Trait.RIME_II) {
			return type == ToolProgressionHelper.ToolType.SWORD || type == ToolProgressionHelper.ToolType.BOW
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.EXECUTIONER_I || trait == Trait.EXECUTIONER_II) {
			return type == ToolProgressionHelper.ToolType.SWORD || type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.SPEAR || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.REAVING_I || trait == Trait.REAVING_II) {
			return type == ToolProgressionHelper.ToolType.SWORD || type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.BOW || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.STALKER_I || trait == Trait.STALKER_II) {
			return type == ToolProgressionHelper.ToolType.BOW || type == ToolProgressionHelper.ToolType.CROSSBOW
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.RIPOSTE_I || trait == Trait.RIPOSTE_II) {
			return type == ToolProgressionHelper.ToolType.SHIELD || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.CRUSHING_I || trait == Trait.CRUSHING_II) {
			return type == ToolProgressionHelper.ToolType.PICKAXE || type == ToolProgressionHelper.ToolType.HAMMER
				|| type == ToolProgressionHelper.ToolType.SHOVEL || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.ANCIENT_I || trait == Trait.ANCIENT_II) {
			return type == ToolProgressionHelper.ToolType.PICKAXE || type == ToolProgressionHelper.ToolType.HAMMER
				|| type == ToolProgressionHelper.ToolType.SHOVEL || type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (type == ToolProgressionHelper.ToolType.FISHING_ROD) {
			return (isUtilityTrait(trait) || trait == Trait.MAGNETIC_I) && !isMiningTrait(trait)
				&& !isMeleeCombatTrait(trait) && trait != Trait.RANGER_I && trait != Trait.RANGER_II;
		}
		if (type == ToolProgressionHelper.ToolType.CROSSBOW) {
			return (isUtilityTrait(trait) || isRangedCombatTrait(trait) || trait == Trait.FRENZY_I
				|| trait == Trait.FRENZY_II || trait == Trait.FRENZY_III || trait == Trait.SPEEDY_I
				|| trait == Trait.SPEEDY_II || trait == Trait.SPEEDY_III)
				&& !isMiningTrait(trait) && !isMeleeOnlyCombatTrait(trait);
		}
		if (type == ToolProgressionHelper.ToolType.TRIDENT) {
			return (isUtilityTrait(trait) || isMeleeCombatTrait(trait) || isRangedCombatTrait(trait)
				|| trait == Trait.FRENZY_I || trait == Trait.FRENZY_II || trait == Trait.FRENZY_III
				|| trait == Trait.SPEEDY_I || trait == Trait.SPEEDY_II || trait == Trait.SPEEDY_III)
				&& !isMiningTrait(trait) && trait != Trait.RANGER_I && trait != Trait.RANGER_II;
		}
		if (type == ToolProgressionHelper.ToolType.STAFF) {
			return isUtilityTrait(trait) && !isMiningTrait(trait) && !isMeleeOnlyCombatTrait(trait)
				&& trait != Trait.RANGER_I && trait != Trait.RANGER_II && trait != Trait.POISON_I
				&& trait != Trait.POISON_II && trait != Trait.FREEZY_I && trait != Trait.FREEZY_II;
		}
		if (type == ToolProgressionHelper.ToolType.WINGS) {
			return isUtilityTrait(trait) && !isMiningTrait(trait) && !isMeleeCombatTrait(trait)
				&& !isRangedCombatTrait(trait) && trait != Trait.RANGER_I && trait != Trait.RANGER_II;
		}
		if (type == ToolProgressionHelper.ToolType.ARMOR || type == ToolProgressionHelper.ToolType.SHIELD) {
			if (trait == Trait.MOMENTUM_I || trait == Trait.MOMENTUM_II
				|| trait == Trait.BROAD_TOUCH_I || trait == Trait.BROAD_TOUCH_II
				|| trait == Trait.SILKY_I || trait == Trait.SILKY_II
				|| trait == Trait.AUTOSMELT || trait == Trait.AUTOSMELT_II
				|| trait == Trait.BOUNTIFUL_I || trait == Trait.SCAVENGER_I
				|| trait == Trait.MAGNETIC_I || trait == Trait.MOONLIT_I || trait == Trait.MOONLIT_II
				|| trait == Trait.RANGER_I || trait == Trait.RANGER_II) {
				return false;
			}
			if (type == ToolProgressionHelper.ToolType.SHIELD
				&& (trait == Trait.POISON_I || trait == Trait.POISON_II
					|| trait == Trait.FREEZY_I || trait == Trait.FREEZY_II
					|| trait == Trait.SHARPY_I || trait == Trait.SHARPY_II || trait == Trait.SHARPY_III
					|| trait == Trait.TITAN_I || trait == Trait.TITAN_II)) {
				return false;
			}
			if (type == ToolProgressionHelper.ToolType.ARMOR
				&& (trait == Trait.POISON_I || trait == Trait.POISON_II
					|| trait == Trait.FREEZY_I || trait == Trait.FREEZY_II
					|| trait == Trait.SHARPY_I || trait == Trait.SHARPY_II || trait == Trait.SHARPY_III)) {
				return false;
			}
		}
		if (trait == Trait.POISON_I || trait == Trait.POISON_II) {
			return type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.SWORD
				|| type == ToolProgressionHelper.ToolType.BOW
				|| type == ToolProgressionHelper.ToolType.CROSSBOW
				|| type == ToolProgressionHelper.ToolType.TRIDENT
				|| type == ToolProgressionHelper.ToolType.SPEAR
				|| type == ToolProgressionHelper.ToolType.FLAIL;
		}
		if (trait == Trait.SHARPY_I || trait == Trait.SHARPY_II || trait == Trait.SHARPY_III) {
			return type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.SWORD
				|| type == ToolProgressionHelper.ToolType.TRIDENT
				|| type == ToolProgressionHelper.ToolType.SPEAR
				|| type == ToolProgressionHelper.ToolType.FLAIL;
		}
		if (trait == Trait.SILKY_I || trait == Trait.SILKY_II) {
			return type != ToolProgressionHelper.ToolType.BOW;
		}
		if (trait == Trait.FREEZY_I || trait == Trait.FREEZY_II) {
			return type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.SWORD
				|| type == ToolProgressionHelper.ToolType.BOW
				|| type == ToolProgressionHelper.ToolType.CROSSBOW
				|| type == ToolProgressionHelper.ToolType.TRIDENT
				|| type == ToolProgressionHelper.ToolType.SPEAR
				|| type == ToolProgressionHelper.ToolType.FLAIL
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.MOMENTUM_I || trait == Trait.MOMENTUM_II) {
			return type == ToolProgressionHelper.ToolType.PICKAXE
				|| type == ToolProgressionHelper.ToolType.HAMMER
				|| type == ToolProgressionHelper.ToolType.SHOVEL
				|| type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.BROAD_TOUCH_I || trait == Trait.BROAD_TOUCH_II) {
			return type == ToolProgressionHelper.ToolType.PICKAXE
				|| type == ToolProgressionHelper.ToolType.HAMMER
				|| type == ToolProgressionHelper.ToolType.SHOVEL
				|| type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.FRENZY_I || trait == Trait.FRENZY_II || trait == Trait.FRENZY_III) {
			return type != ToolProgressionHelper.ToolType.BOW;
		}
		if (trait == Trait.TITAN_I || trait == Trait.TITAN_II) {
			return type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.SWORD
				|| type == ToolProgressionHelper.ToolType.TRIDENT
				|| type == ToolProgressionHelper.ToolType.SPEAR
				|| type == ToolProgressionHelper.ToolType.FLAIL
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.MOONLIT_I || trait == Trait.MOONLIT_II) {
			return type == ToolProgressionHelper.ToolType.PICKAXE
				|| type == ToolProgressionHelper.ToolType.HAMMER
				|| type == ToolProgressionHelper.ToolType.SHOVEL
				|| type == ToolProgressionHelper.ToolType.AXE
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.RANGER_I || trait == Trait.RANGER_II) {
			return type == ToolProgressionHelper.ToolType.BOW
				|| type == ToolProgressionHelper.ToolType.CROSSBOW
				|| type == ToolProgressionHelper.ToolType.ULTIMATE;
		}
		if (trait == Trait.BOUNTIFUL_I || trait == Trait.SCAVENGER_I || trait == Trait.PURIFYING_I
			|| trait == Trait.SWIFTSTEP_I || trait == Trait.BULWARK_I) {
			return true;
		}
		return true;
	}

	public static int getSustainingRank(ItemStack stack) {
		if (hasTrait(stack, Trait.SUSTAINING_III)) {
			return 3;
		}
		if (hasTrait(stack, Trait.SUSTAINING_II)) {
			return 2;
		}
		if (hasTrait(stack, Trait.SUSTAINING_I)) {
			return 1;
		}
		return 0;
	}

	public static float getThornsDamage(ItemStack stack) {
		if (hasTrait(stack, Trait.THORNS_II)) {
			return 5.0f;
		}
		if (hasTrait(stack, Trait.THORNS_I)) {
			return 2.0f;
		}
		return 0.0f;
	}

	public static int getFirewardAmplifier(ItemStack stack) {
		if (hasTrait(stack, Trait.FIREWARD_II)) {
			return 1;
		}
		if (hasTrait(stack, Trait.FIREWARD_I)) {
			return 0;
		}
		return -1;
	}

	public static float getGuardianBlockChanceBonus(ItemStack stack) {
		if (hasTrait(stack, Trait.GUARDIAN_II)) {
			return 0.22f;
		}
		if (hasTrait(stack, Trait.GUARDIAN_I)) {
			return 0.12f;
		}
		return 0.0f;
	}

	public static float getGuardianBlockReductionBonus(ItemStack stack) {
		if (hasTrait(stack, Trait.GUARDIAN_II)) {
			return 0.18f;
		}
		if (hasTrait(stack, Trait.GUARDIAN_I)) {
			return 0.10f;
		}
		return 0.0f;
	}

	public static float getAnglerTreasureChance(ItemStack stack) {
		if (hasTrait(stack, Trait.ANGLER_II)) {
			return 0.25f;
		}
		if (hasTrait(stack, Trait.ANGLER_I)) {
			return 0.12f;
		}
		return 0.0f;
	}

	public static float getMarksmanDamageBonus(ItemStack stack) {
		if (hasTrait(stack, Trait.MARKSMAN_II)) {
			return 2.0f;
		}
		if (hasTrait(stack, Trait.MARKSMAN_I)) {
			return 1.0f;
		}
		return 0.0f;
	}

	public static float getImpalerDamageBonus(ItemStack stack) {
		if (hasTrait(stack, Trait.IMPALER_II)) {
			return 4.0f;
		}
		if (hasTrait(stack, Trait.IMPALER_I)) {
			return 2.0f;
		}
		return 0.0f;
	}

	public static int getStaffCooldownTicks(ItemStack stack) {
		int base = 25;
		if (hasTrait(stack, Trait.ARCANE_II)) {
			return (int) (base * 0.65f);
		}
		if (hasTrait(stack, Trait.ARCANE_I)) {
			return (int) (base * 0.80f);
		}
		return base;
	}

	public static float getArcaneBoltDamageMultiplier(ItemStack stack) {
		if (hasTrait(stack, Trait.ARCANE_II)) {
			return 2.0f;
		}
		if (hasTrait(stack, Trait.ARCANE_I)) {
			return 1.5f;
		}
		return 1.0f;
	}

	public static float getAerodynamicGlideXpMultiplier(ItemStack stack) {
		if (hasTrait(stack, Trait.AERODYNAMIC_II)) {
			return 2.0f;
		}
		if (hasTrait(stack, Trait.AERODYNAMIC_I)) {
			return 1.5f;
		}
		return 1.0f;
	}

	public static float getFeatherfallDamageMultiplier(ItemStack stack) {
		return hasTrait(stack, Trait.FEATHERFALL_I) ? 0.5f : 1.0f;
	}

	public static boolean hasQuickLoad(ItemStack stack) {
		return hasTrait(stack, Trait.QUICK_LOAD_I);
	}

	public static boolean hasReturning(ItemStack stack) {
		return hasTrait(stack, Trait.RETURNING_I);
	}

	public static boolean hasRiptide(ItemStack stack) {
		return hasTrait(stack, Trait.RIPTIDE_I);
	}

	public static boolean hasChanneling(ItemStack stack) {
		return hasTrait(stack, Trait.CHANNELING_I);
	}

	public static boolean hasReel(ItemStack stack) {
		return hasTrait(stack, Trait.REEL_I);
	}

	public static float getMoonlitMiningBonus(ItemStack stack, boolean isNight) {
		if (!isNight) {
			return 0.0f;
		}
		if (hasTrait(stack, Trait.MOONLIT_II)) {
			return 0.20f;
		}
		if (hasTrait(stack, Trait.MOONLIT_I)) {
			return 0.12f;
		}
		return 0.0f;
	}

	// ── Freezy helpers ───────────────────────────────────────────────────────
	public static int getFreezeDurationTicks(ItemStack stack) {
		if (hasTrait(stack, Trait.FREEZY_II)) return 300;
		if (hasTrait(stack, Trait.FREEZY_I)) return 200;
		return 0;
	}

	public static int getFreezeAmplifier(ItemStack stack) {
		return hasTrait(stack, Trait.FREEZY_II) ? 1 : 0;
	}

	public static int getHemorrhageDurationTicks(ItemStack stack) {
		if (hasTrait(stack, Trait.HEMORRHAGE_II)) {
			return 120;
		}
		if (hasTrait(stack, Trait.HEMORRHAGE_I)) {
			return 60;
		}
		return 0;
	}

	public static float getVolleyChance(ItemStack stack) {
		if (hasTrait(stack, Trait.VOLLEY_II)) {
			return 0.30f;
		}
		if (hasTrait(stack, Trait.VOLLEY_I)) {
			return 0.15f;
		}
		return 0.0f;
	}

	public static float getHarvestBonusChance(ItemStack stack) {
		if (hasTrait(stack, Trait.HARVEST_II)) {
			return 0.25f;
		}
		if (hasTrait(stack, Trait.HARVEST_I)) {
			return 0.12f;
		}
		return 0.0f;
	}

	public static float getStrainGainMultiplier(ItemStack stack) {
		if (hasTrait(stack, Trait.STEADFAST_II)) {
			return 0.50f;
		}
		if (hasTrait(stack, Trait.STEADFAST_I)) {
			return 0.75f;
		}
		return 1.0f;
	}

	public static int getSearingFireSeconds(ItemStack stack) {
		if (hasTrait(stack, Trait.SEARING_II)) {
			return 6;
		}
		if (hasTrait(stack, Trait.SEARING_I)) {
			return 3;
		}
		return 0;
	}

	public static float getVitalityHealAmount(ItemStack stack) {
		if (hasTrait(stack, Trait.VITALITY_II)) {
			return 4.0f;
		}
		if (hasTrait(stack, Trait.VITALITY_I)) {
			return 2.0f;
		}
		return 0.0f;
	}

	public static int getRimeSlownessAmplifier(ItemStack stack) {
		if (hasTrait(stack, Trait.RIME_II)) {
			return 1;
		}
		if (hasTrait(stack, Trait.RIME_I)) {
			return 0;
		}
		return -1;
	}

	public static float getRimeBurningDamageMultiplier(ItemStack stack) {
		if (hasTrait(stack, Trait.RIME_II)) {
			return 1.30f;
		}
		if (hasTrait(stack, Trait.RIME_I)) {
			return 1.15f;
		}
		return 1.0f;
	}

	public static float getExecutionerHealthThreshold(ItemStack stack) {
		if (hasTrait(stack, Trait.EXECUTIONER_II)) {
			return 0.50f;
		}
		if (hasTrait(stack, Trait.EXECUTIONER_I)) {
			return 0.35f;
		}
		return 0.0f;
	}

	public static float getExecutionerDamageMultiplier(ItemStack stack) {
		if (hasTrait(stack, Trait.EXECUTIONER_II)) {
			return 1.50f;
		}
		if (hasTrait(stack, Trait.EXECUTIONER_I)) {
			return 1.25f;
		}
		return 1.0f;
	}

	public static float getStalkerProjectileDamageMultiplier(ItemStack stack, boolean sneaking) {
		if (!sneaking) {
			return 1.0f;
		}
		if (hasTrait(stack, Trait.STALKER_II)) {
			return 1.40f;
		}
		if (hasTrait(stack, Trait.STALKER_I)) {
			return 1.20f;
		}
		return 1.0f;
	}

	public static float getRiposteDamage(ItemStack stack) {
		if (hasTrait(stack, Trait.RIPOSTE_II)) {
			return 6.0f;
		}
		if (hasTrait(stack, Trait.RIPOSTE_I)) {
			return 3.0f;
		}
		return 0.0f;
	}

	public static float getCrushingMiningBonus(ItemStack stack) {
		if (hasTrait(stack, Trait.CRUSHING_II)) {
			return 0.25f;
		}
		if (hasTrait(stack, Trait.CRUSHING_I)) {
			return 0.12f;
		}
		return 0.0f;
	}

	// ── Momentum NBT helpers ──────────────────────────────────────────────────
	private static final String NBT_MOMENTUM_BLOCKS = "togMomentumBlocks";
	private static final String NBT_MOMENTUM_LAST_TIME = "togMomentumLastTime";

	public static int getMomentumBlocks(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBT_MOMENTUM_BLOCKS, 0);
	}

	public static void setMomentumBlocks(ItemStack stack, int blocks) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(NBT_MOMENTUM_BLOCKS, blocks));
	}

	public static long getMomentumLastTime(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getLongOr(NBT_MOMENTUM_LAST_TIME, 0L);
	}

	public static void setMomentumLastTime(ItemStack stack, long time) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putLong(NBT_MOMENTUM_LAST_TIME, time));
	}

	public static int getMomentumMaxBlocks(ItemStack stack) {
		if (hasTrait(stack, Trait.MOMENTUM_II)) return 40;
		if (hasTrait(stack, Trait.MOMENTUM_I)) return 50;
		return 0;
	}

	public static float getMomentumMaxBonus(ItemStack stack) {
		if (hasTrait(stack, Trait.MOMENTUM_II)) return 0.40f;
		if (hasTrait(stack, Trait.MOMENTUM_I)) return 0.25f;
		return 0.0f;
	}

	public static float getMomentumMiningBonus(ItemStack stack) {
		int maxBlocks = getMomentumMaxBlocks(stack);
		if (maxBlocks <= 0) return 0.0f;
		int current = Math.min(getMomentumBlocks(stack), maxBlocks);
		return getMomentumMaxBonus(stack) * ((float) current / maxBlocks);
	}

	// ── Broad Touch NBT helpers ───────────────────────────────────────────────
	private static final String NBT_BROAD_MODE = "togBroadMode";

	public static int getBroadTouchMode(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBT_BROAD_MODE, 0);
	}

	public static void cycleBroadTouchMode(ItemStack stack, int maxModes) {
		int current = getBroadTouchMode(stack);
		int next = (current + 1) % maxModes;
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(NBT_BROAD_MODE, next));
	}

	public static boolean hasBroadTouch(ItemStack stack) {
		return hasTrait(stack, Trait.BROAD_TOUCH_I) || hasTrait(stack, Trait.BROAD_TOUCH_II);
	}

	private static final String NBT_SILK_ACTIVE = "togSilkActive";
	private static final String NBT_AUTOSMELT_ACTIVE = "togAutosmeltActive";

	public static boolean isSilkyActive(ItemStack stack) {
		if (hasTrait(stack, Trait.SILKY_I)) {
			return true;
		}
		if (hasTrait(stack, Trait.SILKY_II)) {
			var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			return !tag.contains(NBT_SILK_ACTIVE) || tag.getBooleanOr(NBT_SILK_ACTIVE, false);
		}
		return false;
	}

	public static boolean toggleSilky(ItemStack stack) {
		var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		boolean current = !tag.contains(NBT_SILK_ACTIVE) || tag.getBooleanOr(NBT_SILK_ACTIVE, false);
		boolean newVal = !current;
		CustomData.update(DataComponents.CUSTOM_DATA, stack, t -> t.putBoolean(NBT_SILK_ACTIVE, newVal));
		return newVal;
	}

	public static boolean isAutosmeltActive(ItemStack stack) {
		if (isSilkyActive(stack)) {
			return false; // Silk touch overrides autosmelt
		}
		if (hasTrait(stack, Trait.AUTOSMELT)) {
			return true;
		}
		if (hasTrait(stack, Trait.AUTOSMELT_II)) {
			var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			return !tag.contains(NBT_AUTOSMELT_ACTIVE) || tag.getBooleanOr(NBT_AUTOSMELT_ACTIVE, false);
		}
		return false;
	}

	public static boolean toggleAutosmelt(ItemStack stack) {
		var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		boolean current = !tag.contains(NBT_AUTOSMELT_ACTIVE) || tag.getBooleanOr(NBT_AUTOSMELT_ACTIVE, false);
		boolean newVal = !current;
		CustomData.update(DataComponents.CUSTOM_DATA, stack, t -> t.putBoolean(NBT_AUTOSMELT_ACTIVE, newVal));
		return newVal;
	}

	// ── Synergy system ────────────────────────────────────────────────────────
	public enum Synergy {
		VENOM_RUSH("Venom Rush", "Poison + Speedy: +6% Atk Spd"),
		COLD_STEEL("Cold Steel", "Freezy + Sharpy: +0.5 Dmg"),
		FIELD_COOK("Field Cook", "Sustaining + Autosmelt: +4% Mine Spd"),
		KEEPERS_REACH("Keeper's Reach", "Soulbound + Magnetic: +4% Mine Spd"),
		DEEP_FLOW("Deep Flow", "Broad Touch + Momentum: +5% Mine Spd"),
		SILK_AND_SOUL("Silk & Soul", "Silky + Soulbound: +8% XP"),
		FROZEN_TEMPO("Frozen Tempo", "Freezy + Momentum: +4% Mine Spd"),
		BERSERKER_SCRIPT("Berserker Script", "Frenzy + Scholar: +4% Atk Spd"),
		IRON_PLEDGE("Iron Pledge", "Titan + Soulbound: +0.5 Dmg"),
		RANGER_TEMPO("Ranger Tempo", "Ranger + Frenzy: +3% Atk Spd"),
		MOONCRAFT("Mooncraft", "Moonlit + Scholar: +3% Mine Spd"),
		TRIUNE_FOCUS("Triune Focus", "Frenzy + Titan + Scholar: +2% Atk Spd, +0.5 Dmg"),
		NIGHTFALL_ENGINE("Nightfall Engine", "Moonlit + Momentum + Broad Touch: +3% Mine Spd"),
		ALCHEMIST_LOOP("Alchemist Loop", "Bountiful + Scavenger + Autosmelt: +5% XP");

		private final String name;
		private final String desc;

		Synergy(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}

		public String displayName() { return name; }
		public String description() { return desc; }
	}

	public static List<Synergy> getActiveSynergies(ItemStack stack) {
		List<Trait> traits = getTraits(stack);
		List<Synergy> result = new ArrayList<>();
		boolean hasPoison    = traits.contains(Trait.POISON_I)    || traits.contains(Trait.POISON_II);
		boolean hasSpeedy    = traits.contains(Trait.SPEEDY_I)    || traits.contains(Trait.SPEEDY_II)    || traits.contains(Trait.SPEEDY_III);
		boolean hasFreezy    = traits.contains(Trait.FREEZY_I)    || traits.contains(Trait.FREEZY_II);
		boolean hasSharpy    = traits.contains(Trait.SHARPY_I)    || traits.contains(Trait.SHARPY_II)    || traits.contains(Trait.SHARPY_III);
		boolean hasSustain   = traits.contains(Trait.SUSTAINING_I)|| traits.contains(Trait.SUSTAINING_II)|| traits.contains(Trait.SUSTAINING_III);
		boolean hasSmelt     = traits.contains(Trait.AUTOSMELT)   || traits.contains(Trait.AUTOSMELT_II);
		boolean hasSoulbound = traits.contains(Trait.SOULBOUND);
		boolean hasMagnetic  = traits.contains(Trait.MAGNETIC_I);
		boolean hasBroad     = traits.contains(Trait.BROAD_TOUCH_I)|| traits.contains(Trait.BROAD_TOUCH_II);
		boolean hasMomentum  = traits.contains(Trait.MOMENTUM_I)  || traits.contains(Trait.MOMENTUM_II);
		boolean hasSilky     = traits.contains(Trait.SILKY_I)     || traits.contains(Trait.SILKY_II);
		boolean hasFrenzy    = traits.contains(Trait.FRENZY_I)    || traits.contains(Trait.FRENZY_II)    || traits.contains(Trait.FRENZY_III);
		boolean hasTitan     = traits.contains(Trait.TITAN_I)     || traits.contains(Trait.TITAN_II);
		boolean hasScholar   = traits.contains(Trait.SCHOLAR_I)   || traits.contains(Trait.SCHOLAR_II);
		boolean hasMoonlit   = traits.contains(Trait.MOONLIT_I)   || traits.contains(Trait.MOONLIT_II);
		boolean hasRanger    = traits.contains(Trait.RANGER_I)    || traits.contains(Trait.RANGER_II);
		boolean hasBountiful = traits.contains(Trait.BOUNTIFUL_I);
		boolean hasScavenger = traits.contains(Trait.SCAVENGER_I);
		if (hasPoison  && hasSpeedy)    result.add(Synergy.VENOM_RUSH);
		if (hasFreezy  && hasSharpy)    result.add(Synergy.COLD_STEEL);
		if (hasSustain && hasSmelt)     result.add(Synergy.FIELD_COOK);
		if (hasSoulbound && hasMagnetic)result.add(Synergy.KEEPERS_REACH);
		if (hasBroad   && hasMomentum)  result.add(Synergy.DEEP_FLOW);
		if (hasSilky   && hasSoulbound) result.add(Synergy.SILK_AND_SOUL);
		if (hasFreezy  && hasMomentum)  result.add(Synergy.FROZEN_TEMPO);
		if (hasFrenzy  && hasScholar)   result.add(Synergy.BERSERKER_SCRIPT);
		if (hasTitan   && hasSoulbound) result.add(Synergy.IRON_PLEDGE);
		if (hasRanger  && hasFrenzy)    result.add(Synergy.RANGER_TEMPO);
		if (hasMoonlit && hasScholar)   result.add(Synergy.MOONCRAFT);
		if (hasFrenzy && hasTitan && hasScholar) result.add(Synergy.TRIUNE_FOCUS);
		if (hasMoonlit && hasMomentum && hasBroad) result.add(Synergy.NIGHTFALL_ENGINE);
		if (hasBountiful && hasScavenger && hasSmelt) result.add(Synergy.ALCHEMIST_LOOP);
		return result;
	}

	public static float getSynergyAttackSpeedBonus(ItemStack stack) {
		float bonus = 0.0f;
		for (Synergy syn : getActiveSynergies(stack)) {
			if (syn == Synergy.VENOM_RUSH) bonus += 0.06f;
			if (syn == Synergy.BERSERKER_SCRIPT) bonus += 0.04f;
			if (syn == Synergy.RANGER_TEMPO) bonus += 0.03f;
			if (syn == Synergy.TRIUNE_FOCUS) bonus += 0.02f;
		}
		return bonus;
	}

	public static float getSynergyAttackDamageFlatBonus(ItemStack stack) {
		float bonus = 0.0f;
		for (Synergy syn : getActiveSynergies(stack)) {
			if (syn == Synergy.COLD_STEEL) bonus += 0.5f;
			if (syn == Synergy.IRON_PLEDGE) bonus += 0.5f;
			if (syn == Synergy.TRIUNE_FOCUS) bonus += 0.5f;
		}
		return bonus;
	}

	public static float getSynergyMiningSpeedBonus(ItemStack stack) {
		float bonus = 0.0f;
		for (Synergy syn : getActiveSynergies(stack)) {
			if (syn == Synergy.FIELD_COOK)    bonus += 0.04f;
			if (syn == Synergy.KEEPERS_REACH) bonus += 0.04f;
			if (syn == Synergy.DEEP_FLOW)     bonus += 0.05f;
			if (syn == Synergy.FROZEN_TEMPO)  bonus += 0.04f;
			if (syn == Synergy.MOONCRAFT)     bonus += 0.03f;
			if (syn == Synergy.NIGHTFALL_ENGINE) bonus += 0.03f;
		}
		return bonus;
	}

	public static float getSynergyXpBonus(ItemStack stack) {
		float bonus = 0.0f;
		for (Synergy syn : getActiveSynergies(stack)) {
			if (syn == Synergy.SILK_AND_SOUL) bonus += 0.08f;
			if (syn == Synergy.ALCHEMIST_LOOP) bonus += 0.05f;
		}
		return bonus;
	}
}
