package io.github.piscescup.fabricmc.impl.registers;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 *
 * @author REN YuanTong
 * @since
 */
public abstract class Register<
    V, T extends V,
    PRE extends PreRegistrable<V, T, PRE, POST>,
    POST extends PostRegistrable<V, T, POST>>
    implements PreRegistrable<V, T, PRE, POST>, PostRegistrable<V, T, POST>
{
    protected final String namespace;

    protected Identifier id;

    protected ResourceKey<V> resourceKey;

    protected T thingToBeRegistered;

    protected final ResourceKey<? extends Registry<V>> resourceKeyCategory;

    protected final Registry<V> registerRegistryCategory;

    protected Register(
        final ResourceKey<? extends Registry<V>> resourceKeyCategory, Registry<V> registerRegistryCategory,
        @NotNull String namespace
    ) {
        NullCheck.requireNonNull(resourceKeyCategory, "resourceKeyCategory");
        NullCheck.requireNonNull(namespace, "namespace");
        NullCheck.requireNonNull(registerRegistryCategory, "registerRegistryCategory");
        this.resourceKeyCategory = resourceKeyCategory;
        this.namespace = namespace;
        this.registerRegistryCategory = registerRegistryCategory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull PRE path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        this.id = Identifier.fromNamespaceAndPath(namespace, path);
        this.resourceKey = ResourceKey.create(resourceKeyCategory, id);
        return (PRE) this;
    }

    @Override
    public @NotNull ResourceKey<V> resourceKey() {
        return null;
    }

    @Override
    public @NotNull Identifier identifier() {
        return id;
    }

    @Override
    public @NonNull T get() {
        return thingToBeRegistered;
    }
}
