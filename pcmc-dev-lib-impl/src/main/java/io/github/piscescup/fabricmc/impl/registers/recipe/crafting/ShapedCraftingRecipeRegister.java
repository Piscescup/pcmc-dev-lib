package io.github.piscescup.fabricmc.impl.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.recipe.crafting.ShapedCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.impl.registers.recipe.RecipeRegister;
import io.github.piscescup.fabricmc.impl.store.recipe.ItemCriterionHolderImpl;
import io.github.piscescup.fabricmc.impl.store.recipe.TagCriterionHolderImpl;
import io.github.piscescup.fabricmc.store.recipe.ItemCriterionHolder;
import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import io.github.piscescup.fabricmc.store.recipe.TagCriterionHolder;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.HolderGetter;
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

import java.util.*;

import static net.minecraft.data.recipes.RecipeProvider.inventoryTrigger;

/**
 * Mutable implementation of one shaped crafting-recipe definition.
 *
 * <p>The definition retains pattern rows, symbol mappings, unlock criteria,
 * recipe-book options, and result information. During data generation,
 * {@link #save(RecipeGenerationContext, ResourceKey)} transfers that state to a
 * {@link ShapedRecipeBuilder} in deterministic insertion order.</p>
 *
 * <p>Instances are created by {@link CraftingRecipeRegisterFactory}. Complete
 * their mutable configuration before registering or collecting them, and avoid
 * mutating a definition while a data-provider run is saving it.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ShapedCraftingRecipeRegistrable
 */
public class ShapedCraftingRecipeRegister
    extends RecipeRegister<ShapedCraftingRecipeRegistrable>
    implements ShapedCraftingRecipeRegistrable
{
    /** The default recipe resource key. */
    private final ResourceKey<Recipe<?>> id;
    /** The recipe-book category forwarded to the Minecraft builder. */
    private final RecipeCategory category;
    /** The item-like result forwarded to the Minecraft builder. */
    private final ItemLike result;
    /** The positive number of result items produced. */
    private final int resultCount;

    /** Pattern rows in the order supplied by {@link #pattern(String)}. */
    private final List<String> patterns =
        new ArrayList<>();

    /** Symbol definitions in first-definition order. */
    private final Map<Character, IngredientDefinition> ingredients =
        new LinkedHashMap<>();

    /** Item-based inventory criteria retained for generation. */
    private final Collection<ItemCriterionHolder> itemCriterionHolders =
        new ArrayList<>();

    /** Tag-based inventory criteria retained for generation. */
    private final Collection<TagCriterionHolder> tagCriterionHolders =
        new ArrayList<>();

    /** The recipe-book group, or {@code null} when no group is configured. */
    private String group;
    /** Whether Minecraft should show an unlock notification. */
    private boolean showNotification = true;

    /**
     * Creates an empty shaped-recipe definition.
     *
     * @param id          the default recipe resource key
     * @param category    the recipe-book category
     * @param result      the item or block produced by the recipe
     * @param resultCount the positive number of result items produced
     * @throws NullPointerException if {@code id}, {@code category}, or {@code result} is {@code null}
     * @throws IllegalArgumentException if {@code resultCount} is not positive
     */
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

    /**
     * Sets the unlock-notification option forwarded to the shaped recipe builder.
     *
     * @param showNotification {@code true} to show an unlock notification
     * @return this recipe definition
     */
    @Override
    public ShapedCraftingRecipeRegistrable showNotification(
        boolean showNotification
    ) {
        this.showNotification = showNotification;
        return this;
    }

    /**
     * Defines a symbol as any item belonging to a tag.
     *
     * @param symbol the non-space symbol to define
     * @param tag    the item tag accepted for the symbol
     * @return this recipe definition
     * @throws NullPointerException if {@code symbol} or {@code tag} is {@code null}
     * @throws IllegalArgumentException if the symbol is a space or is already defined
     */
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

    /**
     * Defines a symbol using an arbitrary ingredient.
     *
     * @param symbol     the non-space symbol to define
     * @param ingredient the ingredient accepted for the symbol
     * @return this recipe definition
     * @throws NullPointerException if {@code symbol} or {@code ingredient} is {@code null}
     * @throws IllegalArgumentException if the symbol is a space or is already defined
     */
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

    /**
     * Appends a pattern row for later application to the Minecraft builder.
     *
     * @param row the non-null pattern row
     * @return this recipe definition
     * @throws NullPointerException if {@code row} is {@code null}
     */
    @Override
    public ShapedCraftingRecipeRegistrable pattern(
        String row
    ) {
        patterns.add(NullCheck.requireNonNull(row));
        return this;
    }

    /**
     * Retains an item-based inventory criterion.
     *
     * @param name  the generated criterion name
     * @param item  the required item-like value
     * @param count the exact required count
     * @return this recipe definition
     */
    @Override
    public ShapedCraftingRecipeRegistrable unlockedBy(String name, ItemLike item, int count) {
        this.itemCriterionHolders.add(
            new ItemCriterionHolderImpl(name, item.asItem(), count)
        );
        return this;
    }

    /**
     * Retains a tag-based inventory criterion.
     *
     * @param name  the generated criterion name
     * @param tag   the required item tag
     * @param count the requested number of matching items
     * @return this recipe definition
     */
    @Override
    public ShapedCraftingRecipeRegistrable unlockedBy(String name, TagKey<Item> tag, int count) {
        this.tagCriterionHolders.add(
            new TagCriterionHolderImpl(name, tag, count)
        );
        return this;
    }

    /**
     * Replaces the recipe-book group forwarded during generation.
     *
     * @param group the group name, or {@code null} for no group
     * @return this recipe definition
     */
    @Override
    public ShapedCraftingRecipeRegistrable group(
        @Nullable String group
    ) {
        this.group = group;
        return this;
    }

    /**
     * Returns the resource key created by the recipe factory.
     *
     * @return the default recipe resource key
     */
    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return id;
    }

    /**
     * Builds and submits the shaped recipe under an explicit resource key.
     *
     * <p>Pattern rows and symbol definitions are applied first, followed by
     * arbitrary criteria and inventory-derived criteria. The group and
     * notification option are applied immediately before the builder is saved.</p>
     *
     * @param context  the active recipe-generation context
     * @param location the resource key under which the recipe is saved
     */
    @Override
    public void save(
        @NotNull RecipeGenerationContext context,
        @NotNull ResourceKey<Recipe<?>> location
    ) {
        HolderGetter<Item> itemHolderGetter = context.holderGetter(Registries.ITEM);
        ShapedRecipeBuilder builder =
            ShapedRecipeBuilder.shaped(
                itemHolderGetter,
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

        this.itemCriterionHolders.forEach(
            holder -> builder.unlockedBy(
                holder.criterionName(),
                inventoryTrigger(ItemPredicate.Builder.item()
                                     .of(itemHolderGetter, holder.criterionItem())
                                     .withCount(MinMaxBounds.Ints.exactly(holder.criterionCount()))
                )
            )
        );

        this.tagCriterionHolders.forEach(
            holder -> builder.unlockedBy(
                holder.criterionName(),
                inventoryTrigger(ItemPredicate.Builder.item().of(itemHolderGetter, holder.criterionTag()))
            )
        );

        builder
            .group(group)
            .showNotification(showNotification)
            .save(context.output(), location);
    }

    /**
     * Adds one validated symbol definition to the insertion-ordered map.
     *
     * @param symbol     the non-space pattern symbol
     * @param definition the deferred builder operation for the symbol
     * @throws IllegalArgumentException if {@code symbol} is a space or is already defined
     */
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

        /**
         * Applies this deferred symbol definition to a shaped recipe builder.
         *
         * @param builder the builder receiving the definition
         * @param symbol  the symbol associated with this definition
         */
        void apply(
            ShapedRecipeBuilder builder,
            char symbol
        );
    }
}
