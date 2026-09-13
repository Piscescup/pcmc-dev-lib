package io.github.piscescup.fabricmc.store.tag;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author REN YuanTong
 * @since
 */
public interface TagKeyCollector<T> {
    @NotNull TagKey<T> tagKey();

    @NotNull Set<ResourceKey<T>> includeResourceKeys();

    @NotNull Set<TagKey<T>> includedTags();

    @NotNull Set<T> includeEntries();
}
