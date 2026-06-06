# Ultimate Tool of the Gods

The endgame relic that fuses the core god-tools and the Universe Gem.

## Crafting

Recipe output: `tools_of_the_gods:ultimate_tool_of_the_gods`

Required ingredients:

- Pickaxe of the Gods
- Hammer of the Gods
- Axe of the Gods
- Shovel of the Gods
- Hoe of the Gods
- Sword of the Gods
- Bow of the Gods
- Universe Gem

## Base item stats

- Tier speed stat: `18.0`
- Tier attack damage bonus: `9.0`
- Digger attributes: attack damage `6.0`, attack speed `-2.6`
- Enchantability: `30`
- Rarity: `EPIC`
- Durability: unlimited (`MAX_DAMAGE` removed)
- **Trait slots:** 8 base (+ Modifiable I)

---

## Tool mode wheel

Hold the **Ultimate Tool Mode Wheel** key (default: **G**) while holding the Ultimate Tool.

- A **radial menu** appears with icons from each TOG tool type.
- Move the mouse to highlight a mode; **release G** or **click** to confirm.
- Rebind under **Controls → Tools of the Gods**.

### Tool modes

| Mode | Role |
|---|---|
| Pickaxe | Stone, ores, pickaxe blocks |
| Hammer | Large-area mining (Broad Touch) |
| Axe | Logs, axe blocks |
| Shovel | Dirt, sand, gravel |
| Hoe | Hoe-tagged blocks |
| Sword | Melee combat (+ bonus damage in sword mode) |
| Bow | Ranged (trait synergy) |
| Crossbow | Bolts, Marksman traits |
| Trident | Thrown + melee |
| Staff | Right-click divine bolt |
| Fishing Rod | Right-click cast line near water |

Wrong mode for a block type mines slowly until you switch.

### Adaptive I (Ultimate-only trait)

- **Ingredient:** Compass (2 slots)
- **Effect:** Automatically picks the best tool mode from context:
  - Entity in crosshair → Sword (Bow if far)
  - Shovel / axe / hoe / pickaxe blocks → matching mode
  - Water → Fishing Rod
- Manual wheel selection **locks** auto-switch for **8 seconds**.

---

## Power modes

**Shift + Right-Click** cycles power modes (after trait toggle checks):

1. **Balanced**
2. **Precision**
3. **Overdrive**

| Power mode | Tool-tagged destroy speed | Other blocks | Bonus on-hit damage |
|---|---:|---:|---:|
| Balanced | 320.0 | 64.0 | +2.0 |
| Sword mode multiplies melee bonus in sword tool mode |
| Precision | 8.0 | 3.0 | +6.0 |
| Overdrive | 10000.0 | 10000.0 | +0.5 |

### Trait toggles (Shift + Right-Click, checked first)

- **Silky II** — toggle Silk Touch
- **Autosmelt II** — toggle Autosmelt
- **Broad Touch II** — cycle area mode (`3x3` / `5x5` / `7x7` / `9x9`)

---

## Traits on Ultimate

The Ultimate Tool can bind **almost every trait** in the mod (8+ slots). See [Traits](Traits) and [Trait Compendium](Trait-Compendium).

Ultimate-only:

| Trait | Ingredient |
|---|---|
| Adaptive I | Compass |

---

## Harvest capability

Valid for drops on blocks tagged:

- `mineable/pickaxe`, `mineable/axe`, `mineable/shovel`, `mineable/hoe`

When the active **tool mode** matches the block tag, full power-mode mining speed applies.
