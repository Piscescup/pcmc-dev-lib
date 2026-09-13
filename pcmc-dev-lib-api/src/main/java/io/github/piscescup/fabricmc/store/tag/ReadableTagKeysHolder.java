package io.github.piscescup.fabricmc.store.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 * @author REN YuanTong
 * @since
 */
public interface ReadableTagKeysHolder {

    <T> @NotNull List<TagKeyCollector<T>> getTagCollectors(
        @NotNull ResourceKey<? extends Registry<T>> registryKey
    );
}
