# Tool Base Stats

These are base item attributes before tier multipliers, trait multipliers, and effects.

## Primal tool constructors

| Tool | Base attack damage arg | Base attack speed arg | Tier speed stat | Enchant value | Durability handling |
|---|---:|---:|---:|---:|---|
| Pickaxe | 0.0 | -3.0 | 0.5 | 3 | MAX_DAMAGE removed (effectively infinite) |
| Hammer | 1.0 | -3.2 | 0.5 | 3 | MAX_DAMAGE removed |
| Axe | 0.0 | -3.0 | 2.0 | 3 | MAX_DAMAGE removed |
| Shovel | 0.0 | -3.0 | 2.0 | 3 | MAX_DAMAGE removed |
| Hoe | 0.0 | -3.0 | 2.0 | 3 | MAX_DAMAGE removed |
| Sword | 0.0 | -3.0 | 2.0 | 3 | MAX_DAMAGE removed |
| Bow | n/a | n/a | n/a | n/a | MAX_DAMAGE removed |

## XP gain by action

| Tool | XP event | XP amount |
|---|---|---:|
| Pickaxe | Block mined | handled by pickaxe block procedure |
| Hammer | Center + each AoE block mined | `1` per block, or `2` per block at tier >= 4 |
| Axe | Block mined | `1` |
| Shovel | Block mined | `1` |
| Hoe | Block mined | `1` |
| Sword | Successful melee hit | `2` |
| Bow | Release shot with charge > 5 ticks | `2` |

## Harvest gates for pickaxe-based tools

- Not mineable-with-pickaxe blocks: denied.
- `needs_stone_tool`: tier >= 1
- `needs_iron_tool`: tier >= 2
- `needs_diamond_tool`: tier >= 8

## One-shot logic

Pickaxe/Hammer one-shot whitelist by tier:

- Tier >= 1: `BASE_STONE_OVERWORLD`
- Tier >= 2: `DEEPSLATE_ORE_REPLACEABLES`
- Tier >= 3: `GOLD_ORES`
- Tier >= 4: `IRON_ORES`
- Tier >= 5: `REDSTONE_ORES`
- Tier >= 6: `COAL_ORES`
- Tier >= 7: `COPPER_ORES`
- Tier >= 8: `DIAMOND_ORES`
- Tier >= 9: all pickaxe-mineable blocks
