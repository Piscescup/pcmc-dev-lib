package io.github.piscescup.fabricmc.api.registers.tag;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPostRegistrable<T>
    extends PostRegistrable<TagKey<T>, TagKey<T>, TagKeyPostRegistrable<T>>
{
    ResourceKey<? extends Registry<T>> tagResourceKey();
}
