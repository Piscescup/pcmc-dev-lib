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
public interface ShapelessCraftingRecipeRegistrable
    extends CraftingRecipeRegistrable<ShapelessCraftingRecipeRegistrable>
{
    ShapelessCraftingRecipeRegistrable requires(final TagKey<Item> tag);

    default ShapelessCraftingRecipeRegistrable requires(final ItemLike item, final int count) {
        for (int i = 0; i < count; i++) {
            this.requires(Ingredient.of(item));
        }

        return this;
    }

    default ShapelessCraftingRecipeRegistrable requires(final ItemLike item) {
        return this.requires(item, 1);
    }

    ShapelessCraftingRecipeRegistrable requires(final Ingredient ingredient, final int count);

    default ShapelessCraftingRecipeRegistrable requires(final Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }
}
