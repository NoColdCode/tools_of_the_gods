# Upgrade Materials and Gems

## Tier upgrade requirements

Use `Shift + Right-Click` with the tool to attempt upgrade.

| Upgrade to tier | Requirement |
|---:|---|
| 1 | 32 Cobblestone |
| 2 | 1 White Gem |
| 3 | 1 Yellow Gem |
| 4 | 1 Purple Gem |
| 5 | 1 Red Gem |
| 6 | 1 Black Gem |
| 7 | 1 Green Gem |
| 8 | 1 Blue Gem |
| 9 | 1 Universe Gem |

## Gem crafting recipes

All gem recipes are shaped 3x3 with pattern `aba / bcb / aba` unless noted.

- White Gem: `a=iron_ingot`, `b=iron_block`, `c=flint_and_steel`
- Yellow Gem: `a=clock`, `b=sand`, `c=enchanted_golden_apple`
- Purple Gem: `a=glass`, `b=experience_bottle`, `c=enchanting_table`
- Red Gem: `a=tnt`, `b=redstone_block`, `c=fire_charge`
- Black Gem: `a=obsidian`, `b=lava_bucket`, `c=fermented_spider_eye`
- Green Gem: `a=emerald_block`, `b=blaze_powder`, `c=ender_eye`
- Blue Gem: `a=diamond_block`, `b=prismarine_crystals`, `c=ender_eye`
- Universe Gem: pattern `aba / bcb / ada`
  - `a=nether_star`, `b=end_crystal`, `c=dragon_breath`, `d=blue_gem`

## Gem and trait-remover drop rates

From server event handlers:

- Monster kill with TOG tool in hand:
  - Gem: `1/220` (`0.4545%`)
  - Trait Purge Core: `1/1800` (`0.0556%`)
- Breaking pickaxe-mineable blocks with TOG tool:
  - Gem: `1/1800` (`0.0556%`)
  - Trait Purge Core: `1/6000` (`0.0167%`)

Gem tier from random drop:

- Minimum dropped gem tier is 3 (Yellow Gem)
- Mob drops use `max(3, heldTier + 1)` as upper bound
- Block drops use `max(3, heldTier)` as upper bound
