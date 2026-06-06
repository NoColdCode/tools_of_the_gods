# Hammer 3x3 System

## Area logic

- Breaks center block normally.
- Then attempts extra blocks in a 3x3x1 plane around center (8 additional blocks).
- Plane orientation is based on player look axis:
  - Mostly vertical look -> horizontal plane (Y axis)
  - Otherwise X or Z aligned plane

## Safety rules

A target block is AoE-broken only if all are true:

- Not air
- Tagged `mineable/pickaxe`
- Passes tier harvest check (`TierSystem.canHarvest`)
- Destroy speed >= 0 (not unbreakable)

## XP behavior

- Center and every valid extra block grants XP.
- XP per broken block:
  - Tier < 4: `1`
  - Tier >= 4: `2`

## Recursion guard

- NBT flag `togHammerAoeActive` prevents nested re-trigger loops during AoE chain.
