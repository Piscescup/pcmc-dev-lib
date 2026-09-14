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

/**
 * Holds shared member declarations grouped by target {@link Registry} and {@link TagKey}.
 *
 * <p>Adding a member creates its {@link MutableTagKeyCollector} if necessary.
 * Later additions to the same tag reuse that collector. Identifier and
 * resource-key members are retained as optional references, nested tags as
 * optional tag references, and registered instances as required members.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * MutableTagKeysHolder.INSTANCE.addEntry(tag, Items.IRON_INGOT);
 * List<TagKeyCollector<Item>> collectors =
 *     MutableTagKeysHolder.INSTANCE.getTagCollectors(Registries.ITEM);
 * }</pre>
 *
 * <p>Public mutations and collector-list lookups are synchronized on this
 * singleton. Returned lists are snapshots, but the collectors expose live
 * member sets; iteration over those sets is not protected by the holder's
 * synchronization. Finish populating declarations before data generation reads them.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ReadableTagKeysHolder
 * @see MutableTagKeyCollector
 */
public enum MutableTagKeysHolder
    implements ReadableTagKeysHolder
{
    /**
     * The shared {@link MutableTagKeysHolder} used by tag registration stages.
     */
    INSTANCE;

    /**
     * Registry keys mapped to their {@link TagKey} collectors.
     * Both map levels preserve insertion order. Initially empty; member additions
     * create the registry map and {@link MutableTagKeyCollector} as needed.
     */
    private final Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, MutableTagKeyCollector<?>>> registryMap =
        new LinkedHashMap<>();

    /**
     * Records a {@link ResourceKey} as an optional member of the selected {@link TagKey}.
     *
     * <p>The key is checked against the tag's {@link Registry} before the
     * collector is created or updated. Duplicate keys are ignored by the collector.</p>
     *
     * @param tagKey   the {@link TagKey} receiving the member; must not be {@code null}
     * @param entryKey the member key from the tag's target {@link Registry}; must not be {@code null}
     * @param <T>      the {@link Registry} value type
     * @throws NullPointerException if {@code tagKey} or {@code entryKey} is {@code null}
     * @throws IllegalArgumentException if the member key targets a different registry
     */
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
     * Records an {@link Identifier} as an optional member of the selected {@link TagKey}.
     *
     * <p>The identifier is combined with the tag's registry key without
     * resolving a registered value at declaration time.</p>
     *
     * @param tagKey  the {@link TagKey} receiving the member; must not be {@code null}
     * @param entryId the namespaced member {@link Identifier}; must not be {@code null}
     * @param <T>     the {@link Registry} value type
     * @throws NullPointerException if {@code tagKey} or {@code entryId} is {@code null}
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
     * Records a registered instance as a required member of the selected {@link TagKey}.
     *
     * <p>The instance is retained directly. The supplied data provider resolves
     * it to a resource key by identity in the target registry during generation.</p>
     *
     * @param tagKey the {@link TagKey} receiving the member; must not be {@code null}
     * @param entry  the registered value to include; must not be {@code null}
     * @param <T>    the {@link Registry} value type
     * @throws NullPointerException if {@code tagKey} or {@code entry} is {@code null}
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
     * Records an optional nested {@link TagKey} reference in the selected tag.
     *
     * <p>The nested tag is checked against the receiving tag's {@link Registry}
     * before the collector is created or updated.</p>
     *
     * @param tagKey      the {@link TagKey} receiving the nested reference; must not be {@code null}
     * @param includedTag the tag to include from the same {@link Registry}; must not be {@code null}
     * @param <T>         the {@link Registry} value type
     * @throws NullPointerException if {@code tagKey} or {@code includedTag} is {@code null}
     * @throws IllegalArgumentException if the nested tag targets a different registry
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


    /**
     * Returns the current {@link TagKeyCollector} values for a {@link Registry} in first-addition order.
     *
     * <p>The list is an unmodifiable snapshot. Tags first populated after this
     * call do not appear in it, but existing collectors continue to expose live
     * member sets. A lookup for an unknown registry returns an empty list
     * without creating any collectors.</p>
     *
     * @param registryKey the target {@link Registry} key; must not be {@code null}
     * @param <T>         the {@link Registry} value type preserved by the collectors
     * @return an unmodifiable list of tag collectors, possibly empty
     * @throws NullPointerException if {@code registryKey} is {@code null}
     */
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

    /**
     * Finds or creates a {@link MutableTagKeyCollector} under the tag's registry and identity.
     *
     * <p>Called while a synchronized holder operation owns the instance monitor.</p>
     *
     * @param tagKey the {@link TagKey} whose collector is required; must not be {@code null}
     * @param <T>    the {@link Registry} value type
     * @return the existing or newly created collector
     * @throws NullPointerException if {@code tagKey} is {@code null}
     */
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

    /**
     * Validates that a {@link TagKey} targets the selected {@link Registry}.
     *
     * @param registryKey the expected {@link Registry} key; must not be {@code null}
     * @param tagKey      the {@link TagKey} to validate; must not be {@code null}
     * @param <T>         the {@link Registry} value type
     * @throws NullPointerException if {@code tagKey} or {@code registryKey} is {@code null}
     * @throws IllegalArgumentException if the tag targets a different registry
     */
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
