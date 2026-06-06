# Tiers and Leveling

## Level and tier rules

- Tier width: `10` levels.
- Tier boundary levels: `10, 20, 30, ..., 90`.
- Boundary levels require upgrade and stop XP progression until upgraded.
- Standard tools: max `100` levels, tier `9`.
- Bow: max `50` levels, tier `4`.

## XP formula

`XP_to_next = 10 + (level * 5)`

At levels ending in `9` (for example 9, 19, 29), XP is tripled:

`XP_to_next = (10 + level * 5) * 3`

Examples:

- Level 0 -> 1: `10`
- Level 8 -> 9: `50`
- Level 9 -> 10: `165` (tripled)
- Level 49 -> 50: `765` (tripled)

## Tier mapping

- Tier 0: levels `0-9`
- Tier 1: levels `10-19`
- Tier 2: levels `20-29`
- Tier 3: levels `30-39`
- Tier 4: levels `40-49`
- Tier 5: levels `50-59`
- Tier 6: levels `60-69`
- Tier 7: levels `70-79`
- Tier 8: levels `80-89`
- Tier 9: levels `90-100`

## Pickaxe/Hammer mining speed multiplier

Formula:

`speed = 0.5 + cumulative_tier_bonus + in_tier_bonus`

In-tier coefficient by tier:

- T0: `0.13 * levelInTier`
- T1: `0.18 * levelInTier`
- T2: `0.25 * levelInTier`
- T3: `0.30 * levelInTier`
- T4: `0.45 * levelInTier`
- T5: `0.60 * levelInTier`
- T6: `1.00 * levelInTier`
- T7: `1.50 * levelInTier`
- T8: `2.20 * levelInTier`
- T9: `2.50 * levelInTier`

Cumulative bonus entering each tier:

- T0: `0.0`
- T1: `1.3`
- T2: `3.1`
- T3: `5.6`
- T4: `8.6`
- T5: `13.1`
- T6: `19.1`
- T7: `29.1`
- T8: `44.1`
- T9: `66.1`

Practical ranges by tier:

- T0: `0.50x` to `1.67x`
- T1: `1.80x` to `3.42x`
- T2: `3.60x` to `5.85x`
- T3: `6.10x` to `8.30x`
- T4: `9.10x` to `13.15x`
- T5: `13.60x` to `19.00x`
- T6: `19.60x` to `28.60x`
- T7: `29.60x` to `43.10x`
- T8: `44.60x` to `63.90x`
- T9: `66.60x` to `91.60x` (level 100 uses `levelInTier = 10`)

## Axe/Shovel/Hoe mining speed multiplier

For matching tool tags only:

`speed = 0.5 + tier + (levelInTier * 0.2)`

- Minimum at tier 0 level 0: `0.50x`
- Maximum at tier 9 level 99: `11.30x`
- Tier 9 with matching tool tag: forced one-shot (`break speed = 10000`)
