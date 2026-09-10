package io.github.piscescup.fabricmc.impl.registers;

import io.github.piscescup.fabricmc.api.registers.RegisterFactory;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class RegisterFactoryImpl<R> implements RegisterFactory<R> {
    protected final String namespace;

    protected final ResourceKey<? extends Registry<R>> resourceKey;

    protected RegisterFactoryImpl(
        final ResourceKey<? extends Registry<R>> resourceKey,
        String namespace
    ) {
        NullCheck.requireNonNull(resourceKey, "resourceKey");
        NullCheck.requireNonNull(namespace, "namespace");

        this.resourceKey = resourceKey;
        this.namespace = namespace;
    }

    @Override
    public @NotNull ResourceKey<? extends Registry<R>> registryKey() {
        return this.resourceKey;
    }

    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

}
