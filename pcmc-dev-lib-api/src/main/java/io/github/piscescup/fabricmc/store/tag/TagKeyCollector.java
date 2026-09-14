package io.github.piscescup.fabricmc.store.tag;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Provides a read-only description of the members declared for one {@link TagKey}.
 *
 * <p>{@link ResourceKey} members, nested {@link TagKey tags}, and registered-value
 * members remain available as separate {@link Set sets}. The supplied data
 * provider emits resource keys and nested tags as optional references, while
 * registered values are resolved to required member keys in the target registry.</p>
 *
 * <p>Typical usage when populating a tag builder:</p>
 * <pre>{@code
 * collector.includeResourceKeys().forEach(builder::addOptional);
 * collector.includedTags().forEach(builder::addOptionalTag);
 * for (Item entry : collector.includeEntries()) {
 *     // Resolve the registered item to its key and add it as a required member.
 * }
 * }</pre>
 *
 * <p>The supplied implementation preserves insertion order within each set and
 * deduplicates repeated members in that set. Its unmodifiable sets are live
 * views, so later additions to the collector remain visible. No combined order
 * or deduplication across the three member categories is implied.</p>
 *
 * @param <T> the value type of the registry targeted by the tag,
 *            such as {@code Item} or {@code Block}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ReadableTagKeysHolder
 * @see io.github.piscescup.fabricmc.api.registers.tag.TagKeyPreRegistrable
 */
public interface TagKeyCollector<T> {
    /**
     * Returns the {@link TagKey} whose membership is described by this collector.
     *
     * <p>The key identifies the tag and its target registry. All members in
     * this collector are intended for that registry.</p>
     *
     * @return the target tag key
     */
    @NotNull TagKey<T> tagKey();

    /**
     * Returns members declared by {@link ResourceKey}.
     *
     * <p>This also includes members supplied as identifiers and converted to
     * resource keys. The supplied data provider emits these keys as optional
     * members, allowing the referenced entries to be absent when data is loaded.</p>
     *
     * @return an unmodifiable set of member resource keys, possibly empty
     */
    @NotNull Set<ResourceKey<T>> includeResourceKeys();

    /**
     * Returns {@link TagKey tags} included as nested members.
     *
     * <p>Each nested tag targets the same registry as {@link #tagKey()}. The
     * supplied data provider emits optional tag references and leaves member
     * expansion to tag loading.</p>
     *
     * @return an unmodifiable set of included tags, possibly empty
     */
    @NotNull Set<TagKey<T>> includedTags();

    /**
     * Returns members declared as registered values.
     *
     * <p>The supplied data provider resolves these instances to resource keys
     * in the target registry and emits them as required members.</p>
     *
     * @return an unmodifiable set of registered member values, possibly empty
     */
    @NotNull Set<T> includeEntries();
}
