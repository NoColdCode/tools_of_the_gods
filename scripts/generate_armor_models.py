#!/usr/bin/env python3
"""Generate armor item model JSON files with tier overrides."""

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MODELS = ROOT / "src/main/resources/assets/tools_of_the_gods/models/item"

# Armor prefixes: leather → chain → iron → gold → gems (see TogArmorTextures.java)
TIER_PREFIX = ["h", "c", "i", "g", "a", "u", "o", "e", "d"]
PIECES = {
    "helmet_of_the_gods": ("helm", "helmog"),
    "chestplate_of_the_gods": ("chest", "chestog"),
    "leggings_of_the_gods": ("leg", "leggog"),
    "boots_of_the_gods": ("boot", "bootog"),
}


def tex(piece_suffix: str, tier9: str, tier: int) -> str:
    if tier == 9:
        return tier9
    return f"{TIER_PREFIX[tier]}{piece_suffix}"


def main() -> None:
    for item_id, (suffix, tier9) in PIECES.items():
        base_tex = tex(suffix, tier9, 0)
        overrides = []
        for tier in range(1, 10):
            overrides.append({
                "predicate": {"tools_of_the_gods:tier": tier / 10.0},
                "model": f"tools_of_the_gods:item/{item_id}_tier{tier}",
            })
            tier_model = {
                "parent": "item/generated",
                "textures": {"layer0": f"tools_of_the_gods:item/{tex(suffix, tier9, tier)}"},
            }
            path = MODELS / f"{item_id}_tier{tier}.json"
            path.write_text(json.dumps(tier_model, indent=2) + "\n", encoding="utf-8")

        main_model = {
            "parent": "item/generated",
            "textures": {"layer0": f"tools_of_the_gods:item/{base_tex}"},
            "overrides": overrides,
        }
        (MODELS / f"{item_id}.json").write_text(json.dumps(main_model, indent=2) + "\n", encoding="utf-8")
        print(f"updated {item_id}.json + 9 tier models")


if __name__ == "__main__":
    main()
