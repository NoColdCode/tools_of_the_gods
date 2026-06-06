#!/usr/bin/env python3
"""Remap armor item texture PNGs when tier prefixes change (see TogArmorTextures.java)."""

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ITEM = ROOT / "src/main/resources/assets/tools_of_the_gods/textures/item"

SUFFIXES = ["helm", "chest", "leg", "boot"]
TIER9 = {"helm": "helmog", "chest": "chestog", "leg": "leggog", "boot": "bootog"}

# Previous divine-metal prefixes → leather/chain/iron/gold progression
OLD_PREFIXES = ["i", "t", "g", "l", "a", "u", "o", "e", "d"]
NEW_PREFIXES = ["h", "c", "i", "g", "a", "u", "o", "e", "d"]


def main() -> None:
    staging = ITEM / "_armor_migrate_staging"
    if staging.exists():
        for f in staging.glob("*.png"):
            f.unlink()
    staging.mkdir(exist_ok=True)

    for suffix in SUFFIXES:
        for tier, (old_p, new_p) in enumerate(zip(OLD_PREFIXES, NEW_PREFIXES)):
            if old_p == new_p:
                src = ITEM / f"{old_p}{suffix}.png"
                if src.exists() and not (staging / f"{new_p}{suffix}.png").exists():
                    src.replace(staging / f"{new_p}{suffix}.png")
                continue
            src = ITEM / f"{old_p}{suffix}.png"
            if src.exists():
                src.replace(staging / f"{new_p}{suffix}.png")

        og = TIER9[suffix]
        src = ITEM / f"{og}.png"
        if src.exists() and not (staging / f"{og}.png").exists():
            src.replace(staging / f"{og}.png")

    for tmp in staging.glob("*.png"):
        dest = ITEM / tmp.name
        if dest.exists():
            dest.unlink()
        tmp.replace(dest)
        print(f"  {dest.name}")

    staging.rmdir()
    print("Armor texture migration complete.")


if __name__ == "__main__":
    main()
