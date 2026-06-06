#!/usr/bin/env python3
"""Build TOG worn-armor layers from vanilla Minecraft armor + trim templates.

Pipeline per tier:
  1. Load vanilla base armor layer (+ leather stitch overlay when relevant)
  2. Composite a trim pattern on top (different pattern per tier)
  3. Desaturate to grayscale (single luminosity channel)
  4. Re-color with the tier material hue (shadow -> highlight)

Requires the Minecraft client jar from a NeoForge/Gradle setup (1.21.1 by default).
"""

from __future__ import annotations

import argparse
import io
import zipfile
from pathlib import Path

from PIL import Image, ImageOps

ROOT = Path(__file__).resolve().parents[1]
OUT_DIR = ROOT / "src/main/resources/assets/tools_of_the_gods/textures/models/armor"

# prefix, vanilla base armor id, trim pattern id, material RGB, optional shadow scale
TIER_SPECS: list[tuple[str, str, str, tuple[int, int, int], float]] = [
    ("h", "leather", "coast", (120, 72, 40), 0.30),       # 0 worn leather
    ("c", "chainmail", "sentry", (145, 145, 150), 0.35),  # 1 rusty chainmail
    ("i", "iron", "vex", (175, 178, 185), 0.35),          # 2 forged iron
    ("g", "gold", "rib", (255, 200, 60), 0.30),           # 3 gilded leather
    ("a", "diamond", "spire", (160, 80, 255), 0.28),      # 4 amethyst
    ("u", "diamond", "snout", (220, 40, 80), 0.28),       # 5 ruby
    ("o", "netherite", "ward", (40, 30, 50), 0.22),       # 6 obsidian runed
    ("e", "diamond", "eye", (30, 200, 90), 0.28),         # 7 arcane emerald
    ("d", "diamond", "flow", (90, 220, 255), 0.28),       # 8 ethereal diamond
    ("og", "netherite", "silence", (255, 120, 255), 0.25),  # 9 divine
]


def find_minecraft_jar(explicit: Path | None) -> Path:
    if explicit and explicit.is_file():
        return explicit
    home = Path.home()
    candidates = [
        home / ".gradle/caches/neoformruntime/artifacts/minecraft_1.21.1_client.jar",
        home / ".gradle/caches/neoformruntime/artifacts/minecraft_1.21.1-client.jar",
    ]
    for path in candidates:
        if path.is_file():
            return path
    raise FileNotFoundError(
        "Could not find minecraft client jar. Run gradlew once, or pass --jar PATH"
    )


def load_texture(jar: zipfile.ZipFile, rel: str) -> Image.Image:
    key = f"assets/minecraft/textures/{rel}"
    try:
        data = jar.read(key)
    except KeyError as exc:
        raise FileNotFoundError(f"Missing in client jar: {key}") from exc
    return Image.open(io.BytesIO(data)).convert("RGBA")


def composite_all(layers: list[Image.Image]) -> Image.Image:
    if not layers:
        raise ValueError("No layers to composite")
    out = layers[0].copy()
    for layer in layers[1:]:
        out = Image.alpha_composite(out, layer)
    return out


def desaturate(img: Image.Image) -> Image.Image:
    rgba = img.convert("RGBA")
    r, g, b, a = rgba.split()
    gray = ImageOps.grayscale(Image.merge("RGB", (r, g, b)))
    return Image.merge("RGBA", (gray, gray, gray, a))


def tint_material(gray: Image.Image, rgb: tuple[int, int, int], shadow: float) -> Image.Image:
    """Map luminosity to a material color (dark shadow -> bright highlight)."""
    r, g, b = rgb
    dark = (max(0, int(r * shadow)), max(0, int(g * shadow)), max(0, int(b * shadow)))
    bright = (min(255, int(r * 1.12 + 18)), min(255, int(g * 1.12 + 18)), min(255, int(b * 1.12 + 18)))
    lum = gray.split()[0]
    colored = ImageOps.colorize(lum.convert("L"), black=dark, white=bright)
    colored.putalpha(gray.split()[3])
    return colored


def build_layer(
    jar: zipfile.ZipFile,
    base_id: str,
    trim_id: str,
    layer: int,
    material_rgb: tuple[int, int, int],
    shadow: float,
) -> Image.Image:
    layer_suffix = f"_layer_{layer}"
    leggings_suffix = "_leggings" if layer == 2 else ""

    base = load_texture(jar, f"models/armor/{base_id}{layer_suffix}.png")
    layers: list[Image.Image] = [base]

    if base_id == "leather":
        overlay_key = f"models/armor/leather{layer_suffix}_overlay.png"
        try:
            layers.append(load_texture(jar, overlay_key))
        except FileNotFoundError:
            pass

    trim_key = f"trims/models/armor/{trim_id}{leggings_suffix}.png"
    try:
        layers.append(load_texture(jar, trim_key))
    except FileNotFoundError:
        trim_key = f"trims/models/armor/{trim_id}.png"
        layers.append(load_texture(jar, trim_key))

    merged = composite_all(layers)
    gray = desaturate(merged)
    return tint_material(gray, material_rgb, shadow)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--jar", type=Path, default=None, help="Path to minecraft client jar")
    parser.add_argument("--force", action="store_true", help="Overwrite existing tier layers")
    args = parser.parse_args()

    jar_path = find_minecraft_jar(args.jar)
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    print(f"Using {jar_path}")
    with zipfile.ZipFile(jar_path) as jar:
        for prefix, base_id, trim_id, color, shadow in TIER_SPECS:
            for layer in (1, 2):
                out = OUT_DIR / f"{prefix}_layer_{layer}.png"
                if out.exists() and not args.force:
                    print(f"skip {out.name} (exists)")
                    continue
                img = build_layer(jar, base_id, trim_id, layer, color, shadow)
                img.save(out)
                print(f"wrote {out.relative_to(ROOT)}  base={base_id} trim={trim_id} color={color}")

    print("Done. Re-run with --force after changing TIER_SPECS colors or trim choices.")


if __name__ == "__main__":
    main()
