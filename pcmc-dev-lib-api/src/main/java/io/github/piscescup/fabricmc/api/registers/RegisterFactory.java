package io.github.piscescup.fabricmc.api.registers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a registration factory scoped to one {@link Registry} and namespace.
 *
 * <p>The {@link Registry} determines the kind of values produced by the factory,
 * while the namespace is used by its registration paths. Specialized factories add the
 * options specific to items, blocks, creative mode tabs, or tags.</p>
 *
 * @param <R> the value type of the target registry
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface RegisterFactory<R> {
    /**
     * Returns the {@link ResourceKey} of the {@link Registry} targeted by this factory.
     *
     * @return the target registry key
     */
    @NotNull
    ResourceKey<? extends Registry<R>> registryKey();

    /**
     * Returns the namespace used for registration identifiers.
     *
     * @return the namespace, usually a mod ID
     */
    @NotNull
    String namespace();

}
