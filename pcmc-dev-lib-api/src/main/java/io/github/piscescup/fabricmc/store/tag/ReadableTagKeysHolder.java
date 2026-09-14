package io.github.piscescup.fabricmc.store.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides read-only {@link TagKeyCollector tag declarations} grouped by target
 * {@link Registry}.
 *
 * <p>Consumers can use this view to discover collected declarations for a
 * registry, including extensions of existing vanilla tags. Each
 * {@link TagKeyCollector} describes one tag and separates its resource-key
 * members, nested tags, and registered-value members.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * List<TagKeyCollector<Item>> collectors = holder.getTagCollectors(Registries.ITEM);
 * for (TagKeyCollector<Item> collector : collectors) {
 *     TagKey<Item> tag = collector.tagKey();
 *     Set<Item> entries = collector.includeEntries();
 *     // Forward the declaration to a tag data provider.
 * }
 * }</pre>
 *
 * <p>The supplied implementation creates collectors when members are added.
 * Creating a tag registration stage and calling {@code register()} without
 * adding members does not, by itself, create a collector. Configure declarations
 * before constructing a data provider that obtains its collector list here.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see TagKeyCollector
 */
public interface ReadableTagKeysHolder {

    /**
     * Returns the {@link TagKeyCollector tag declarations} for a target
     * {@link Registry}.
     *
     * <p>The supplied implementation returns an unmodifiable snapshot of the
     * collector list in first-declaration order. Collectors added later do not
     * appear in that list, but each existing collector's member sets are live
     * views and may reflect subsequent additions.</p>
     *
     * @param registryKey the target {@link Registry} key
     * @param <T>         the registry value type, preserved by the returned collectors
     * @return an unmodifiable {@link List} of tag collectors, or an empty list when none exist
     * @see TagKeyCollector
     */
    <T> @NotNull List<TagKeyCollector<T>> getTagCollectors(
        @NotNull ResourceKey<? extends Registry<T>> registryKey
    );
}
