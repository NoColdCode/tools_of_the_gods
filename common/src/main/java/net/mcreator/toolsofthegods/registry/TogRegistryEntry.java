package net.mcreator.toolsofthegods.registry;

import java.util.function.Supplier;

/**
 * Loader-neutral registry holder. Platform modules bind values during registration
 * (NeoForge {@code DeferredRegister}, Fabric {@code Registry.register}, etc.).
 */
public final class TogRegistryEntry<T> implements Supplier<T> {
	private T value;

	public void bind(T value) {
		if (this.value != null) {
			throw new IllegalStateException("Registry entry already bound: " + value);
		}
		this.value = value;
	}

	@Override
	public T get() {
		if (value == null) {
			throw new IllegalStateException("Registry entry not yet bound");
		}
		return value;
	}

	public boolean isBound() {
		return value != null;
	}
}
