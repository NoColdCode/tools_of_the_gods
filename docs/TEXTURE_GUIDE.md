# Tools of the Gods — Texture File Guide

All paths are relative to:

`src/main/resources/assets/tools_of_the_gods/textures/`

**Important (Minecraft 1.21+):** every filename must be **all lowercase** (e.g. `ihelm.png`, not `IHelm.png`).

---

## Armor — worn on the player (one pair per tier)

**Folder:** `textures/models/armor/` — **Size:** 64×32 (vanilla armor UV)

| Tier | Prefix | Layer 1 (helm / chest / boots) | Layer 2 (leggings) |
|------|--------|--------------------------------|--------------------|
| 0 | **h** | `h_layer_1.png` | `h_layer_2.png` |
| 1 | **c** | `c_layer_1.png` | `c_layer_2.png` |
| 2 | **i** | `i_layer_1.png` | `i_layer_2.png` |
| 3 | **g** | `g_layer_1.png` | `g_layer_2.png` |
| 4 | **a** | `a_layer_1.png` | `a_layer_2.png` |
| 5 | **u** | `u_layer_1.png` | `u_layer_2.png` |
| 6 | **o** | `o_layer_1.png` | `o_layer_2.png` |
| 7 | **e** | `e_layer_1.png` | `e_layer_2.png` |
| 8 | **d** | `d_layer_1.png` | `d_layer_2.png` |
| 9 | **og** | `og_layer_1.png` | `og_layer_2.png` |

Generate transparent blanks:

```bash
python scripts/generate_armor_layer_blanks.py
```

**Recommended — auto-generate from vanilla armor + trim (desaturate + material hue):**

```bash
python scripts/generate_armor_layers_from_vanilla.py --force
```

Uses leather/chain/iron/gold/diamond/netherite bases, a different trim pattern per tier, then tints to the material color in `TIER_SPECS` inside the script.

Clone your current `motion_of_the_gods_layer_*.png` into every tier (manual hue-shift starting point):

```bash
python scripts/generate_armor_layer_blanks.py --clone-reference --force
```

---

## Armor — inventory icons (10 tiers × 4 pieces)

**Armor progression** (leather → chain → iron → gold, then gem ascension — not wood/stone tool tiers):

| Tier | Levels | Material name (in-game) | Prefix | Helmet | Chest | Leggings | Boots |
|------|--------|-------------------------|--------|--------|-------|----------|-------|
| 0 | 0–9 | Worn Leather | **h** | `hhelm.png` | `hchest.png` | `hleg.png` | `hboot.png` |
| 1 | 10–19 | Rusty Chainmail | **c** | `chelm.png` | `cchest.png` | `cleg.png` | `cboot.png` |
| 2 | 20–29 | Forged Iron | **i** | `ihelm.png` | `ichest.png` | `ileg.png` | `iboot.png` |
| 3 | 30–39 | Gilded Leather | **g** | `ghelm.png` | `gchest.png` | `gleg.png` | `gboot.png` |
| 4 | 40–49 | Amethyst-Touched | **a** | `ahelm.png` | `achest.png` | `aleg.png` | `aboot.png` |
| 5 | 50–59 | Ruby-Forged | **u** | `uhelm.png` | `uchest.png` | `uleg.png` | `uboot.png` |
| 6 | 60–69 | Obsidian Runed | **o** | `ohelm.png` | `ochest.png` | `oleg.png` | `oboot.png` |
| 7 | 70–79 | Arcane Emerald | **e** | `ehelm.png` | `echest.png` | `eleg.png` | `eboot.png` |
| 8 | 80–89 | Ethereal Diamond | **d** | `dhelm.png` | `dchest.png` | `dleg.png` | `dboot.png` |
| 9 | 90–100 | Divine (of the Gods) | **og** | `helmog.png` | `chestog.png` | `leggog.png` | `bootog.png` |

**Folder:** `textures/item/` — **Size:** 32×32

---

## Regenerate / migrate

```bash
python scripts/migrate_armor_texture_names.py   # remap armor PNG prefixes (once)
python scripts/generate_armor_models.py         # refresh model JSON
python scripts/generate_armor_layers_from_vanilla.py --force  # worn layers from MC assets
python scripts/generate_texture_placeholders.py # missing item icon PNGs only
```

---

## Tools (unchanged)

Tools still use wood/stone/iron naming (`wswo`, `wham`, `item21`, etc.) — see existing files in `textures/item/`.
