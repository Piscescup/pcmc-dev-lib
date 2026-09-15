package io.github.piscescup.fabricmc.api.registers.recipe.crafting;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/**
 * Configures a shapeless crafting recipe for data generation.
 *
 * <p>Each {@code requires} call appends ingredients without assigning crafting
 * grid positions. Item counts are represented as repeated ingredient entries;
 * Minecraft validates the completed ingredient list when the recipe is saved.</p>
 *
 * <p>Typical configuration:</p>
 * <pre>{@code
 * factory.shapeless("wool_bundle", RecipeCategory.MISC, result)
 *     .requires(Items.STRING, 4)
 *     .requires(ItemTags.WOOL)
 *     .unlockedBy("has_string", Items.STRING)
 *     .register();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ShapedCraftingRecipeRegistrable
 */
public interface ShapelessCraftingRecipeRegistrable
    extends CraftingRecipeRegistrable<ShapelessCraftingRecipeRegistrable>
{
    /**
     * Appends one ingredient that accepts any item in the selected tag.
     *
     * @param tag the item tag accepted by the ingredient
     * @return this shapeless-recipe definition
     */
    ShapelessCraftingRecipeRegistrable requires(final TagKey<Item> tag);

    /**
     * Appends the item as {@code count} separate ingredient entries.
     *
     * <p>A non-positive count causes this default method to append no entries.</p>
     *
     * @param item  the item or block required by each entry
     * @param count the number of ingredient entries to append
     * @return this shapeless-recipe definition
     */
    default ShapelessCraftingRecipeRegistrable requires(final ItemLike item, final int count) {
        for (int i = 0; i < count; i++) {
            this.requires(Ingredient.of(item));
        }

        return this;
    }

    /**
     * Appends one ingredient for the selected item or block.
     *
     * @param item the item or block required by the recipe
     * @return this shapeless-recipe definition
     * @see #requires(ItemLike, int)
     */
    default ShapelessCraftingRecipeRegistrable requires(final ItemLike item) {
        return this.requires(item, 1);
    }

    /**
     * Appends an {@link Ingredient} the requested number of times.
     *
     * @param ingredient the ingredient to append
     * @param count      the positive number of ingredient entries to append
     * @return this shapeless-recipe definition
     * @throws IllegalArgumentException if {@code count} is not positive in the
     *         supplied implementation
     */
    ShapelessCraftingRecipeRegistrable requires(final Ingredient ingredient, final int count);

    /**
     * Appends one arbitrary {@link Ingredient}.
     *
     * @param ingredient the ingredient to append
     * @return this shapeless-recipe definition
     * @see #requires(Ingredient, int)
     */
    default ShapelessCraftingRecipeRegistrable requires(final Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }
}
