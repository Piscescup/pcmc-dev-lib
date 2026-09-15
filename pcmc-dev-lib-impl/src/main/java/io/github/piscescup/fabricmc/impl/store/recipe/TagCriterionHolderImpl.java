package io.github.piscescup.fabricmc.impl.store.recipe;

import io.github.piscescup.fabricmc.store.recipe.TagCriterionHolder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Immutable tag-based recipe criterion values.
 *
 * @param criterionName  the name written to the generated recipe advancement
 * @param criterionTag   the item tag tested by the inventory predicate
 * @param criterionCount the requested number of matching items
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see TagCriterionHolder
 */
public record TagCriterionHolderImpl(
    String criterionName,
    TagKey<Item> criterionTag,
    int criterionCount
)
    implements TagCriterionHolder
{

}
