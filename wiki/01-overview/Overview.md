# Overview

Tools of the Gods is a progression mod centered around evolving tools, armor, and relics forged from divine materials — **not** vanilla wood or iron tiers.

## Core progression loop

1. Craft a TOG item (tool, armor piece, shield, or expanded gear).
2. Gain XP by using it.
3. Every 10 levels, perform a tier upgrade with materials and gems.
4. Unlock stronger mining, combat, powers, and traits at the Trait Smithing Table.
5. Fuse the core god-tools into the **Ultimate Tool of the Gods**.

## Tool roster

### Core tools (primal → god tier)

| Item | Notes |
|---|---|
| Pickaxe of the Gods | Main mining progression |
| Hammer of the Gods | 3×3×1 mining, larger at high tier |
| Axe of the Gods | Tree felling |
| Shovel of the Gods | Digging |
| Hoe of the Gods | Farming |
| Sword of the Gods | Combat |
| Bow of the Gods | 5-tier bow progression (max level 50) |

### Defense

| Item | Notes |
|---|---|
| Armor of the Gods (4 pieces) | **Divine** material, set bonus, defensive traits |
| Shield of the Gods | Block scaling, Guardian / Repulse traits |
| Wings of the Gods | Elytra flight, **Aerial** material (not iron chestplate) |

### Expanded gear

| Item | Notes |
|---|---|
| Fishing Rod of the Gods | Angler / Reel traits |
| Crossbow of the Gods | Marksman / Quick Load traits |
| Trident of the Gods | Impaler / Returning / Riptide traits |
| Staff of the Gods | Arcane / Channeling traits |

### Endgame

| Item | Notes |
|---|---|
| Ultimate Tool of the Gods | All core tools + mode wheel + Adaptive trait |

## Materials philosophy

- **Tools** use custom textures and tier models (`tools_of_the_gods:tier` predicate).
- **Armor** uses the registered **`motion_of_the_gods`** divine armor material (netherite-tier stats, Universe Gem repair) — not `ArmorMaterials.IRON`.
- **Wings** use **`aerial`** material (zero armor overlay, elytra flight only) — not iron chestplate visuals.

## Progression endpoints

| Category | Max level | Max tier |
|---|---:|---:|
| Most tools & armor | 100 | 9 ("… of the Gods") |
| Bow | 50 | 4 |
| Ultimate Tool | 100 | — (8 trait slots) |

## See also

- [Armor and Defense](../03-systems/Armor-and-Defense)
- [Expanded Tools](../03-systems/Expanded-Tools)
- [Ultimate Tool](../03-systems/Ultimate-Tool)
- [Traits](../03-systems/Traits)
