package io.github.piscescup.fabricmc.datagen.tag;


import io.github.piscescup.fabricmc.store.tag.ReadableTagKeysHolder;
import io.github.piscescup.fabricmc.store.tag.TagKeyCollector;
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
 * Generates {@link Registry}-specific tag data from {@link TagKeyCollector} declarations.
 *
 * <p>For each {@link TagKeyCollector}, resource keys are emitted as optional
 * members and nested tags as optional tag references. Registered-value members
 * are resolved by object identity in the target registry and emitted as required
 * members. Those instances must be present in the supplied registry lookup.</p>
 *
 * <p>The collector list is obtained once, when the provider is constructed.
 * With the supplied shared holder, this is a snapshot of the known tags, while
 * the member sets of existing collectors remain live. Complete tag declarations
 * before constructing the provider so that all intended tags are included.</p>
 *
 * <p>Typical usage when adding a provider directly to a data pack:</p>
 * <pre>{@code
 * pack.addProvider((output, lookup) -> new TagProvider<>(
 *     output, Registries.ITEM, lookup, tagKeysHolder
 * ));
 * }</pre>
 *
 * @param <T> the value type of the {@link Registry} whose tags are generated
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ReadableTagKeysHolder
 * @see TagKeyCollector
 * @see io.github.piscescup.fabricmc.datagen.DatagenCollectors
 */
public class TagProvider<T> extends FabricTagsProvider<T> {
    /**
     * The {@link TagKeyCollector} list obtained from the holder at construction time.
     * Retained as-is and reused by {@link #addTags(HolderLookup.Provider)};
     * the shared holder returns a list snapshot containing collectors with live member sets.
     */
    private final List<TagKeyCollector<T>> collectors;

    /**
     * Creates a {@link TagProvider} and obtains declarations for its target {@link Registry}.
     *
     * <p>The collector list returned by the holder is retained for generation.
     * No second holder lookup is performed when {@link #addTags(HolderLookup.Provider)} runs.</p>
     *
     * @param output               the {@link FabricPackOutput} used for tag data; must not be {@code null}
     * @param resourceKey          the target {@link Registry} key; must not be {@code null}
     * @param registryLookupFuture the future {@link HolderLookup.Provider} containing the target registry; must not be {@code null}
     * @param tagKeysHolder        the {@link ReadableTagKeysHolder} supplying declarations; must not be {@code null}
     * @throws NullPointerException if {@code tagKeysHolder} is {@code null}
     */
    public TagProvider(
        FabricPackOutput output,
        ResourceKey<? extends Registry<T>> resourceKey,
        CompletableFuture<HolderLookup.Provider> registryLookupFuture,
        ReadableTagKeysHolder tagKeysHolder
    ) {
        super(output, resourceKey, registryLookupFuture);
        this.collectors = tagKeysHolder.getTagCollectors(resourceKey);
    }


    /**
     * Populates {@link TagAppender} builders from the retained declarations and registry lookup.
     *
     * <p>First indexes registered values by identity to obtain their resource
     * keys. For each collector, adds optional resource keys, optional nested
     * tags, and then required registered-value members. Order within each
     * category follows the collector's set iteration order.</p>
     *
     * <p>The lookup must contain the target registry, and every registered-value
     * member must be the actual instance found in that registry. Missing values
     * cannot be resolved into valid required member keys.</p>
     *
     * @param registries the {@link HolderLookup.Provider} resolving registered instances; must not be {@code null}
     * @throws NullPointerException if {@code registries} is {@code null}
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
