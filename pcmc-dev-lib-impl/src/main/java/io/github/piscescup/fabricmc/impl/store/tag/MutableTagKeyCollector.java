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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class MutableTagKeyCollector<T> implements TagKeyCollector<T> {
    private final TagKey<T> tagKey;

    private final Set<ResourceKey<T>> resourceKeys =
        new LinkedHashSet<>();

    private final Set<TagKey<T>> includedTags =
        new LinkedHashSet<>();

    private final Set<T> includeEntries =
        new LinkedHashSet<>();

    MutableTagKeyCollector(@NotNull TagKey<T> tagKey) {
        this.tagKey = NullCheck.requireNonNull(tagKey, "tagKey");
    }

    void addEntry(@NotNull T entry) {
        NullCheck.requireNonNull(entry, "entry");

        this.includeEntries.add(entry);
    }

    void addResourceKey(@NotNull ResourceKey<T> entryKey) {
        if (!entryKey.isFor(this.tagKey.registry())) {
            throw new IllegalArgumentException(
                "Entry " + entryKey.identifier()
                    + " belongs to a different registry"
            );
        }

        this.resourceKeys.add(entryKey);
    }

    void addTag(@NotNull TagKey<T> includedTag) {
        if (!includedTag.isFor(this.tagKey.registry())) {
            throw new IllegalArgumentException(
                "Tag " + includedTag.location()
                    + " belongs to a different registry"
            );
        }

        this.includedTags.add(includedTag);
    }

    @Override
    public @NotNull TagKey<T> tagKey() {
        return this.tagKey;
    }

    @Override
    public @NotNull Set<ResourceKey<T>> includeResourceKeys() {
        return Collections.unmodifiableSet(this.resourceKeys);
    }

    @Override
    public @NotNull Set<TagKey<T>> includedTags() {
        return Collections.unmodifiableSet(this.includedTags);
    }

    @Override
    public @NotNull Set<T> includeEntries() {
        return Collections.unmodifiableSet(this.includeEntries);
    }
}
