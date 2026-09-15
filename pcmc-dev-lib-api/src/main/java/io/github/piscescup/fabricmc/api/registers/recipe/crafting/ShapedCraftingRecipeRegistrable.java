package io.github.piscescup.fabricmc.api.registers.recipe.crafting;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ShapedCraftingRecipeRegistrable
    extends CraftingRecipeRegistrable<ShapedCraftingRecipeRegistrable>
{
     ShapedCraftingRecipeRegistrable showNotification(final boolean showNotification);

     default ShapedCraftingRecipeRegistrable define(final Character symbol, final ItemLike item) {
          return this.define(symbol, Ingredient.of(item));
     }

     ShapedCraftingRecipeRegistrable define(final Character symbol, final TagKey<Item> tag);

     ShapedCraftingRecipeRegistrable define(final Character symbol, final Ingredient ingredient);

     ShapedCraftingRecipeRegistrable pattern(final String row);


}
