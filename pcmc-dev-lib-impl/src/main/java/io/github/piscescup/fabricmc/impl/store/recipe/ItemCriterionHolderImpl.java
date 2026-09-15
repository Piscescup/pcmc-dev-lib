package io.github.piscescup.fabricmc.impl.store.recipe;

import io.github.piscescup.fabricmc.store.recipe.ItemCriterionHolder;
import net.minecraft.world.level.ItemLike;

/**
 * Immutable item-based recipe criterion values.
 *
 * @param criterionName  the name written to the generated recipe advancement
 * @param criterionItem  the item tested by the inventory predicate
 * @param criterionCount the exact required item count
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ItemCriterionHolder
 */
public record ItemCriterionHolderImpl(
    String criterionName,
    ItemLike criterionItem,
    int criterionCount
)
    implements ItemCriterionHolder
{

}
