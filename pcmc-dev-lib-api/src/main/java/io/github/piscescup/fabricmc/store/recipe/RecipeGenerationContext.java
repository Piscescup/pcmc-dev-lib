package io.github.piscescup.fabricmc.store.recipe;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface RecipeGenerationContext {
    @NotNull HolderLookup.Provider registryLookup();

    @NotNull RecipeOutput output();

    default <T> @NotNull HolderGetter<T> holderGetter(ResourceKey<Registry<T>> registryKey) {
        return registryLookup()
            .lookupOrThrow(registryKey);
    }
}
