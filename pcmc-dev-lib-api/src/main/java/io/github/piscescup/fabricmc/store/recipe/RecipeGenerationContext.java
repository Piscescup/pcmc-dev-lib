package io.github.piscescup.fabricmc.store.recipe;

import io.github.piscescup.fabricmc.api.recipe.RecipeRegistrable;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 * Exposes the registry lookup and output used while generating recipes.
 *
 * <p>A {@link RecipeRegistrable} receives this context when a recipe data
 * provider asks it to save. The context keeps recipe definitions independent
 * of the concrete Fabric provider while still allowing them to resolve holders
 * from any available {@link Registry}.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RecipeRegistrable
 */
public interface RecipeGenerationContext {
    /**
     * Returns the registry lookup available to the current data-generator run.
     *
     * @return the non-null registry lookup provider
     */
    @NotNull HolderLookup.Provider registryLookup();

    /**
     * Returns the output that receives generated recipes and advancements.
     *
     * @return the non-null recipe output
     */
    @NotNull RecipeOutput output();

    /**
     * Obtains a typed holder getter for a registry.
     *
     * <p>This is a convenience wrapper around
     * {@link HolderLookup.Provider#lookupOrThrow(ResourceKey)}.</p>
     *
     * @param registryKey the key of the registry to access
     * @param <T>         the value type stored by the registry
     * @return the holder getter for the requested registry
     * @throws IllegalStateException if the registry is unavailable from the lookup provider
     */
    default <T> @NotNull HolderGetter<T> holderGetter(ResourceKey<Registry<T>> registryKey) {
        return registryLookup()
            .lookupOrThrow(registryKey);
    }
}
