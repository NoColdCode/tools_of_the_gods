#!/usr/bin/env python3
"""Generate placeholder PNGs for missing TOG textures (armor focus). Replace with final art later."""

from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
TEX = ROOT / "src/main/resources/assets/tools_of_the_gods/textures"

TIER_COLORS = [
    (120, 72, 40),   # 0 worn leather
    (145, 145, 150), # 1 rusty chainmail
    (175, 178, 185), # 2 forged iron
    (255, 200, 60),  # 3 gilded leather / gold
    (160, 80, 255),  # 4 amethyst
    (220, 40, 80),   # 5 ruby
    (40, 30, 50),    # 6 obsidian
    (30, 200, 90),   # 7 emerald
    (90, 220, 255),  # 8 diamond
    (255, 120, 255), # 9 divine
]

PIECES = {
    "helm": ("helm", "helmog"),
    "chest": ("chest", "chestog"),
    "leg": ("leg", "leggog"),
    "boot": ("boot", "bootog"),
}
PREFIXES = ["h", "c", "i", "g", "a", "u", "o", "e", "d"]


def save_item_icon(path: Path, name: str, rgb: tuple[int, int, int]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        return
    img = Image.new("RGBA", (32, 32), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    draw.rectangle((4, 4, 27, 27), fill=rgb + (255,))
    draw.rectangle((4, 4, 27, 27), outline=(255, 255, 255, 200), width=1)
    label = name[:6]
    draw.text((5, 11), label, fill=(255, 255, 255, 255))
    img.save(path)
    print(f"created {path.relative_to(ROOT)}")


def save_armor_layer(path: Path, label: str, rgba: tuple[int, int, int, int], size=(64, 32)) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        return
    img = Image.new("RGBA", size, rgba)
    if rgba[3] > 0:
        draw = ImageDraw.Draw(img)
        draw.text((4, 10), label, fill=(255, 255, 255, 255))
    img.save(path)
    print(f"created {path.relative_to(ROOT)}")


def main() -> None:
    item_dir = TEX / "item"
    armor_dir = TEX / "models/armor"

    for tier, color in enumerate(TIER_COLORS):
        for suffix, (_, tier9) in PIECES.items():
            if tier == 9:
                name = tier9
            else:
                name = PREFIXES[tier] + suffix
            save_item_icon(item_dir / f"{name}.png", name, color)

    tier_prefixes = ["h", "c", "i", "g", "a", "u", "o", "e", "d", "og"]
    for prefix in tier_prefixes:
        save_armor_layer(armor_dir / f"{prefix}_layer_1.png", f"{prefix} L1", (0, 0, 0, 0))
        save_armor_layer(armor_dir / f"{prefix}_layer_2.png", f"{prefix} L2", (0, 0, 0, 0))

    extras = {
        "wings.png": (200, 230, 255),
        "trait_upgrade_airborne.png": (180, 220, 255),
        "shield_of_the_gods.png": (160, 160, 200),
    }
    for name, color in extras.items():
        save_item_icon(item_dir / name, name.replace(".png", ""), color)

    print("Done. Replace placeholders with your art using the same filenames (all lowercase).")


if __name__ == "__main__":
    main()
