package io.github.piscescup.fabricmc.datagen.recipe;

import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since
 */
record RecipeGenerationContextImpl(
    HolderLookup.Provider registryLookup,
    RecipeOutput output
)
    implements RecipeGenerationContext
{

    RecipeGenerationContextImpl(
        @NotNull HolderLookup.Provider registryLookup,
        @NotNull RecipeOutput output
    ) {
        this.registryLookup = registryLookup;
        this.output = output;
    }

}
