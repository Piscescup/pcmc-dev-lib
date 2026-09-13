package io.github.piscescup.fabricmc.impl.store.tag;

import io.github.piscescup.fabricmc.store.tag.ReadableTagKeysHolder;
import io.github.piscescup.fabricmc.store.tag.TagKeyCollector;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public enum MutableTagKeysHolder
    implements ReadableTagKeysHolder
{
    INSTANCE;

    private final Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, MutableTagKeyCollector<?>>> registryMap =
        new LinkedHashMap<>();

    public synchronized <T> void addEntry(
        @NotNull TagKey<T> tagKey,
        @NotNull ResourceKey<T> entryKey
    ) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        NullCheck.requireNonNull(entryKey, "entryKey");

        if (!entryKey.isFor(tagKey.registry())) {
            throw new IllegalArgumentException(
                "Entry " + entryKey.identifier()
                    + " does not belong to registry "
                    + tagKey.registry().identifier()
            );
        }

        getOrCreate(tagKey).addResourceKey(entryKey);
    }

    /**
     * Adds an entry identifier to the specified tag.
     */
    public synchronized <T> void addEntry(
        @NotNull TagKey<T> tagKey,
        @NotNull Identifier entryId
    ) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        NullCheck.requireNonNull(entryId, "entryId");

        ResourceKey<T> entryKey = ResourceKey.create(
            tagKey.registry(),
            entryId
        );

        getOrCreate(tagKey).addResourceKey(entryKey);
    }

    /**
     * Adds a registered object to the specified tag.
     *
     * <p>The actual registry is required because a registry key alone
     * cannot resolve an object to its resource key.</p>
     */
    public synchronized <T> void addEntry(
        @NotNull TagKey<T> tagKey,
        @NotNull T entry
    ) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        NullCheck.requireNonNull(entry, "entry");


        getOrCreate(tagKey).addEntry(entry);
    }

    /**
     * Adds another tag to the specified tag.
     */
    public synchronized <T> void addTag(
        @NotNull TagKey<T> tagKey,
        @NotNull TagKey<T> includedTag
    ) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        NullCheck.requireNonNull(includedTag, "includedTag");

        if (!includedTag.isFor(tagKey.registry())) {
            throw new IllegalArgumentException(
                "Included tag " + includedTag.location()
                    + " does not belong to registry "
                    + tagKey.registry().identifier()
            );
        }

        getOrCreate(tagKey).addTag(includedTag);
    }


    @SuppressWarnings("unchecked")
    @Override
    public synchronized @NotNull <T> List<TagKeyCollector<T>> getTagCollectors(
        @NotNull ResourceKey<? extends Registry<T>> registryKey
    ) {
        NullCheck.requireNonNull(registryKey, "registryKey");

        Map<TagKey<?>, MutableTagKeyCollector<?>> collectors =
            this.registryMap.get(registryKey);

        if (collectors == null) {
            return List.of();
        }

        List<MutableTagKeyCollector<T>> result =
            new ArrayList<>(collectors.size());

        for (MutableTagKeyCollector<?> collector : collectors.values()) {
            result.add((MutableTagKeyCollector<T>) collector);
        }

        return List.copyOf(result);
    }

    @SuppressWarnings("unchecked")
    private <T> @NotNull MutableTagKeyCollector<T> getOrCreate(
        @NotNull TagKey<T> tagKey
    ) {
        Map<TagKey<?>, MutableTagKeyCollector<?>> collectors =
            this.registryMap.computeIfAbsent(
                tagKey.registry(),
                _ -> new LinkedHashMap<>()
            );

        return (MutableTagKeyCollector<T>) collectors.computeIfAbsent(
            tagKey,
            _ -> new MutableTagKeyCollector<>(tagKey)
        );
    }

    private static <T> void checkRegistry(
        @NotNull ResourceKey<? extends Registry<T>> registryKey,
        @NotNull TagKey<T> tagKey
    ) {
        if (!tagKey.isFor(registryKey)) {
            throw new IllegalArgumentException(
                "Tag " + tagKey.location()
                    + " does not belong to registry "
                    + registryKey.identifier()
            );
        }
    }
}