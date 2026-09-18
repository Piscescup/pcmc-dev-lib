package io.github.piscescup.fabricmc.datagen.recipe;

import io.github.piscescup.fabricmc.api.recipe.RecipeRegistrable;
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
 * Generates recipe and recipe-advancement data from collected definitions.
 *
 * <p>The constructor retains the collection exposed by a
 * {@link ReadableRecipeRegistrablesHolder}. When Fabric invokes the provider,
 * each {@link RecipeRegistrable} is saved under its default identifier using a
 * shared {@link RecipeGenerationContextImpl}.</p>
 *
 * <p>With the supplied mutable holder, the retained collection is a live,
 * unmodifiable view. Recipe definitions registered after provider construction
 * remain visible as long as registration finishes before the provider iterates
 * the collection.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RecipeRegistrable
 * @see ReadableRecipeRegistrablesHolder
 */
public class RecipesGenerator
    extends FabricRecipeProvider
{
    /** The stable name reported to Fabric's data-generator diagnostics. */
    private static final String PCMC_DEV_LIB_RECIPE_GENERATOR_NAME =
        "PC MC Develop Lib Recipe Generator";

    /** Recipe definitions traversed when the provider builds its output. */
    private final Collection<RecipeRegistrable<?>> recipes;

    /**
     * Creates a recipe provider backed by a readable recipe holder.
     *
     * @param output           the Fabric pack output receiving generated files
     * @param registriesFuture the future supplying registry lookup data
     * @param recipesHolder    the holder whose recipe collection should be generated
     * @throws NullPointerException if {@code recipesHolder} is {@code null}
     */
    public RecipesGenerator(
        FabricPackOutput output,
        CompletableFuture<HolderLookup.Provider> registriesFuture,
        @NotNull ReadableRecipeRegistrablesHolder recipesHolder
    ) {
        super(output, registriesFuture);
        this.recipes = recipesHolder.recipes();
    }

    /**
     * Creates the Minecraft provider that saves each retained recipe definition.
     *
     * @param registries the registry lookup for this generation run
     * @param output     the output receiving recipes and their advancements
     * @return a provider that traverses the retained recipes in collection order
     */
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

    /**
     * Returns the provider name displayed in data-generator output.
     *
     * @return {@value #PCMC_DEV_LIB_RECIPE_GENERATOR_NAME}
     */
    @Override
    @NotNull
    public String getName() {
        return PCMC_DEV_LIB_RECIPE_GENERATOR_NAME;
    }
}
