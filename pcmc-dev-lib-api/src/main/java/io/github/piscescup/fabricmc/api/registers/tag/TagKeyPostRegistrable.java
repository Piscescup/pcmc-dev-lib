package io.github.piscescup.fabricmc.api.registers.tag;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * Provides the operations available after a tag key declaration is complete.
 *
 * <p>{@link #get()} returns the {@link TagKey} and {@link #identifier()} returns
 * its namespaced {@link net.minecraft.resources.Identifier Identifier}. Use
 * {@link #tagResourceKey()} when the {@link Registry} tagged by this key is
 * required; {@link #resourceKey()} is not applicable to {@link TagKey tag keys}.</p>
 *
 * @param <T> the value type of the registry targeted by the tag
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPostRegistrable<T>
    extends PostRegistrable<TagKey<T>, TagKey<T>, TagKeyPostRegistrable<T>>
{
    /**
     * Returns the {@link Registry} whose values may belong to this {@link TagKey}.
     *
     * @return the target registry key
     */
    ResourceKey<? extends Registry<T>> tagResourceKey();
}
