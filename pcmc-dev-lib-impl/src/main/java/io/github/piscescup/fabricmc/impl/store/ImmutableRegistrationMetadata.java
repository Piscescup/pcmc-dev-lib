package io.github.piscescup.fabricmc.impl.store;

import io.github.piscescup.fabricmc.store.RegistrationMetadata;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * An immutable implementation of {@link RegistrationMetadata} containing
 * metadata about a registered entry.
 *
 * <p>The registry type {@code R} represents the base type accepted by the
 * target registry, while {@code T} represents the concrete type of the
 * registered entry.</p>
 *
 * @param identifier  the identifier assigned to the entry
 * @param resourceKey the resource key assigned to the entry
 * @param entry       the registered entry
 * @param <R>         the base type accepted by the target registry
 * @param <T>         the concrete type of the registered entry
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public record ImmutableRegistrationMetadata<R, T extends R>(
    Identifier identifier,
    ResourceKey<R> resourceKey,
    T entry
)
    implements RegistrationMetadata<R, T>
{}
