package io.github.piscescup.fabricmc.store.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides read-only {@link TagKeyCollector tag declarations} grouped by target
 * {@link Registry}.
 *
 * <p>Consumers can use this view to discover every declared tag for a registry,
 * including tags that extend existing vanilla tags.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ReadableTagKeysHolder {

    /**
     * Returns the {@link TagKeyCollector tag declarations} for a target
     * {@link Registry}.
     *
     * @param registryKey the target {@link Registry} key
     * @param <T> the registry value type
     * @return an unmodifiable {@link List} of tag collectors, or an empty list when none exist
     */
    <T> @NotNull List<TagKeyCollector<T>> getTagCollectors(
        @NotNull ResourceKey<? extends Registry<T>> registryKey
    );
}
