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
 * membership can then be used for tag data
 * generation. Existing vanilla tags can be extended through the same API:</p>
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
 * @param <T> the value type of the registry targeted by the tag
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPreRegistrable<T>
    extends PreRegistrable<TagKey<T>, TagKey<T>, TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>>
{
    /**
     * Adds an optional tag member by {@link Identifier}.
     *
     * @param id the namespaced identifier of the member
     * @return this pre-registration stage
     */
    TagKeyPreRegistrable<T> add(@NotNull Identifier id);

    /**
     * Adds a registered value as a tag member.
     *
     * @param entry the value to include
     * @return this pre-registration stage
     */
    TagKeyPreRegistrable<T> addEntry(@NotNull T entry);

    /**
     * Adds registered values as tag members.
     *
     * @param entries the values to include
     * @return this pre-registration stage
     */
    default TagKeyPreRegistrable<T> addEntries(@NotNull Collection<? extends T> entries) {
        NullCheck.requireNonNull(entries, "entries");
        entries.forEach(this::addEntry);
        return this;
    }

    /**
     * Adds registered values as tag members.
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
     * @param tagKey the tag to include
     * @return this pre-registration stage
     */
    TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tagKey);

    /**
     * Includes {@link TagKey tags} from the same registry as nested tags.
     *
     * @param tags the tags to include
     * @return this pre-registration stage
     */
    default TagKeyPreRegistrable<T> addTags(@NotNull Collection<TagKey<T>> tags) {
        NullCheck.requireNonNull(tags,  "tags");
        tags.forEach(this::addTag);
        return this;
    }

    /**
     * Includes {@link TagKey tags} from the same registry as nested tags.
     *
     * @param tags the tags to include
     * @return this pre-registration stage
     */
    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addTags(@NotNull TagKey<T>... tags) {
        return addTags(Arrays.asList(tags));
    }

    /**
     * Adds an optional tag member by {@link ResourceKey}.
     *
     * @param key a member key from the tag's registry
     * @return this pre-registration stage
     */
    TagKeyPreRegistrable<T> addRegistryKey(@NotNull ResourceKey<T> key);

    /**
     * Adds optional tag members by {@link ResourceKey}.
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
     * Adds optional tag members by {@link ResourceKey}.
     *
     * @param keys member keys from the tag's registry
     * @return this pre-registration stage
     */
    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addRegistryKeys(@NotNull ResourceKey<T>... keys) {
        return addRegistryKeys(Arrays.asList(keys));
    }
}
