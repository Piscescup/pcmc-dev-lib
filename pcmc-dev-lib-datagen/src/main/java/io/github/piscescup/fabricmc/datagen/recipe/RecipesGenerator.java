package io.github.piscescup.fabricmc.datagen.recipe;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;
import io.github.piscescup.fabricmc.store.recipe.ReadableRecipeRegistrablesHolder;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class RecipesGenerator
    extends FabricRecipeProvider
{
    private static final String PCMC_DEV_LIB_RECIPE_GENERATOR_NAME =
        "PC MC Develop Lib Recipe Generator";

    private final Collection<RecipeRegistrable<?>> recipes;

    public RecipesGenerator(
        FabricPackOutput output,
        CompletableFuture<HolderLookup.Provider> registriesFuture,
        @NotNull ReadableRecipeRegistrablesHolder recipesHolder
    ) {
        super(output, registriesFuture);
        this.recipes = recipesHolder.recipes();
    }

    @Override
    @NotNull
    protected RecipeProvider createRecipeProvider(
        HolderLookup.@NotNull Provider registries,
        @NotNull RecipeOutput output
    ) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                RecipeGenerationContextImpl context =
                    new RecipeGenerationContextImpl(registries, output);

                for (RecipeRegistrable<?> registrable : recipes) {
                    registrable.save(context);
                }
            }
        };
    }

    @Override
    @NotNull
    public String getName() {
        return PCMC_DEV_LIB_RECIPE_GENERATOR_NAME;
    }
}
