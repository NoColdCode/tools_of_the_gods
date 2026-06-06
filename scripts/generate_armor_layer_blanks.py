#!/usr/bin/env python3
"""Create blank worn-armor layer PNGs (64x32) for each TOG armor tier.

Default: fully transparent files ready for you to paste art and hue-shift per tier.
Use --clone-reference to duplicate motion_of_the_gods_layer_*.png into every tier instead.
Use --force to overwrite files that already exist.
"""

from __future__ import annotations

import argparse
import shutil
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
ARMOR_DIR = ROOT / "src/main/resources/assets/tools_of_the_gods/textures/models/armor"

# Same prefixes as TogArmorTextures.wornLayerPrefix()
TIER_PREFIXES = ["h", "c", "i", "g", "a", "u", "o", "e", "d", "og"]
LEGACY_REFERENCE = "motion_of_the_gods"


def save_blank(path: Path) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    Image.new("RGBA", (64, 32), (0, 0, 0, 0)).save(path)


def clone_reference(path: Path, layer: int, force: bool) -> None:
    if path.exists() and not force:
        return
    ref = ARMOR_DIR / f"{LEGACY_REFERENCE}_layer_{layer}.png"
    if ref.exists():
        path.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(ref, path)
        print(f"cloned {path.relative_to(ROOT)} <- {ref.name}")
    else:
        save_blank(path)
        print(f"created blank {path.relative_to(ROOT)}")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--clone-reference",
        action="store_true",
        help=f"Copy {LEGACY_REFERENCE}_layer_*.png into each tier file (good hue-shift starting point)",
    )
    parser.add_argument(
        "--force",
        action="store_true",
        help="Overwrite existing tier layer files",
    )
    args = parser.parse_args()

    for prefix in TIER_PREFIXES:
        for layer in (1, 2):
            path = ARMOR_DIR / f"{prefix}_layer_{layer}.png"
            if path.exists() and not args.force:
                continue
            if args.clone_reference:
                clone_reference(path, layer, args.force)
            else:
                save_blank(path)
                print(f"created blank {path.relative_to(ROOT)}")

    print(
        "Done. Edit each *_layer_1.png / *_layer_2.png (64x32, vanilla armor UV). "
        "Hue-shift per tier in GIMP / Photopea / Photoshop."
    )


if __name__ == "__main__":
    main()
