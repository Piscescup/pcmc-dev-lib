package io.github.piscescup.fabricmc.api.recipe.crafting;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/**
 * Configures a shaped crafting recipe for data generation.
 *
 * <p>Build a recipe by appending pattern rows and assigning every non-space
 * symbol to an {@link Ingredient}, {@link ItemLike}, or item {@link TagKey}.
 * Pattern and symbol validation is completed by the Minecraft recipe builder
 * when the definition is saved.</p>
 *
 * <p>Typical configuration:</p>
 * <pre>{@code
 * factory.shaped("compressed_iron", RecipeCategory.BUILDING_BLOCKS, result)
 *     .pattern("III")
 *     .pattern("III")
 *     .pattern("III")
 *     .define('I', Items.IRON_INGOT)
 *     .unlockedBy("has_iron", Items.IRON_INGOT)
 *     .register();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ShapelessCraftingRecipeRegistrable
 */
public interface ShapedCraftingRecipeRegistrable
    extends CraftingRecipeRegistrable<ShapedCraftingRecipeRegistrable>
{
     /**
      * Controls whether unlocking this recipe displays a notification.
      *
      * @param showNotification {@code true} to show the recipe-unlocked notification
      * @return this shaped-recipe definition
      */
     ShapedCraftingRecipeRegistrable showNotification(final boolean showNotification);

     /**
      * Defines a pattern symbol using the item form of an {@link ItemLike} value.
      *
      * @param symbol the non-space pattern symbol to define
      * @param item   the item or block accepted for the symbol
      * @return this shaped-recipe definition
      * @see #define(Character, Ingredient)
      */
     default ShapedCraftingRecipeRegistrable define(final Character symbol, final ItemLike item) {
          return this.define(symbol, Ingredient.of(item));
     }

     /**
      * Defines a pattern symbol using any item in a {@link TagKey}.
      *
      * @param symbol the non-space pattern symbol to define
      * @param tag    the item tag accepted for the symbol
      * @return this shaped-recipe definition
      * @throws IllegalArgumentException if the supplied implementation receives
      *         a space or an already defined symbol
      */
     ShapedCraftingRecipeRegistrable define(final Character symbol, final TagKey<Item> tag);

     /**
      * Defines a pattern symbol using an arbitrary {@link Ingredient}.
      *
      * @param symbol     the non-space pattern symbol to define
      * @param ingredient the ingredient accepted for the symbol
      * @return this shaped-recipe definition
      * @throws IllegalArgumentException if the supplied implementation receives
      *         a space or an already defined symbol
      */
     ShapedCraftingRecipeRegistrable define(final Character symbol, final Ingredient ingredient);

     /**
      * Appends one row to the recipe pattern.
      *
      * <p>Rows are retained in call order. Minecraft validates the final row
      * widths, dimensions, and symbol usage when the recipe is saved.</p>
      *
      * @param row the next pattern row, using spaces for empty slots
      * @return this shaped-recipe definition
      */
     ShapedCraftingRecipeRegistrable pattern(final String row);
}
