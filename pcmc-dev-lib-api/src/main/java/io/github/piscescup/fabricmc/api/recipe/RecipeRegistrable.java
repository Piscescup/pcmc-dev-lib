package io.github.piscescup.fabricmc.api.recipe;

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
 * Describes a recipe that can be collected for later data generation.
 *
 * <p>A recipe definition has a {@link #defaultId() default identifier} and can
 * be enriched with an optional recipe-book group and advancement criteria.
 * Calling {@link #register()} places the definition in the library's shared
 * recipe store; {@link #collectsTo(Collection)} can instead add it to a
 * caller-managed collection.</p>
 *
 * <p>The definition does not write JSON when it is registered. A data provider
 * later calls one of the {@code save} methods with a
 * {@link RecipeGenerationContext}, at which point the concrete implementation
 * creates and submits the Minecraft recipe builder.</p>
 *
 * @param <SUB_RR> the concrete fluent recipe-definition type
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RecipeGenerationContext
 */
public interface RecipeRegistrable<SUB_RR extends RecipeRegistrable<SUB_RR>> {

    /**
     * Adds an advancement criterion that unlocks this recipe.
     *
     * <p>Criterion names must be unique within one recipe. The supplied
     * implementation retains criteria in insertion order and rejects a second
     * criterion with the same name.</p>
     *
     * @param name      the criterion name written to the recipe advancement
     * @param criterion the Minecraft criterion used to unlock the recipe
     * @return this recipe definition
     * @throws IllegalArgumentException if the supplied implementation already
     *         contains a criterion with the same name
     */
    SUB_RR unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion);

    /**
     * Sets the recipe-book group for this recipe.
     *
     * <p>Recipes with the same non-null group can be displayed together in the
     * recipe book. Passing {@code null} clears the configured group.</p>
     *
     * @param group the recipe-book group, or {@code null} for no group
     * @return this recipe definition
     */
    SUB_RR group(@Nullable String group);

    /**
     * Returns the identifier used by {@link #save(RecipeGenerationContext)}.
     *
     * @return the default recipe resource key
     */
    ResourceKey<Recipe<?>> defaultId();

    /**
     * Registers this definition with the library's recipe store.
     *
     * <p>Registration collects the definition for a later recipe data-provider
     * run; it does not register a runtime {@link Recipe} instance.</p>
     *
     * @throws IllegalStateException if another collected recipe already uses
     *         the same default identifier in the supplied implementation
     */
    void register();

    /**
     * Writes this recipe through the supplied generation context under an
     * explicit resource key.
     *
     * <p>Concrete implementations transfer their retained ingredients,
     * criteria, group, and other recipe-specific options to a Minecraft recipe
     * builder before submitting it to {@link RecipeGenerationContext#output()}.</p>
     *
     * @param context  the active data-generation context
     * @param location the resource key under which the recipe is saved
     */
    void save(RecipeGenerationContext context, ResourceKey<Recipe<?>> location);

    /**
     * Writes this recipe under its {@linkplain #defaultId() default identifier}.
     *
     * @param context the active data-generation context
     * @see #save(RecipeGenerationContext, ResourceKey)
     */
    default void save(final RecipeGenerationContext context) {
        this.save(context, defaultId());
    }

    /**
     * Writes this recipe under an identifier parsed from a string.
     *
     * <p>A namespaced value such as {@code example:compressed_iron} is parsed
     * directly by {@link Identifier#parse(String)} and converted to a recipe
     * resource key. Call {@link #save(RecipeGenerationContext)} when the desired
     * identifier is the recipe's default.</p>
     *
     * @param context the active data-generation context
     * @param id      the namespaced identifier to parse
     * @throws IllegalStateException if the parsed key is the same key instance
     *         as {@link #defaultId()}
     * @see #save(RecipeGenerationContext, ResourceKey)
     */
    default void save(final RecipeGenerationContext context, final String id) {
        ResourceKey<Recipe<?>> defaultKey = this.defaultId();
        ResourceKey<Recipe<?>> overriddenKey = ResourceKey.create(Registries.RECIPE, Identifier.parse(id));
        if (overriddenKey == defaultKey) {
            throw new IllegalStateException("Recipe " + id + " should remove its 'save' argument as it is equal to default one");
        }

        this.save(context, overriddenKey);
    }

    /**
     * Adds this definition to a caller-managed recipe collection.
     *
     * <p>The collection receives this instance immediately. No duplicate check
     * is performed, and the recipe is not added to the shared recipe store.</p>
     *
     * @param recipes the mutable collection that should receive this definition
     * @return this recipe definition
     * @throws UnsupportedOperationException if the collection does not support insertion
     */
    @SuppressWarnings("unchecked")
    default SUB_RR collectsTo(Collection<RecipeRegistrable<?>> recipes) {
        recipes.add(this);
        return (SUB_RR) this;
    }
}
