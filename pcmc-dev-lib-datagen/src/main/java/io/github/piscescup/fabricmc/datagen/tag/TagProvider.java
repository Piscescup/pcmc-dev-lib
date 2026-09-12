package io.github.piscescup.fabricmc.datagen.tag;

import io.github.piscescup.fabricmc.impl.store.tag.TagKeyCollector;
import io.github.piscescup.fabricmc.impl.store.tag.TagKeysHolder;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagProvider<T> extends FabricTagsProvider<T> {
    private final List<TagKeyCollector<T>> collectors;

    /**
     * Constructs a new {@link FabricTagsProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output               the {@link FabricPackOutput} instance
     * @param registryKey
     * @param registryLookupFuture the backing registry for the tag type
     */
    public TagProvider(FabricPackOutput output, ResourceKey<? extends Registry<T>> resourceKey, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, resourceKey, registryLookupFuture);
        this.collectors = TagKeysHolder.INSTANCE.getTagCollectors(resourceKey);
    }


    /**
     * Implement this method and then use {@link FabricTagsProvider#builder} to get and register new tag builders.
     *
     * @param registries
     */
    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        Stream<Holder.Reference<T>> elements = registries.lookupOrThrow(this.registryKey)
            .listElements();

        final Map<T, ResourceKey<T>> keysByEntry =
            new IdentityHashMap<>();

        elements.forEach(reference ->
            keysByEntry.put(
                reference.value(),
                reference.key()
            )
        );

        for (TagKeyCollector<T> collector : collectors) {
            TagKey<T> tagBuilt = collector.tagKey();

            Set<ResourceKey<T>> includedResourceKeys = collector.includeResourceKeys();
            Set<TagKey<T>> includeTags = collector.includedTags();
            Set<T> includedEntries = collector.includeEntries();

            TagAppender<T> builder = builder(tagBuilt);

            includedResourceKeys.forEach(builder::addOptional);
            includeTags.forEach(builder::addOptionalTag);

            for (T includedEntry : includedEntries) {
                builder.add(keysByEntry.get(includedEntry));
            }
        }
    }
}
