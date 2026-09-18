package io.github.piscescup.fabricmc.impl.registers;

import io.github.piscescup.fabricmc.api.RegisterFactory;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 * Stores the target {@link Registry} key and namespace shared by registration factories.
 *
 * <p>Specialized factories combine {@link #namespace()} with a selected path
 * and return a new pre-registration builder. This base implementation only
 * retains the factory's scope; construction does not register any values.</p>
 *
 * @param <R> the value type of the target registry; for tag factories,
 *            the type of values that the tags may contain
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see RegisterFactory
 */
public class RegisterFactoryImpl<R> implements RegisterFactory<R> {
    /**
     * The namespace supplied at construction time. Specialized factories combine
     * it with selected paths to construct registration identifiers.
     */
    protected final String namespace;

    /**
     * The {@link ResourceKey} of the target {@link Registry} supplied at
     * construction time. Exposed by {@link #registryKey()}.
     */
    protected final ResourceKey<? extends Registry<R>> resourceKey;

    /**
     * Creates a factory scope for a registry and namespace.
     *
     * <p>Both arguments are retained as-is. Identifier construction and namespace
     * syntax validation are performed when specialized factories create paths.</p>
     *
     * @param resourceKey the target {@link Registry} key; must not be {@code null}
     * @param namespace   the namespace for identifiers; must not be {@code null}
     * @throws NullPointerException if {@code resourceKey} or {@code namespace} is {@code null}
     */
    protected RegisterFactoryImpl(
        final ResourceKey<? extends Registry<R>> resourceKey,
        String namespace
    ) {
        NullCheck.requireNonNull(resourceKey, "resourceKey");
        NullCheck.requireNonNull(namespace, "namespace");

        this.resourceKey = resourceKey;
        this.namespace = namespace;
    }

    /**
     * Returns the {@link Registry} key retained by this factory.
     *
     * @return the target registry key
     */
    @Override
    public @NotNull ResourceKey<? extends Registry<R>> registryKey() {
        return this.resourceKey;
    }

    /**
     * Returns the namespace retained by this factory.
     *
     * @return the namespace used for registration paths
     */
    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

}
