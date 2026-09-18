package io.github.piscescup.fabricmc.impl.store;

import io.github.piscescup.fabricmc.api.recipe.RecipeRegistrable;
import io.github.piscescup.fabricmc.store.recipe.ReadableRecipeRegistrablesHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Collects recipe definitions by their default resource keys.
 *
 * <p>The singleton preserves first-registration order and rejects duplicate
 * keys. It exposes a live, unmodifiable collection for data providers, so a
 * provider holding that view can observe recipes added before generation
 * begins without allowing callers to mutate the backing map directly.</p>
 *
 * <p>This holder performs no synchronization. Populate it during the normal
 * single-threaded registration phase before recipe data generation starts.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ReadableRecipeRegistrablesHolder
 */
public enum MutableReciperegistrablesHolder implements ReadableRecipeRegistrablesHolder {
    /** The shared holder used by recipe registration implementations. */
    INSTANCE;

    /** Recipes keyed by default ID in first-registration order. */
    private final Map<ResourceKey<Recipe<?>>, RecipeRegistrable<?>> recipes =
        new LinkedHashMap<>();

    /**
     * Adds a recipe definition if its default resource key is not yet present.
     *
     * @param recipe the recipe definition to collect
     * @param <R>    the concrete recipe-definition type
     * @return the same recipe definition
     * @throws NullPointerException if {@code recipe} is {@code null}
     * @throws IllegalStateException if another definition already uses the same default ID
     */
    public <R extends RecipeRegistrable<?>> @NotNull R add(
        @NotNull R recipe
    ) {
        ResourceKey<Recipe<?>> id = recipe.defaultId();

        RecipeRegistrable<?> previous =
            recipes.putIfAbsent(id, recipe);

        if (previous != null) {
            throw new IllegalStateException(
                "Duplicate recipe id: " + id
            );
        }

        return recipe;
    }

    /**
     * Returns a live, unmodifiable view of recipes in registration order.
     *
     * @return the collected recipe definitions, possibly empty
     */
    @Override
    public @NotNull Collection<RecipeRegistrable<?>> recipes() {
        return Collections.unmodifiableCollection(recipes.values());
    }

    /**
     * Looks up a recipe definition by its default resource key.
     *
     * @param id the default recipe resource key
     * @return the matching definition, or an empty optional when absent
     */
    @Override
    public @NotNull Optional<RecipeRegistrable<?>> recipe(
        @NotNull ResourceKey<Recipe<?>> id
    ) {
        return Optional.ofNullable(recipes.get(id));
    }
}
