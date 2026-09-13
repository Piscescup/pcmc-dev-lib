package io.github.piscescup.fabricmc.store.tag;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Provides a read-only description of the members declared for one {@link TagKey}.
 *
 * <p>{@link ResourceKey} members, nested {@link TagKey tags}, and registered-value
 * members remain available as separate {@link Set sets} so consumers can preserve
 * their tag semantics.</p>
 *
 * @param <T> the value type of the registry targeted by the tag
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyCollector<T> {
    /**
     * Returns the {@link TagKey} whose membership is described by this collector.
     *
     * @return the target tag key
     */
    @NotNull TagKey<T> tagKey();

    /**
     * Returns members declared by {@link ResourceKey}.
     *
     * @return an unmodifiable set of member resource keys
     */
    @NotNull Set<ResourceKey<T>> includeResourceKeys();

    /**
     * Returns {@link TagKey tags} included as nested members.
     *
     * @return an unmodifiable set of included tags
     */
    @NotNull Set<TagKey<T>> includedTags();

    /**
     * Returns members declared as registered values.
     *
     * @return an unmodifiable set of member values
     */
    @NotNull Set<T> includeEntries();
}
