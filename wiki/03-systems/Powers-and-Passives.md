# Powers and Passives

## Activation key

- Keybind: `V`
- Translation key: `key.tools_of_the_gods.activate_pickaxe_power`
- Sends `activate_pickaxe_power` packet to server.

## Active power (all TOG tools)

Uses per-item cooldown NBT: `togPowerCooldownEnd`.

### Standard tools (non-bow)

By tier:

| Tier | Haste amplifier | Haste level in game | Duration (ticks) | Duration (seconds) | Cooldown (ticks) | Cooldown (seconds) |
|---:|---:|---|---:|---:|---:|---:|
| 1 | 2 | Haste III | 60 | 3 | 1200 | 60 |
| 2 | 2 | Haste III | 100 | 5 | 1200 | 60 |
| 3 | 2 | Haste III | 120 | 6 | 1200 | 60 |
| 4 | 2 | Haste III | 120 | 6 | 1200 | 60 |
| 5 | 1 | Haste II | 160 | 8 | 1200 | 60 |
| 6 | 2 | Haste III | 300 | 15 | 900 | 45 |
| 7 | 3 | Haste IV | 300 | 15 | 900 | 45 |
| 8 | 4 | Haste V | 400 | 20 | 2400 | 120 |
| 9 | 5 | Haste VI | 500 | 25 | 2000 | 100 |

Flight granted at tier >= 8:

- Tier 8: `600` ticks (`30s`)
- Tier 9: `800` ticks (`40s`)

### Bow override

Bow replaces standard active values:

- Haste amplifier: `max(1, tier)`
- Haste duration: `80 + tier * 20` ticks
- Cooldown: `800` ticks (`40s`)

## Passive effects

- Tier >= 6 (all TOG tools): Night Vision refresh (`240` ticks)
- Sword tier >= 4: Strength with amp `min(2, tier/3)`
- Bow tier >= 2: Speed with amp `min(2, tier/2)`
- Tool group (pickaxe, hammer, axe, shovel, hoe):
  - Tier >= 7: Haste IV (amp 3)
  - Tier >= 8: Haste V (amp 4)
