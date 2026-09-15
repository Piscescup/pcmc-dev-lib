package io.github.piscescup.fabricmc.impl.store;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;
import io.github.piscescup.fabricmc.store.recipe.ReadableRecipeRegistrablesHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since
 */
public enum MutableReciperegistrablesHolder implements ReadableRecipeRegistrablesHolder {
    INSTANCE;

    private final Map<ResourceKey<Recipe<?>>, RecipeRegistrable<?>> recipes =
        new LinkedHashMap<>();

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

    @Override
    public @NotNull Collection<RecipeRegistrable<?>> recipes() {
        return Collections.unmodifiableCollection(recipes.values());
    }

    @Override
    public @NotNull Optional<RecipeRegistrable<?>> recipe(
        @NotNull ResourceKey<Recipe<?>> id
    ) {
        return Optional.ofNullable(recipes.get(id));
    }
}
