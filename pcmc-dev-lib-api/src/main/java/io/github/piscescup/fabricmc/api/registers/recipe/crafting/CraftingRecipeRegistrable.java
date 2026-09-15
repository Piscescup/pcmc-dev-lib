package io.github.piscescup.fabricmc.api.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/**
 * Adds crafting-specific inventory criteria to a fluent recipe definition.
 *
 * <p>In addition to arbitrary Minecraft criteria inherited from
 * {@link RecipeRegistrable}, crafting recipes can be unlocked when the player
 * inventory contains an item or an item from a tag. The overloads without a
 * count use a required count of one.</p>
 *
 * @param <SUB_C> the concrete fluent crafting-recipe type
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RecipeRegistrable
 */
public interface CraftingRecipeRegistrable<SUB_C extends CraftingRecipeRegistrable<SUB_C>>
    extends RecipeRegistrable<SUB_C>
{
    /**
     * Adds an inventory criterion for an exact number of the selected item.
     *
     * @param name  the criterion name written to the recipe advancement
     * @param item  the item whose presence unlocks the recipe
     * @param count the exact item count required by the criterion
     * @return this crafting-recipe definition
     */
    SUB_C unlockedBy(String name, ItemLike item, int count);

    /**
     * Adds an inventory criterion requiring one of the selected item.
     *
     * @param name the criterion name written to the recipe advancement
     * @param item the item whose presence unlocks the recipe
     * @return this crafting-recipe definition
     * @see #unlockedBy(String, ItemLike, int)
     */
    default SUB_C unlockedBy(String name, ItemLike item) {
        return unlockedBy(name, item, 1);
    }

    /**
     * Adds an inventory criterion for items belonging to the selected tag.
     *
     * @param name  the criterion name written to the recipe advancement
     * @param tag   the item tag tested against the player's inventory
     * @param count the requested number of matching items
     * @return this crafting-recipe definition
     */
    SUB_C unlockedBy(String name, TagKey<Item> tag, int count);

    /**
     * Adds an inventory criterion requiring an item from the selected tag.
     *
     * @param name the criterion name written to the recipe advancement
     * @param tag  the item tag tested against the player's inventory
     * @return this crafting-recipe definition
     * @see #unlockedBy(String, TagKey, int)
     */
    default SUB_C unlockedBy(String name, TagKey<Item> tag) {
        return unlockedBy(name, tag, 1);
    }
}
