# Admin Commands

Command root: `/tog`

## Permission model

- Dispatcher requirement is currently `requires(cs -> true)`.
- Individual command checks enforce creative mode on some actions.

## Command list

### `/tog setlevel <level>`

- Allowed range: `0..100`
- Requires creative mode
- Works on currently held TOG tool
- Sets: level, tier, xp=0, nexttier, needsUpgrade

### `/tog givepickaxe <level>`

- Allowed range: `0..100`
- Requires creative mode
- Gives initialized TOG pickaxe at target level

### `/tog givebow <level>`

- Allowed range: `0..50`
- Gives initialized TOG bow at target level
- Note: no explicit creative check in current implementation

### `/tog addxp <amount>`

- Allowed range: `1..10000`
- Requires creative mode
- Adds raw XP to held TOG tool

### `/tog info`

- Displays held tool info:
  - tool type
  - level
  - tier and tier name
  - xp and next level xp
  - computed mining speed summary
