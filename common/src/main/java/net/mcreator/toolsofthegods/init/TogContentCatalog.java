package net.mcreator.toolsofthegods.init;

import net.minecraft.world.item.Item;

import net.mcreator.toolsofthegods.registry.TogRegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/** Shared catalog of registered content references for platform registration glue. */
public final class TogContentCatalog {
	private static final List<TogRegistryEntry<Item>> ALL_ITEMS = new ArrayList<>();

	private TogContentCatalog() {
	}

	public static void trackItem(TogRegistryEntry<Item> item) {
		ALL_ITEMS.add(item);
	}

	public static List<TogRegistryEntry<Item>> allItems() {
		return Collections.unmodifiableList(ALL_ITEMS);
	}

	public static <T> T bind(TogRegistryEntry<T> ref, T value) {
		ref.bind(value);
		return value;
	}

	public static <T> T bind(TogRegistryEntry<T> ref, Supplier<T> factory) {
		return bind(ref, factory.get());
	}
}
