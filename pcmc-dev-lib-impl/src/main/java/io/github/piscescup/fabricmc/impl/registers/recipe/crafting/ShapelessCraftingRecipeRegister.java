package io.github.piscescup.fabricmc.impl.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.registers.recipe.crafting.ShapelessCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.impl.registers.recipe.RecipeRegister;
import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ShapelessCraftingRecipeRegister
    extends RecipeRegister<ShapelessCraftingRecipeRegistrable>
    implements ShapelessCraftingRecipeRegistrable
{
    private final ResourceKey<Recipe<?>> id;
    private final RecipeCategory category;
    private final ItemStackTemplate result;

    private final List<IngredientRequirement> requirements =
        new ArrayList<>();

    private final Map<String, Criterion<?>> criteria =
        new LinkedHashMap<>();

    private String group;

    ShapelessCraftingRecipeRegister(
        @NotNull ResourceKey<Recipe<?>> id,
        @NotNull RecipeCategory category,
        @NotNull ItemStackTemplate result
    ) {
        this.id = Objects.requireNonNull(id);
        this.category = Objects.requireNonNull(category);
        this.result = Objects.requireNonNull(result);
    }

    @Override
    public ShapelessCraftingRecipeRegistrable requires(
        @NotNull TagKey<Item> tag
    ) {
        requirements.add(builder -> builder.requires(tag));
        return this;
    }

    @Override
    public ShapelessCraftingRecipeRegistrable requires(
        @NotNull Ingredient ingredient,
        int count
    ) {
        if (count <= 0) {
            throw new IllegalArgumentException(
                "Ingredient count must be positive"
            );
        }

        Objects.requireNonNull(ingredient);

        requirements.add(
            builder -> builder.requires(ingredient, count)
        );

        return this;
    }

    @Override
    public ShapelessCraftingRecipeRegistrable unlockedBy(
        @NotNull String name,
        @NotNull Criterion<?> criterion
    ) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(criterion);

        Criterion<?> previous =
            criteria.putIfAbsent(name, criterion);

        if (previous != null) {
            throw new IllegalArgumentException(
                "Duplicate recipe criterion: " + name
            );
        }

        return this;
    }

    @Override
    public ShapelessCraftingRecipeRegistrable group(
        @Nullable String group
    ) {
        this.group = group;
        return this;
    }

    @Override
    public @NotNull ResourceKey<Recipe<?>> defaultId() {
        return id;
    }

    @Override
    public void save(
        @NotNull RecipeGenerationContext context,
        @NotNull ResourceKey<Recipe<?>> location
    ) {
        ShapelessRecipeBuilder builder =
            ShapelessRecipeBuilder.shapeless(
                context.holderGetter(Registries.ITEM),
                category,
                result
            );

        for (IngredientRequirement requirement : requirements) {
            requirement.apply(builder);
        }

        criteria.forEach(builder::unlockedBy);

        builder
            .group(group)
            .save(context.output(), location);
    }

    @FunctionalInterface
    private interface IngredientRequirement {

        void apply(
            @NotNull ShapelessRecipeBuilder builder
        );
    }
}
