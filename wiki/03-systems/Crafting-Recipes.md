# Crafting Recipes

This page lists exact recipe data currently present in `data/tools_of_the_gods/recipe`.

## Core progression recipes

### Compressed Cobble

- Pattern:
  - `aaa`
  - `aaa`
  - `aaa`
- Key: `a = minecraft:cobblestone`
- Result: `tools_of_the_gods:compressedcobble`

### Crude Stone Pickaxe

- Pattern:
  - `aaa`
  - `aba`
  - `aaa`
- Key:
  - `a = tools_of_the_gods:compressedcobble`
  - `b = tools_of_the_gods:completed_primal_wooden_pickaxe`
- Result: `tools_of_the_gods:crude_stonepickaxe`

## Primal tools recipes

### Primal Pickaxe

- Pattern: `aba /  c  /  c `
- Key: `a=planks tag`, `b=wooden_pickaxe`, `c=stick`

### Primal Hammer

- Pattern: `aaa /  b  /  b `
- Key: `a=planks tag`, `b=stick`

### Primal Axe

- Pattern: `ab / ac /  c`
- Key: `a=planks tag`, `b=wooden_axe`, `c=stick`

### Primal Shovel

- Pattern: `a / b / b`
- Key: `a=wooden_shovel`, `b=stick`

### Primal Hoe

- Pattern: `ab /  c /  c`
- Key: `a=planks tag`, `b=wooden_hoe`, `c=stick`

### Primal Sword

- Pattern: `a / b / c`
- Key: `a=wooden_sword`, `b=planks tag`, `c=stick`

### Primal Bow

- Pattern: `ab  / acd / ab `
- Key: `a=stick`, `b=string`, `c=bow`, `d=planks tag`

## Gem recipes

- White Gem: `iron_ingot + iron_block + flint_and_steel`
- Yellow Gem: `clock + sand + enchanted_golden_apple`
- Purple Gem: `glass + experience_bottle + enchanting_table`
- Red Gem: `tnt + redstone_block + fire_charge`
- Black Gem: `obsidian + lava_bucket + fermented_spider_eye`
- Green Gem: `emerald_block + blaze_powder + ender_eye`
- Blue Gem: `diamond_block + prismarine_crystals + ender_eye`
- Universe Gem:
  - Pattern: `aba / bcb / ada`
  - Key: `a=nether_star`, `b=end_crystal`, `c=dragon_breath`, `d=blue_gem`

## Ultimate fusion recipe

- Pattern:
  - `pas`
  - `huc`
  - `bq `
- Key:
  - `p = pickaxe_of_the_gods`
  - `a = axe_of_the_gods`
  - `s = sword_of_the_gods`
  - `h = hammer_of_the_gods`
  - `u = universe_gem`
  - `c = shovel_of_the_gods`
  - `b = bow_of_the_gods`
  - `q = hoe_of_the_gods`
- Result: `ultimate_tool_of_the_gods`
