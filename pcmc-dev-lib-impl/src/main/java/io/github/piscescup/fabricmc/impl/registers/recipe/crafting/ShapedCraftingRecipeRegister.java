package io.github.piscescup.fabricmc.impl.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.registers.recipe.crafting.ShapedCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.impl.registers.recipe.RecipeRegister;
import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ShapedCraftingRecipeRegister
    extends RecipeRegister<ShapedCraftingRecipeRegistrable>
    implements ShapedCraftingRecipeRegistrable
{
    private final ResourceKey<Recipe<?>> id;
    private final RecipeCategory category;
    private final ItemLike result;
    private final int resultCount;

    private final List<String> patterns =
        new ArrayList<>();

    private final Map<Character, IngredientDefinition> ingredients =
        new LinkedHashMap<>();

    private final Map<String, Criterion<?>> criteria =
        new LinkedHashMap<>();

    private String group;
    private boolean showNotification = true;

    ShapedCraftingRecipeRegister(
        ResourceKey<Recipe<?>> id,
        RecipeCategory category,
        ItemLike result,
        int resultCount
    ) {
        this.id = NullCheck.requireNonNull(id);
        this.category = NullCheck.requireNonNull(category);
        this.result = NullCheck.requireNonNull(result);

        if (resultCount <= 0) {
            throw new IllegalArgumentException(
                "Result count must be positive"
            );
        }

        this.resultCount = resultCount;
    }

    @Override
    public ShapedCraftingRecipeRegistrable showNotification(
        boolean showNotification
    ) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public ShapedCraftingRecipeRegistrable define(
        Character symbol,
        TagKey<Item> tag
    ) {
        NullCheck.requireNonNull(symbol);
        NullCheck.requireNonNull(tag);

        addIngredient(
            symbol,
            (builder, key) -> builder.define(key, tag)
        );

        return this;
    }

    @Override
    public ShapedCraftingRecipeRegistrable define(
        Character symbol,
        Ingredient ingredient
    ) {
        NullCheck.requireNonNull(symbol);
        NullCheck.requireNonNull(ingredient);

        addIngredient(
            symbol,
            (builder, key) -> builder.define(
                key,
                ingredient
            )
        );

        return this;
    }

    @Override
    public ShapedCraftingRecipeRegistrable pattern(
        String row
    ) {
        patterns.add(NullCheck.requireNonNull(row));
        return this;
    }

    @Override
    public ShapedCraftingRecipeRegistrable unlockedBy(
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

        return this;
    }

    @Override
    public ShapedCraftingRecipeRegistrable group(
        @Nullable String group
    ) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return id;
    }

    @Override
    public void save(
        @NotNull RecipeGenerationContext context,
        @NotNull ResourceKey<Recipe<?>> location
    ) {
        ShapedRecipeBuilder builder =
            ShapedRecipeBuilder.shaped(
                context.holderGetter(Registries.ITEM),
                category,
                result,
                resultCount
            );

        for (String pattern : patterns) {
            builder.pattern(pattern);
        }

        ingredients.forEach(
            (symbol, definition) ->
                definition.apply(builder, symbol)
        );

        criteria.forEach(builder::unlockedBy);

        builder
            .group(group)
            .showNotification(showNotification)
            .save(context.output(), location);
    }

    private void addIngredient(
        char symbol,
        IngredientDefinition definition
    ) {
        if (symbol == ' ') {
            throw new IllegalArgumentException(
                "Symbol ' ' is reserved and cannot be defined"
            );
        }

        IngredientDefinition previous =
            ingredients.putIfAbsent(symbol, definition);

        if (previous != null) {
            throw new IllegalArgumentException(
                "Symbol '" + symbol
                    + "' is already defined"
            );
        }
    }

    @FunctionalInterface
    private interface IngredientDefinition {

        void apply(
            ShapedRecipeBuilder builder,
            char symbol
        );
    }
}
