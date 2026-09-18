package io.github.piscescup.fabricmc.impl.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.recipe.crafting.ShapelessCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.impl.registers.recipe.RecipeRegister;
import io.github.piscescup.fabricmc.impl.store.recipe.ItemCriterionHolderImpl;
import io.github.piscescup.fabricmc.impl.store.recipe.TagCriterionHolderImpl;
import io.github.piscescup.fabricmc.store.recipe.ItemCriterionHolder;
import io.github.piscescup.fabricmc.store.recipe.RecipeGenerationContext;
import io.github.piscescup.fabricmc.store.recipe.TagCriterionHolder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static net.minecraft.data.recipes.RecipeProvider.inventoryTrigger;

/**
 * Mutable implementation of one shapeless crafting-recipe definition.
 *
 * <p>The definition retains deferred ingredient operations, unlock criteria,
 * result information, and recipe-book options. During data generation,
 * {@link #save(RecipeGenerationContext, ResourceKey)} transfers that state to a
 * {@link ShapelessRecipeBuilder} in deterministic insertion order.</p>
 *
 * <p>Instances are created by {@link CraftingRecipeRegisterFactory}. Complete
 * their mutable configuration before registering or collecting them, and avoid
 * mutating a definition while a data-provider run is saving it.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ShapelessCraftingRecipeRegistrable
 */
public class ShapelessCraftingRecipeRegister
    extends RecipeRegister<ShapelessCraftingRecipeRegistrable>
    implements ShapelessCraftingRecipeRegistrable
{
    /** The default recipe resource key. */
    private final ResourceKey<Recipe<?>> id;
    /** The recipe-book category forwarded to the Minecraft builder. */
    private final RecipeCategory category;
    /** The result template forwarded to the Minecraft builder. */
    private final ItemStackTemplate result;

    /** Deferred ingredient operations retained in call order. */
    private final List<IngredientRequirement> requirements =
        new ArrayList<>();

    /** Item-based inventory criteria retained for generation. */
    private final Collection<ItemCriterionHolder> itemCriterionHolders =
        new ArrayList<>();

    /** Tag-based inventory criteria retained for generation. */
    private final Collection<TagCriterionHolder> tagCriterionHolders =
        new ArrayList<>();

    /** The recipe-book group, or {@code null} when no group is configured. */
    private String group;

    /**
     * Creates an empty shapeless-recipe definition.
     *
     * @param id       the default recipe resource key
     * @param category the recipe-book category
     * @param result   the result template
     * @throws NullPointerException if any argument is {@code null}
     */
    ShapelessCraftingRecipeRegister(
        @NotNull ResourceKey<Recipe<?>> id,
        @NotNull RecipeCategory category,
        @NotNull ItemStackTemplate result
    ) {
        this.id = Objects.requireNonNull(id);
        this.category = Objects.requireNonNull(category);
        this.result = Objects.requireNonNull(result);
    }

    /**
     * Appends one deferred tag ingredient.
     *
     * @param tag the item tag accepted by the ingredient
     * @return this recipe definition
     */
    @Override
    public ShapelessCraftingRecipeRegistrable requires(
        @NotNull TagKey<Item> tag
    ) {
        requirements.add(builder -> builder.requires(tag));
        return this;
    }

    /**
     * Appends a deferred ingredient operation with an explicit count.
     *
     * @param ingredient the ingredient to append
     * @param count      the positive number of required entries
     * @return this recipe definition
     * @throws NullPointerException if {@code ingredient} is {@code null}
     * @throws IllegalArgumentException if {@code count} is not positive
     */
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

    /**
     * Returns the resource key created by the recipe factory.
     *
     * @return the default recipe resource key
     */
    @Override
    public @NotNull ResourceKey<Recipe<?>> defaultId() {
        return id;
    }

    /**
     * Builds and submits the shapeless recipe under an explicit resource key.
     *
     * <p>Ingredient operations are applied first, followed by arbitrary
     * criteria and inventory-derived criteria. The recipe-book group is applied
     * immediately before the builder is saved.</p>
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
        ShapelessRecipeBuilder builder =
            ShapelessRecipeBuilder.shapeless(
                itemHolderGetter,
                category,
                result
            );

        for (IngredientRequirement requirement : requirements) {
            requirement.apply(builder);
        }

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
            .save(context.output(), location);
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
    public ShapelessCraftingRecipeRegistrable unlockedBy(String name, ItemLike item, int count) {
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
    public ShapelessCraftingRecipeRegistrable unlockedBy(String name, TagKey<Item> tag, int count) {
        this.tagCriterionHolders.add(
            new TagCriterionHolderImpl(name, tag, count)
        );
        return this;
    }

    @FunctionalInterface
    private interface IngredientRequirement {

        /**
         * Applies this deferred ingredient operation to a shapeless recipe builder.
         *
         * @param builder the builder receiving the ingredient
         */
        void apply(
            @NotNull ShapelessRecipeBuilder builder
        );
    }
}
