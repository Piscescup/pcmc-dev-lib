package io.github.piscescup.fabricmc.api.tag;

import io.github.piscescup.fabricmc.api.PostRegistrable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * Provides the operations available after a tag key declaration is complete.
 *
 * <p>This stage is returned by {@link TagKeyPreRegistrable#register()} after
 * the tag declaration is complete. The inherited methods expose the declared
 * {@link TagKey}, its namespaced
 * {@link net.minecraft.resources.Identifier Identifier}, translation support,
 * and collection of the tag key itself. Tag membership is materialized by
 * data generation and data loading.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * TagKeyPostRegistrable<Item> declared = ITEM_TAG_KEYS.path("test_items")
 *     .addEntries(Items.PAPER, Items.GOLD_INGOT)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Test Items");
 * TagKey<Item> tag = declared.get();
 * ResourceKey<? extends Registry<Item>> registry = declared.tagResourceKey();
 * }</pre>
 *
 * <p>Use {@link #tagResourceKey()} to obtain the registry whose values the tag
 * groups. The inherited {@link #resourceKey()} would describe a registry entry
 * whose value is a tag key and is unsupported by the supplied implementation;
 * it throws {@link UnsupportedOperationException}. Translations are recorded
 * under {@link TagKey#getTranslationKey()} by that implementation.</p>
 *
 * @param <T> the value type of the registry targeted by the tag,
 *            such as {@code Item} or {@code Block}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see TagKeyPreRegistrable
 * @see PostRegistrable
 */
public interface TagKeyPostRegistrable<T>
    extends PostRegistrable<TagKey<T>, TagKey<T>, TagKeyPostRegistrable<T>>
{
    /**
     * Returns the key of the {@link Registry} whose values may belong to this tag.
     *
     * <p>For example, an item tag returns the item registry key. This identifies
     * the registry targeted by the tag, while {@link #identifier()} identifies
     * the tag within that registry's tag namespace.</p>
     *
     * @return the target registry key, typed to the tag's member type
     * @see #identifier()
     */
    ResourceKey<? extends Registry<T>> tagResourceKey();
}
