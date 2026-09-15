package io.github.piscescup.fabricmc.store.recipe;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides read-only access to recipe definitions collected for data generation.
 *
 * <p>Recipes are addressed by their
 * {@link RecipeRegistrable#defaultId() default resource keys}. The supplied
 * mutable holder preserves registration order and exposes an unmodifiable live
 * collection, allowing a provider created earlier to observe recipes collected
 * before generation begins.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RecipeRegistrable
 */
public interface ReadableRecipeRegistrablesHolder {
    /**
     * Returns the currently collected recipe definitions.
     *
     * @return a non-null, read-only collection of recipes, possibly empty
     */
    @NotNull Collection<RecipeRegistrable<?>> recipes();

    /**
     * Looks up a recipe definition by its default resource key.
     *
     * @param id the default resource key to find
     * @return the matching recipe definition, or an empty optional when absent
     */
    @NotNull Optional<RecipeRegistrable<?>> recipe(
        @NotNull ResourceKey<Recipe<?>> id
    );
}
