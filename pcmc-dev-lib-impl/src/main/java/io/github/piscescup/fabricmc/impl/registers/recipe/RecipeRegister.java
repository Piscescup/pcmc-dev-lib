package io.github.piscescup.fabricmc.impl.registers.recipe;

import io.github.piscescup.fabricmc.api.recipe.RecipeRegistrable;
import io.github.piscescup.fabricmc.impl.store.recipe.MutableReciperegistrablesHolder;
import net.minecraft.advancements.triggers.Criterion;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Supplies shared collection and criterion behavior for recipe definitions.
 *
 * <p>Concrete recipes retain their generation inputs until a recipe provider
 * calls {@code save}. This base class stores arbitrary Minecraft criteria in
 * insertion order and registers definitions with
 * {@link MutableReciperegistrablesHolder}.</p>
 *
 * @param <SUB_RR> the concrete fluent recipe-definition type
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RecipeRegistrable
 */
public abstract class RecipeRegister<SUB_RR extends RecipeRegistrable<SUB_RR>>
    implements RecipeRegistrable<SUB_RR>
{
    /**
     * Advancement criteria keyed by their generated names.
     * The map preserves the order in which criteria are added.
     */
    protected final Map<String, Criterion<?>> criteria =
        new LinkedHashMap<>();

    /**
     * The configured recipe-book group, or {@code null} when no group is selected.
     */
    private @Nullable String group;

    /**
     * Creates an empty base recipe definition with no criteria or group.
     */
    public RecipeRegister() {}

    /**
     * Adds this definition to the shared recipe holder.
     *
     * @throws IllegalStateException if a definition with the same default ID
     *         has already been collected
     */
    @Override
    public void register() {
        MutableReciperegistrablesHolder.INSTANCE.add(this);
    }

    /**
     * Retains a named Minecraft criterion for the concrete recipe builder.
     *
     * @param name      the unique criterion name
     * @param criterion the criterion to emit
     * @return this definition as its concrete fluent type
     * @throws IllegalArgumentException if {@code name} is already present
     */
    @Override
    @SuppressWarnings("unchecked")
    public SUB_RR unlockedBy(
        @NotNull String name,
        @NotNull Criterion<?> criterion
    ) {
        Criterion<?> previous =
            criteria.putIfAbsent(name, criterion);

        if (previous != null) {
            throw new IllegalArgumentException(
                "Duplicate recipe criterion: " + name
            );
        }

        return (SUB_RR) this;
    }

    /**
     * Replaces the configured recipe-book group.
     *
     * @param group the group name, or {@code null} to clear it
     * @return this definition as its concrete fluent type
     */
    @SuppressWarnings("unchecked")
    @Override
    public SUB_RR group(@Nullable String group) {
        this.group = group;
        return (SUB_RR) this;
    }
}
