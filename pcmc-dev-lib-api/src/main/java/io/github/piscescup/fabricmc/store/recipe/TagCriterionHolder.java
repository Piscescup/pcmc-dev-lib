package io.github.piscescup.fabricmc.store.recipe;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Provides the values used to create a tag-based recipe unlock criterion.
 *
 * <p>The criterion name becomes an entry in the generated recipe advancement.
 * The item tag and count describe the requested inventory predicate associated
 * with that entry. Implementations are intended as immutable transfer objects
 * between a recipe definition and its data-generation builder.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ItemCriterionHolder
 */
public interface TagCriterionHolder {
    /**
     * Returns the criterion name written to the recipe advancement.
     *
     * @return the criterion name
     */
    String criterionName();

    /**
     * Returns the item tag tested by the inventory predicate.
     *
     * @return the required item tag
     */
    TagKey<Item> criterionTag();

    /**
     * Returns the requested number of matching items.
     *
     * @return the requested matching-item count
     */
    int criterionCount();
}
