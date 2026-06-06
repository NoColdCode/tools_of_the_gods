# Traits

Traits are stored in item custom data (`togTraits`) and are applied through the Trait Smithing Table.

For grouped rank chains and a full cross-trait table, see [Trait Compendium](Trait-Compendium).

## Trait slots

Base slot count by tier:

| Tier | Slots |
|---:|---:|
| 0 | 0 |
| 1 | 1 |
| 2 | 1 |
| 3 | 2 |
| 4 | 2 |
| 5 | 3 |
| 6 | 4 |
| 7 | 4 |
| 8 | 5 |
| 9 | 6 |

Additional slot rules:

- `Modifiable I` adds `+1` slot.
- `Ultimate Tool` has a fixed base of `8` trait slots (plus `Modifiable I` if present).

## Trait ingredients and upgrade paths

| Ingredient | Trait chain |
|---|---|
| `Redstone Block` | `Speedy I -> Speedy II -> Speedy III` |
| `Fermented Spider Eye` | `Poison I -> Poison II` |
| `Bread` | `Sustaining I -> Sustaining II -> Sustaining III` |
| `Magma Block` | `Autosmelt -> Autosmelt II` |
| `Nether Star` | `Soulbound` |
| `Iron Ingot` | `Sharpy I -> Sharpy II -> Sharpy III` |
| `Diamond` | `Modifiable I` |
| `Copper Ingot` | `Magnetic I` |
| `String` | `Silky I -> Silky II` |
| `Ice` | `Freezy I -> Freezy II` |
| `Sugar` | `Momentum I -> Momentum II` |
| `Slime Ball` | `Broad Touch I -> Broad Touch II` |
| `Blaze Powder` | `Frenzy I -> Frenzy II -> Frenzy III` |
| `Anvil` | `Titan I -> Titan II` |
| `Lapis Lazuli` | `Scholar I -> Scholar II` |
| `Phantom Membrane` | `Moonlit I -> Moonlit II` |
| `Flint` | `Ranger I -> Ranger II` |
| `Emerald` | `Bountiful I` |
| `Hopper` | `Scavenger I` |
| `Glowstone Dust` | `Purifying I` |
| `Feather` | `Swiftstep I` |
| `Brick` | `Bulwark I` |
| `Cactus` | `Thorns I -> Thorns II` (armor) |
| `Magma Cream` | `Fireward I -> Fireward II` (armor) |
| `Blaze Rod` | `Fireward II` upgrade (armor) |
| `Iron Block` | `Guardian I` (shield) |
| `Obsidian` | `Guardian II` (shield) |
| `Piston` | `Repulse I` (shield) |
| `Nautilus Shell` | `Angler I` (fishing rod) |
| `Heart of the Sea` | `Angler II` (fishing rod) |
| `Cod` | `Reel I` (fishing rod) |
| `Spectral Arrow` | `Marksman I` (crossbow) |
| `Arrow` | `Marksman II` (crossbow) |
| `Firework Rocket` | `Quick Load I` (crossbow) |
| `Ender Pearl` | `Returning I` (trident) |
| `Prismarine Shard` | `Riptide I` (trident) |
| `Prismarine Crystals` | `Impaler I` (trident) |
| `Netherite Ingot` | `Impaler II` (trident) |
| `Amethyst Shard` | `Arcane I` (staff) |
| `Dragon's Breath` | `Arcane II` (staff) |
| `Lightning Rod` | `Channeling I` (staff) |
| `Ghast Tear` | `Aerodynamic I` (wings) |
| `Elytra` | `Aerodynamic II` (wings) |
| `Turtle Scute` | `Featherfall I` (wings) |
| `Compass` | `Adaptive I` (ultimate only) |

## Trait list and effects

| Trait | Slots | Effect |
|---|---:|---|
| `Speedy I` | 1 | `+20%` attack speed, `+15%` mining speed, `-10%` attack damage, `-10%` XP |
| `Speedy II` | 2 | `+40%` attack speed, `+30%` mining speed, `-15%` attack damage, `-15%` XP |
| `Speedy III` | 3 | `+65%` attack speed, `+50%` mining speed, `-20%` attack damage, `-20%` XP |
| `Poison I` | 1 | Applies Poison I for `5s` on hit, `-20%` attack speed |
| `Poison II` | 2 | Applies Poison I for `10s` on hit, `-30%` attack speed |
| `Sustaining I` | 1 | `+1` food every `30s`, `+1` saturation every `20s`, loot `-20%` |
| `Sustaining II` | 2 | `+1` food every `20s`, `+2` saturation every `15s`, loot `-35%` |
| `Sustaining III` | 3 | `+2` food every `30s`, `+2` saturation every `10s`, loot `-50%` |
| `Autosmelt` | 1 | Smelts broken blocks and compatible drops |
| `Autosmelt II` | 1 | Same as Autosmelt, with toggle support |
| `Soulbound` | 1 | Tool is kept on death |
| `Silky I` | 1 | Always mines with Silk Touch |
| `Silky II` | 1 | Silk Touch is toggleable |
| `Sharpy I` | 1 | `+2` flat damage, `-30%` attack speed, `-30%` mining speed |
| `Sharpy II` | 2 | `+4` flat damage, `-50%` attack speed, `-50%` mining speed |
| `Sharpy III` | 3 | `+6` flat damage, `-80%` attack speed, `-80%` mining speed |
| `Modifiable I` | 0 | `+1` trait slot |
| `Magnetic I` | 1 | Attracts items in a small radius (about 5 blocks) |
| `Freezy I` | 1 | Applies Slowness I for `10s` on melee/arrow hit, `-0.5` flat damage |
| `Freezy II` | 2 | Applies Slowness II for `15s` on melee/arrow hit, `-1.0` flat damage |
| `Momentum I` | 1 | Up to `+25%` mining speed after breaking `50` blocks; resets after idle |
| `Momentum II` | 2 | Up to `+40%` mining speed after breaking `40` blocks; resets after idle |
| `Broad Touch I` | 2 | Pickaxe/Shovel `3x3`, Hammer `5x5`, Axe tree-fell cap `12` logs |
| `Broad Touch II` | 4 | Mode-based larger areas, Axe tree-fell up to `64` logs |
| `Frenzy I` | 1 | `+10%` attack speed, `+6%` mining speed, `-4%` attack damage |
| `Frenzy II` | 2 | `+18%` attack speed, `+12%` mining speed, `-8%` attack damage |
| `Frenzy III` | 3 | `+28%` attack speed, `+18%` mining speed, `-12%` attack damage |
| `Titan I` | 1 | `+2` flat damage, `-15%` attack speed, `-10%` mining speed |
| `Titan II` | 2 | `+4` flat damage, `-30%` attack speed, `-20%` mining speed |
| `Scholar I` | 1 | `+10%` XP gain |
| `Scholar II` | 2 | `+20%` XP gain |
| `Moonlit I` | 1 | At night only: `+12%` mining speed |
| `Moonlit II` | 2 | At night only: `+20%` mining speed |
| `Ranger I` | 1 | Bow/Ultimate-focused trait, `+5%` XP gain |
| `Ranger II` | 2 | Bow/Ultimate-focused trait, `+10%` XP gain |
| `Bountiful I` | 1 | Mechanic-only: each block-drop stack has `8%` chance to duplicate |
| `Scavenger I` | 1 | Mechanic-only: auto-collects block/mob drops into inventory |
| `Purifying I` | 1 | Mechanic-only: every `8s`, removes one negative effect |
| `Swiftstep I` | 1 | Mechanic-only: grants Speed I while held |
| `Bulwark I` | 1 | Mechanic-only: grants Resistance I while held |
| `Thorns I` | 1 | Armor: reflects 2 damage to melee attackers |
| `Thorns II` | 2 | Armor: reflects 5 damage |
| `Fireward I` | 1 | Armor: Fire Resistance I while worn |
| `Fireward II` | 2 | Armor: Fire Resistance II while worn |
| `Guardian I` | 1 | Shield: +12% block chance, +10% block power |
| `Guardian II` | 2 | Shield: +22% block chance, +18% block power |
| `Repulse I` | 1 | Shield: right-click knockback (not while sneaking) |
| `Angler I` | 1 | Fishing: better loot, +10% fishing XP |
| `Angler II` | 2 | Fishing: much better loot, +20% fishing XP |
| `Reel I` | 1 | Fishing: faster bites |
| `Marksman I` | 1 | Crossbow: +1 bolt damage, +5% XP |
| `Marksman II` | 2 | Crossbow: +2 bolt damage, +10% XP |
| `Quick Load I` | 1 | Crossbow: 25% faster reload |
| `Returning I` | 1 | Trident: returns faster |
| `Riptide I` | 1 | Trident: +50% thrown damage in rain/water |
| `Impaler I` | 1 | Trident: +2 damage |
| `Impaler II` | 2 | Trident: +4 damage |
| `Arcane I` | 1 | Staff: stronger bolt, 20% shorter cooldown |
| `Arcane II` | 2 | Staff: much stronger bolt, 35% shorter cooldown |
| `Channeling I` | 1 | Staff: lightning on bolt hits during storms |
| `Aerodynamic I` | 1 | Wings: faster glide, +50% glide XP |
| `Aerodynamic II` | 2 | Wings: much faster glide, +100% glide XP |
| `Featherfall I` | 1 | Wings: 50% less fall damage while worn |
| `Adaptive I` | 2 | Ultimate: auto-selects tool mode for context |

## Tool restrictions

Trait gating is enforced by tool type. **Ultimate Tool** can use almost all traits (see [Ultimate Tool](Ultimate-Tool)).

### Combat traits

- `Poison I/II`: Axe, Sword, Bow, Crossbow, Trident, Ultimate
- `Sharpy I/II/III`: Axe, Sword, Trident, Ultimate
- `Freezy I/II`: Axe, Sword, Bow, Crossbow, Trident, Ultimate
- `Titan I/II`: Axe, Sword, Trident, Ultimate
- `Frenzy I/II/III`: all TOG tools except Bow
- `Ranger I/II`: Bow, Crossbow, Ultimate

### Mining traits

- `Silky I/II`: all TOG tools except Bow
- `Momentum I/II`: Pickaxe, Hammer, Shovel, Axe, Ultimate
- `Broad Touch I/II`: Pickaxe, Hammer, Shovel, Axe, Ultimate
- `Moonlit I/II`: Pickaxe, Hammer, Shovel, Axe, Ultimate
- `Magnetic I`: most tools; also Fishing Rod

### Armor / shield exclusives

- `Thorns I/II`, `Fireward I/II`: **Armor only**
- `Guardian I/II`, `Repulse I`: **Shield only**

### Expanded tool exclusives

- `Angler`, `Reel`: **Fishing Rod**
- `Marksman`, `Quick Load`: **Crossbow**
- `Returning`, `Riptide`, `Impaler`: **Trident**
- `Arcane`, `Channeling`: **Staff**
- `Aerodynamic`, `Featherfall`: **Wings**

### Ultimate exclusive

- `Adaptive I`: **Ultimate Tool only**

### Utility traits (most items)

`Scholar`, `Soulbound`, `Modifiable`, `Sustaining`, `Bountiful`, `Scavenger`, `Purifying`, `Swiftstep`, `Bulwark` — allowed on tools, armor, shield, and wings per internal rules.

## Toggle behavior

Use `Shift + Right-Click` while holding a TOG tool:

- `Silky II`: toggles Silk Touch ON/OFF.
- `Autosmelt II`: toggles Autosmelt ON/OFF.
- `Broad Touch II`: cycles mode value used for area size/tree-fell cap.

Mode ranges:

- Axe: `Fell 12` / `Fell 64`
- Hammer and Ultimate: `3x3` / `5x5` / `7x7` / `9x9`
- Pickaxe and Shovel: `1x1` / `3x3` / `5x5`

## Momentum details

- Momentum counter increments on successful block drops.
- Counter hard-resets after `60` ticks (`3s`) of idle time.
- Bonus scales linearly from `0` to max based on current momentum blocks.

## Synergies

Synergies activate automatically when all required traits are present, and are shown in tooltip under `Synergies`.

| Synergy | Required traits | Bonus |
|---|---|---|
| `Venom Rush` | Poison + Speedy | `+6%` attack speed |
| `Cold Steel` | Freezy + Sharpy | `+0.5` flat damage |
| `Field Cook` | Sustaining + Autosmelt | `+4%` mining speed |
| `Keeper's Reach` | Soulbound + Magnetic | `+4%` mining speed |
| `Deep Flow` | Broad Touch + Momentum | `+5%` mining speed |
| `Silk & Soul` | Silky + Soulbound | `+8%` XP gain |
| `Frozen Tempo` | Freezy + Momentum | `+4%` mining speed |
| `Berserker Script` | Frenzy + Scholar | `+4%` attack speed |
| `Iron Pledge` | Titan + Soulbound | `+0.5` flat damage |
| `Ranger Tempo` | Ranger + Frenzy | `+3%` attack speed |
| `Mooncraft` | Moonlit + Scholar | `+3%` mining speed |
| `Triune Focus` | Frenzy + Titan + Scholar | `+2%` attack speed, `+0.5` flat damage |
| `Nightfall Engine` | Moonlit + Momentum + Broad Touch | `+3%` mining speed |
| `Alchemist Loop` | Bountiful + Scavenger + Autosmelt | `+5%` XP gain |

## Trait detail pages

- [Previous Traits Index](traits/Previous-Traits)
- [Speedy](traits/Speedy)
- [Poison](traits/Poison)
- [Sustaining](traits/Sustaining)
- [Autosmelt](traits/Autosmelt)
- [Soulbound](traits/Soulbound)
- [Silky](traits/Silky)
- [Sharpy](traits/Sharpy)
- [Modifiable](traits/Modifiable)
- [Magnetic](traits/Magnetic)
- [Freezy](traits/Freezy)
- [Momentum](traits/Momentum)
- [Broad Touch](traits/Broad-Touch)
- [Frenzy](traits/Frenzy)
- [Titan](traits/Titan)
- [Scholar](traits/Scholar)
- [Moonlit](traits/Moonlit)
- [Ranger](traits/Ranger)
- [Bountiful](traits/Bountiful)
- [Scavenger](traits/Scavenger)
- [Purifying](traits/Purifying)
- [Swiftstep](traits/Swiftstep)
- [Bulwark](traits/Bulwark)

## Trait removal

- Hold `Trait Remover` in offhand and trigger upgrade action.
- One random trait is removed.
- Consumes one `Trait Remover`.
