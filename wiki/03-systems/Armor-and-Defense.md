# Armor and Defense

TOG defense gear uses **custom divine materials**, not vanilla iron, leather, or wood. Armor shares the same progression system as tools (levels, tiers, traits).

## Armor of the Gods (full set)

| Piece | Slot | Material | Repair |
|---|---|---|---|
| Helmet of the Gods | Head | `motion_of_the_gods` (Divine) | Universe Gem |
| Chestplate of the Gods | Chest | Divine | Universe Gem |
| Leggings of the Gods | Legs | Divine | Universe Gem |
| Boots of the Gods | Feet | Divine | Universe Gem |

### Divine armor stats

Comparable to high-tier netherite-style protection:

| Stat | Value |
|---|---:|
| Helmet defense | 4 |
| Chestplate defense | 9 |
| Leggings defense | 7 |
| Boots defense | 4 |
| Toughness | 3.0 |
| Knockback resistance | 12% |
| Enchantability | 25 |
| Durability | Unlimited (`MAX_DAMAGE` removed) |
| Equip sound | Netherite-style |

### Set bonus

Wearing **all four** Armor of the Gods pieces (chest must be armor, not wings):

- Periodic **Absorption I**
- Extra **damage reduction** (config: `armorSetBonusReduction`)

### Armor traits

Armor can bind **defensive traits** at the Trait Smithing Table:

| Trait | Ingredient | Effect |
|---|---|---|
| Thorns I / II | Cactus → upgrade | Reflect damage to melee attackers |
| Fireward I / II | Magma Cream → Blaze Rod | Fire Resistance while worn |
| Scholar, Soulbound, Modifiable, Sustaining | (same as tools) | Utility traits |
| Bulwark, Swiftstep, Purifying | Brick, Feather, Glowstone | Passive buffs while worn |

**Not allowed on armor:** mining traits (Momentum, Silky, Broad Touch, etc.), Ranger, on-hit combat traits (Poison, Sharpy on armor slot).

### Progression

- **Type:** `ARMOR`
- **Max level:** 100 (tier 9)
- **XP:** gained slowly while wearing pieces and when taking damage
- **Upgrade:** Shift + Right-Click with tier materials/gems (same flow as tools)

---

## Shield of the Gods

- **Slot:** Off-hand (or active block)
- **Type:** `SHIELD`
- **XP:** on successful blocks
- **Mechanics:** block chance and block power scale with level; low levels can fail blocks

### Shield-only traits

| Trait | Ingredient |
|---|---|
| Guardian I / II | Iron Block → Obsidian |
| Repulse I | Piston |

Also supports Scholar, Soulbound, Sustaining, Bulwark, Swiftstep, Purifying where applicable.

---

## Wings of the Gods

- **Slot:** Chest (replaces chestplate visually for armor layer — uses **Aerial** material with **no armor overlay**)
- **Type:** `WINGS`
- **Flight:** Elytra-style glide when worn in chest slot
- **XP:** passive glide XP (boosted by Aerodynamic trait)
- **Material:** `aerial` — 0 armor points, elytra equip sound, phantom membrane repair
- **Not iron chestplate** — no vanilla metal wings texture

### Wings traits

| Trait | Ingredient |
|---|---|
| Aerodynamic I / II | Ghast Tear → Elytra |
| Featherfall I | Turtle Scute |
| Scholar, Soulbound, Modifiable, Sustaining, Bulwark, Swiftstep, Purifying | (utility) |

---

## Worn textures (resource pack)

Divine armor expects custom layer textures:

- `assets/tools_of_the_gods/textures/models/armor/motion_of_the_gods_layer_1.png`
- `assets/tools_of_the_gods/textures/models/armor/motion_of_the_gods_layer_2.png` (optional second layer)

Inventory icons use the **Universe Gem** texture until per-piece art is added.

Wings use the **Airborne** upgrade icon in inventory; no chestplate layer is rendered on the player model.
