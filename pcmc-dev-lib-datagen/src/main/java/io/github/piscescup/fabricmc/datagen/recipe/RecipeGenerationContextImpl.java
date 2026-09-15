package io.github.piscescup.fabricmc.datagen.recipe;

import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable data-provider implementation of {@link RecipeGenerationContext}.
 *
 * @param registryLookup the registry lookup for the current generation run
 * @param output         the output receiving recipes and advancements
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
record RecipeGenerationContextImpl(
    HolderLookup.Provider registryLookup,
    RecipeOutput output
)
    implements RecipeGenerationContext
{

    /**
     * Creates a context from the values supplied to a Minecraft recipe provider.
     *
     * @param registryLookup the non-null registry lookup
     * @param output         the non-null recipe output
     */
    RecipeGenerationContextImpl(
        @NotNull HolderLookup.Provider registryLookup,
        @NotNull RecipeOutput output
    ) {
        this.registryLookup = registryLookup;
        this.output = output;
    }

}
