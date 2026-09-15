package io.github.piscescup.fabricmc.api.registers.recipe;

import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface RecipeRegistrable<SUB_RR extends RecipeRegistrable<SUB_RR>> {

    SUB_RR unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion);

    SUB_RR group(@Nullable String group);

    ResourceKey<Recipe<?>> defaultId();

    void register();


    void save(RecipeGenerationContext context, ResourceKey<Recipe<?>> location);

    default void save(final RecipeGenerationContext context) {
        this.save(context, defaultId());
    }

    default void save(final RecipeGenerationContext context, final String id) {
        ResourceKey<Recipe<?>> defaultKey = this.defaultId();
        ResourceKey<Recipe<?>> overriddenKey = ResourceKey.create(Registries.RECIPE, Identifier.parse(id));
        if (overriddenKey == defaultKey) {
            throw new IllegalStateException("Recipe " + id + " should remove its 'save' argument as it is equal to default one");
        }

        this.save(context, overriddenKey);
    }

    @SuppressWarnings("unchecked")
    default SUB_RR collectsTo(Collection<RecipeRegistrable<?>> recipes) {
        recipes.add(this);
        return (SUB_RR) this;
    }
}
