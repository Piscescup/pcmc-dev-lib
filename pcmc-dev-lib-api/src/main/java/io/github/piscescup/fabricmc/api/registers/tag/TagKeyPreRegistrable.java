package io.github.piscescup.fabricmc.api.registers.tag;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * Configures the members of a {@link TagKey} before completing its declaration.
 *
 * <p>Members may be supplied as registered values, {@link Identifier identifiers},
 * {@link ResourceKey resource keys}, or nested {@link TagKey tags}. The collected
 * membership can then be used for tag data generation. The supplied data provider
 * emits registered values as required members, and identifiers, resource keys,
 * and nested tags as optional members that may be absent when data is loaded.</p>
 *
 * <p>Typical usage, including an extension of an existing vanilla tag:</p>
 * <pre>{@code
 * public static final TagKey<Item> TEST_ITEM_TAG = ITEM_TAG_KEYS.path("test1")
 *     .addEntries(Items.PAPER, Items.GOLD_INGOT, TestItems.CUSTOM_ITEM)
 *     .register()
 *     .get();
 *
 * public static final TagKey<Block> NEEDS_IRON_TOOL =
 *     TagKeyRegisterFactory.ofVanilla(BlockTags.NEEDS_IRON_TOOL)
 *         .addEntries(TestBlocks.CUSTOM_BLOCK, TestBlocks.TEST_BLOCK1)
 *         .register()
 *         .get();
 * }</pre>
 *
 * <p>For tags, {@link #register()} completes the declaration and returns the
 * matching {@link TagKeyPostRegistrable post-registration stage}. The supplied
 * implementation creates the tag key when the stage is created and collects
 * members as they are added. Registration therefore marks the stage transition;
 * generating and loading tag data is a separate operation.</p>
 *
 * <p>Members are stored in separate sets for registered values, resource keys,
 * and nested tags. Repeated members are deduplicated within each set. Complete
 * the declaration before constructing data providers, then use the returned
 * post-registration stage for translations and access to the tag key.</p>
 *
 * @param <T> the value type of the registry targeted by the tag,
 *            such as {@code Item} or {@code Block}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 * @see TagKeyPostRegistrable
 * @see io.github.piscescup.fabricmc.store.tag.TagKeyCollector
 */
public interface TagKeyPreRegistrable<T>
    extends PreRegistrable<TagKey<T>, TagKey<T>, TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>>
{
    /**
     * Adds an optional tag member by {@link Identifier}.
     *
     * <p>The supplied implementation combines the identifier with this tag's
     * target registry to form a resource key. The member need not exist at
     * declaration time and is emitted as optional by the supplied data provider.</p>
     *
     * @param id the namespaced identifier of the member
     * @return this pre-registration stage
     * @see #addRegistryKey(ResourceKey)
     */
    TagKeyPreRegistrable<T> add(@NotNull Identifier id);

    /**
     * Adds a registered value as a required tag member.
     *
     * <p>The supplied data provider resolves the value to a resource key using
     * the tag's target registry. Supply an instance registered in that registry;
     * repeated additions of the same value are deduplicated.</p>
     *
     * @param entry the registered value to include
     * @return this pre-registration stage
     * @see #add(Identifier)
     */
    TagKeyPreRegistrable<T> addEntry(@NotNull T entry);

    /**
     * Adds a collection of registered values as required tag members.
     *
     * <p>Traverses the collection immediately and delegates each value to
     * {@link #addEntry(Object)}. Later structural changes to the collection
     * do not change the declaration.</p>
     *
     * @param entries the registered values to include in iteration order
     * @return this pre-registration stage
     */
    default TagKeyPreRegistrable<T> addEntries(@NotNull Collection<? extends T> entries) {
        NullCheck.requireNonNull(entries, "entries");
        entries.forEach(this::addEntry);
        return this;
    }

    /**
     * Adds registered values as required tag members in argument order.
     *
     * <p>Delegates to {@link #addEntries(Collection)}.</p>
     *
     * @param items the values to include
     * @return this pre-registration stage
     */
    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addEntries(@NotNull T... items) {
        return addEntries(Arrays.asList(items));
    }

    /**
     * Includes another {@link TagKey} from the same registry as an optional
     * nested tag.
     *
     * <p>The supplied data provider emits a reference to the included tag,
     * allowing its members to be resolved when tag data is loaded. A missing
     * optional tag does not make the generated reference required.</p>
     *
     * @param tagKey the tag to include
     * @return this pre-registration stage
     * @throws IllegalArgumentException if the supplied implementation receives
     *                                  a tag from a different registry
     */
    TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tagKey);

    /**
     * Includes a collection of {@link TagKey tags} as optional nested tags.
     *
     * <p>Traverses the collection immediately in iteration order and delegates
     * each tag to {@link #addTag(TagKey)}. Every tag must target the same registry
     * as this declaration.</p>
     *
     * @param tags the tags to include from this declaration's registry
     * @return this pre-registration stage
     */
    default TagKeyPreRegistrable<T> addTags(@NotNull Collection<TagKey<T>> tags) {
        NullCheck.requireNonNull(tags,  "tags");
        tags.forEach(this::addTag);
        return this;
    }

    /**
     * Includes {@link TagKey tags} as optional nested tags in argument order.
     *
     * <p>Delegates to {@link #addTags(Collection)}.</p>
     *
     * @param tags the tags to include from this declaration's registry
     * @return this pre-registration stage
     */
    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addTags(@NotNull TagKey<T>... tags) {
        return addTags(Arrays.asList(tags));
    }

    /**
     * Adds an optional tag member by {@link ResourceKey}.
     *
     * <p>The key must belong to this tag's target registry. The supplied data
     * provider emits it as an optional member without requiring a registered
     * value when the declaration is configured.</p>
     *
     * @param key a member key from the tag's registry
     * @return this pre-registration stage
     * @throws IllegalArgumentException if the supplied implementation receives
     *                                  a key from a different registry
     * @see #add(Identifier)
     */
    TagKeyPreRegistrable<T> addRegistryKey(@NotNull ResourceKey<T> key);

    /**
     * Adds a collection of optional tag members by {@link ResourceKey}.
     *
     * <p>Validates that the collection and its elements are non-null, then
     * delegates each key to {@link #addRegistryKey(ResourceKey)} in iteration
     * order. All keys must target this tag's registry.</p>
     *
     * @param keys member keys from the tag's registry
     * @return this pre-registration stage
     */
    default TagKeyPreRegistrable<T> addRegistryKeys(@NotNull Collection<ResourceKey<T>> keys) {
        NullCheck.requireAllNonNull(keys, "keys");
        keys.forEach(this::addRegistryKey);
        return this;
    }

    /**
     * Adds optional tag members by {@link ResourceKey} in argument order.
     *
     * <p>Delegates to {@link #addRegistryKeys(Collection)}.</p>
     *
     * @param keys member keys from the tag's registry
     * @return this pre-registration stage
     */
    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addRegistryKeys(@NotNull ResourceKey<T>... keys) {
        return addRegistryKeys(Arrays.asList(keys));
    }
}
