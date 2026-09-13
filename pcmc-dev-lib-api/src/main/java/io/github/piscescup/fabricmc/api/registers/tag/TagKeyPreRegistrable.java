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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPreRegistrable<T>
    extends PreRegistrable<TagKey<T>, TagKey<T>, TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>>
{
    TagKeyPreRegistrable<T> add(@NotNull Identifier id);

    TagKeyPreRegistrable<T> addEntry(@NotNull T entry);

    default TagKeyPreRegistrable<T> addEntries(@NotNull Collection<? extends T> entries) {
        NullCheck.requireNonNull(entries, "entries");
        entries.forEach(this::addEntry);
        return this;
    }

    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addEntries(@NotNull T... items) {
        return addEntries(Arrays.asList(items));
    }

    TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tagKey);

    default TagKeyPreRegistrable<T> addTags(@NotNull Collection<TagKey<T>> tags) {
        NullCheck.requireNonNull(tags,  "tags");
        tags.forEach(this::addTag);
        return this;
    }

    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addTags(@NotNull TagKey<T>... tags) {
        return addTags(Arrays.asList(tags));
    }

    TagKeyPreRegistrable<T> addRegistryKey(@NotNull ResourceKey<T> key);

    default TagKeyPreRegistrable<T> addRegistryKeys(@NotNull Collection<ResourceKey<T>> keys) {
        NullCheck.requireAllNonNull(keys, "keys");
        keys.forEach(this::addRegistryKey);
        return this;
    }

    @SuppressWarnings("unchecked")
    default TagKeyPreRegistrable<T> addRegistryKeys(@NotNull ResourceKey<T>... keys) {
        return addRegistryKeys(Arrays.asList(keys));
    }
}
