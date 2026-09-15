package io.github.piscescup.fabricmc.store.recipe;

import net.minecraft.world.level.ItemLike;

/**
 * Provides the values used to create an item-based recipe unlock criterion.
 *
 * <p>The criterion name becomes an entry in the generated recipe advancement.
 * The item and count describe the inventory predicate associated with that
 * entry. Implementations are intended as immutable transfer objects between a
 * recipe definition and its data-generation builder.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see TagCriterionHolder
 */
public interface ItemCriterionHolder {
    /**
     * Returns the criterion name written to the recipe advancement.
     *
     * @return the criterion name
     */
    String criterionName();

    /**
     * Returns the item tested by the inventory predicate.
     *
     * @return the required item-like value
     */
    ItemLike criterionItem();

    /**
     * Returns the exact item count requested by the criterion.
     *
     * @return the requested item count
     */
    int criterionCount();
}
