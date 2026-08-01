# Tools of the Gods — 1.4.1

**Minecraft 1.21.1 · NeoForge + Fabric (Fabric scaffold)**

---

## Multi-project layout

- **`common/`** — shared Java, textures, models, recipes, lang, Patchouli, default configs (`mod_id`: `tools_of_the_gods`)
  - **`common/logic/`** — loader-neutral gameplay logic (traits, progression, powers, commands)
- **`neoforge/1.21.1/`** — NeoForge 1.21.1 glue (`NeoForgeBootstrap`, event/command subscribers), `META-INF/neoforge.mods.toml`, dev runs, release jar
- **`fabric/<version>/`** — Fabric glue per Minecraft version (shared Java glue in `fabric/1.21.1/src/main/java`, version configs in `gradle/fabric-versions/`)

Build NeoForge: `gradlew :neoforge-1.21.1:build` → `neoforge/1.21.1/build/libs/tools_of_the_gods-*.jar`

Build Fabric (example): `gradlew :fabric-1.21.1:build` → `fabric/1.21.1/build/libs/tools_of_the_gods-fabric-1.21.1-*.jar`

Fabric modules: **1.16.5**, **1.18.2**, **1.19.2**, **1.20.1**, **1.21.1**, **1.21.8**, **26.1** (`:fabric-<version>`). Shared `gradle/fabric-project.gradle` merges `:common` + glue. **`common/` currently targets 1.21.1 APIs** — only **1.21.1** and **1.21.8** are expected to compile until version-specific `common` backports exist for older releases.

Future loaders/versions add sibling folders that embed `:common` and register the same `logic` classes behind their own event glue.

**Phase 1 refactor:** event handlers, pickaxe power tick glue, and `/tog` command registration live in `neoforge/1.21.1`; implementations live in `common/logic/*Logic.java`.

**Phase 2 refactor:** `common/logic` uses loader-neutral `logic/context/*` types (`TogBlockDropsContext`, `TogIncomingDamageContext`, `TogShieldBlockContext`, etc.). NeoForge maps events via `NeoForgeEventAdapters` in the glue module — no NeoForge imports remain under `common/logic/`.

**Phase 3 refactor (Fabric scaffold):**
- Loader-neutral `TogRegistryEntry` holders in `common/init/`; registration moved to `NeoForgeModContent` and `FabricModContent`
- `TogModConstants` (MODID, version, logger) shared across loaders
- `fabric/1.21.1/` with Loom — registers all content, wires `FabricGameplayEvents` + mixins to the same `common/logic/*` classes
- NeoForge-only pieces moved to glue: `NeoForgeIngredientTypes`, `NeoForgeTogItemComponents`, `NeoForgeModTabsHandler`, armor/wings `IItemExtension` bridges
- **Not yet on Fabric:** client UI (screens, keybinds), NeoForge networking payloads, `max_progression_tool` custom ingredient, block-drop mixin (Silky/Broad Touch/etc. need a follow-up mixin), full runtime QA

**Phase 3 smoke tests:**
- Fabric compiles shared `common` sources with official Mojang mappings (NeoForge-compiled classes are not embedded at runtime)
- Loader stubs: Fabric config defaults, `GuideBookOpener`, `TogEntityInventoryHelper`, `FabricPlayerAttachments` for persistent player NBT
- `TogPlayerData` + `TogPlatform.getPersistentData()` for cross-loader player data
**Phase 3 multi-version Fabric:**
- Fabric modules: `fabric/1.16.5`, `1.18.2`, `1.19.2`, `1.20.1`, `1.21.1`, `1.21.8`, `26.1` — shared glue in `fabric/1.21.1/src/main/java`, per-version config in `gradle/fabric-versions/`, shared build logic in `gradle/fabric-project.gradle`
- Enable modules with `-PfabricVersions=1.21.1` (default). Example: `-PfabricVersions=1.21.8` or `-PfabricVersions=1.16.5,1.18.2,...`
- **Only `:fabric-1.21.1:build` is fully wired today** (`merge_common_sources=true`). Other versions are scaffolded (`merge_common_sources=false`) until version-specific `common` backports exist — `common/` targets 1.21.1 APIs (e.g. 1.21.8 renamed/moved `ArmorItem`, `ItemProperties`, etc.)
- Do not enable two Fabric modules that require different Loom versions in one Gradle invocation (e.g. 1.21.1 uses Loom 1.9.2, 1.21.8 uses Loom 1.11.8)

---

## Dedicated server fix

- Fixed crash on dedicated servers: `Attempted to load class Screen for invalid dist DEDICATED_SERVER`
- Moved client-only code (guide book screen, mob effect GUI, config screen, dynamic textures) behind client-only subscribers
- Mod jar remains **one file** for both client and dedicated server — install the same jar on both sides

---

# Tools of the Gods — 1.4.0

**Minecraft 1.21.1 · NeoForge**

---

## Shield of the Gods

Full rebalance — stats now scale **linearly with level only** (tier affects visuals and gem gates, not core formulas). Removed the old early-tier buff that made high-tier shields weaker than lower tiers at the same level.

| Stat | Level 0 / 1 | Level 50 | Level 100 |
|------|-------------|----------|-----------|
| **Block** | 50% | 74.5% | 99% |
| **Power** | 60% (from Lv1) | ~80% | 100% |
| **Strain cap** | 4 | 27 | 50 |
| **Recovery** | 6s | 4.5s | 3s |
| **Return** | 0% | 1% | 15% |

- **Strain** replaces overheat: blocking builds strain from negated damage; exceeding capacity disables blocking until recovery ends
- Rapid raise/lower spam adds a strain penalty instead of resetting
- Guide book updated with full shield scaling pages (Patchouli + in-game guide)

## Enchanting

- **Bow of the Gods** — full vanilla bow enchantments at the enchanting table (Power, Flame, Infinity, Unbreaking, Mending, etc.)
- **Shield of the Gods** — intentionally **not** enchantable (progression comes from levels and traits)

> Re-craft existing bows/shields if enchant behaviour does not update on old items.

## Bug fixes & polish

- Fixed gem-upgrade chat spam when using tools at a tier boundary (e.g. level 50/60) without upgrade materials
- Fixed level-0 block chance reading 30% from stale config — now hardcoded at **50%**
- Shift + right-click upgrade no longer conflicts with sneaking while using the bow or shield

---

# Tools of the Gods — 1.3.0

**Minecraft 1.21.1 · NeoForge**

---

## Expanded content

- **Armor of the Gods** — full four-piece set with tier visuals, fractional defense scaling, and set bonus (Absorption + configurable damage reduction)
- **Shield of the Gods** — block chance scales with level and tier; Guardian and Repulse traits
- **Wings of the Gods** — chest-slot elytra-style flight with wing-specific traits
- **Fishing Rod, Crossbow, Trident, and Staff of the Gods** — each with its own XP loop and trait pool
- **Spear and Flail of the Gods** — melee weapons with full level/tier progression
- **Ultimate Tool of the Gods** — endgame fusion tool with mode wheel and Adaptive trait

## Traits

Original tool traits remain (Speedy, Silky, Autosmelt, Broad Touch, Frenzy, and others). New trait bindings for expanded gear:

| Category | Traits |
|---|---|
| Armor | Thorns, Fireward, Bulwark, Swiftstep, Purifying |
| Shield | Guardian, Repulse |
| Fishing rod | Angler, Reel |
| Crossbow | Marksman, Quick Load |
| Trident | Impaler, Returning, Riptide |
| Staff | Arcane, Channeling |
| Wings | Aerodynamic, Featherfall |
| Ultimate | Adaptive |

**Trait Smithing Table** — slot previews now show correct stat values; Thorns, Fireward, Guardian, and Repulse display properly instead of blank or “+0%” placeholders.

## Enchanting

All TOG tools, armor, shield, bow, and expanded gear can be enchanted at the enchanting table and anvil.

- Gear remains unbreakable (no durability loss)
- Enchantments persist through tier upgrades
- Armor and tools registered with vanilla enchant tags for proper table compatibility

> Items crafted before 1.3.0 may need to be re-crafted to gain enchantability.

## In-game guide (Patchouli)

- Guide book granted on first join; right-click to reopen anytime
- Consolidated **Core Tools** entry with all seven recipes
- Full **Gem Progression** page with all eight gems and crafting layouts
- Trait entries unlocked by default
- **Armor** section: overview, defense scaling, tiers and upgrades

## Gameplay polish

- **Night Vision** (pickaxe tier 6+) refreshes before expiring; works from held tool or worn armor
- **Sustaining** works on worn armor at a slower rate than when held
- Swiftstep, Bulwark, and Fireward refresh their effects before they drop off

## Bug fixes

- Fixed Green Gem recipe failing to load (ender pearl missing from crafting pattern)
- Fixed invalid entries in vanilla pickaxe tag breaking enchantment compatibility
- Added TOG items to armor and tool enchant tags
- Fixed Universe Gem recipe duplicate key
