package net.mcreator.toolsofthegods.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

import net.mcreator.toolsofthegods.ToolsOfTheGodsMod;
import net.mcreator.toolsofthegods.world.TempleOfTheGodsFeature;

public class TogModFeatures {
	public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, ToolsOfTheGodsMod.MODID);

	public static final DeferredHolder<Feature<?>, Feature<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>> TEMPLE_OF_THE_GODS =
		REGISTRY.register("temple_of_the_gods", () -> new TempleOfTheGodsFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));
}
