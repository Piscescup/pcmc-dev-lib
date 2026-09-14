package io.github.piscescup.fabricmc.impl.store.tag;

import io.github.piscescup.fabricmc.store.tag.TagKeyCollector;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Stores the member declarations for one {@link TagKey} in separate ordered sets.
 *
 * <p>{@link MutableTagKeysHolder} creates and populates collectors. Resource
 * keys, nested tags, and registered values are each retained in a
 * {@link LinkedHashSet}, which preserves insertion order and removes duplicates
 * within that category. No deduplication across categories is performed.</p>
 *
 * <p>The {@link TagKeyCollector} accessors expose unmodifiable live views for
 * data generation. Resource keys and nested tags are checked against the target
 * registry when added. Registered values are stored directly and resolved by
 * the data provider later. This collector does not synchronize access.</p>
 *
 * @param <T> the value type of the registry targeted by the tag
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see MutableTagKeysHolder
 * @see TagKeyCollector
 */
public class MutableTagKeyCollector<T> implements TagKeyCollector<T> {
    /**
     * The {@link TagKey} supplied at construction time. Its target registry is
     * used to validate resource-key and nested-tag additions.
     */
    private final TagKey<T> tagKey;

    /**
     * Optional {@link ResourceKey} members retained in insertion order.
     * Duplicate keys are ignored; {@link #includeResourceKeys()} exposes a live view.
     */
    private final Set<ResourceKey<T>> resourceKeys =
        new LinkedHashSet<>();

    /**
     * Optional nested {@link TagKey} references retained in insertion order.
     * Duplicate tags are ignored; {@link #includedTags()} exposes a live view.
     */
    private final Set<TagKey<T>> includedTags =
        new LinkedHashSet<>();

    /**
     * Required registered-value members retained in insertion order.
     * Values are resolved during data generation; {@link #includeEntries()} exposes a live view.
     */
    private final Set<T> includeEntries =
        new LinkedHashSet<>();

    /**
     * Creates an empty collector for a {@link TagKey}.
     *
     * @param tagKey the {@link TagKey} whose members are collected; must not be {@code null}
     * @throws NullPointerException if {@code tagKey} is {@code null}
     */
    MutableTagKeyCollector(@NotNull TagKey<T> tagKey) {
        this.tagKey = NullCheck.requireNonNull(tagKey, "tagKey");
    }

    /**
     * Retains a registered value as a required member for data generation.
     *
     * @param entry the value registered in the tag's target registry; must not be {@code null}
     * @throws NullPointerException if {@code entry} is {@code null}
     */
    void addEntry(@NotNull T entry) {
        NullCheck.requireNonNull(entry, "entry");

        this.includeEntries.add(entry);
    }

    /**
     * Retains a {@link ResourceKey} as an optional member for data generation.
     *
     * @param entryKey the {@link ResourceKey} from the tag's target registry; must not be {@code null}
     * @throws NullPointerException if {@code entryKey} is {@code null}
     * @throws IllegalArgumentException if the key targets a different registry
     */
    void addResourceKey(@NotNull ResourceKey<T> entryKey) {
        if (!entryKey.isFor(this.tagKey.registry())) {
            throw new IllegalArgumentException(
                "Entry " + entryKey.identifier()
                    + " belongs to a different registry"
            );
        }

        this.resourceKeys.add(entryKey);
    }

    /**
     * Retains an optional nested {@link TagKey} reference for data generation.
     *
     * @param includedTag the nested {@link TagKey} from the same target registry; must not be {@code null}
     * @throws NullPointerException if {@code includedTag} is {@code null}
     * @throws IllegalArgumentException if the nested tag targets a different registry
     */
    void addTag(@NotNull TagKey<T> includedTag) {
        if (!includedTag.isFor(this.tagKey.registry())) {
            throw new IllegalArgumentException(
                "Tag " + includedTag.location()
                    + " belongs to a different registry"
            );
        }

        this.includedTags.add(includedTag);
    }

    /**
     * Returns the {@link TagKey} whose membership is described by this collector.
     *
     * @return the target tag key
     */
    @Override
    public @NotNull TagKey<T> tagKey() {
        return this.tagKey;
    }

    /**
     * Returns an unmodifiable live {@link Set} of optional member {@link ResourceKey} values.
     *
     * @return the member keys in insertion order, possibly empty
     */
    @Override
    public @NotNull Set<ResourceKey<T>> includeResourceKeys() {
        return Collections.unmodifiableSet(this.resourceKeys);
    }

    /**
     * Returns an unmodifiable live {@link Set} of optional nested {@link TagKey} values.
     *
     * @return the nested tags in insertion order, possibly empty
     */
    @Override
    public @NotNull Set<TagKey<T>> includedTags() {
        return Collections.unmodifiableSet(this.includedTags);
    }

    /**
     * Returns an unmodifiable live {@link Set} of required registered-value members.
     *
     * @return the registered values in insertion order, possibly empty
     */
    @Override
    public @NotNull Set<T> includeEntries() {
        return Collections.unmodifiableSet(this.includeEntries);
    }
}
