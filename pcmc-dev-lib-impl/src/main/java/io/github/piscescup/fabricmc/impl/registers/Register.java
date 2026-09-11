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

    protected Identifier id;

    protected Registry<V> registry;

    protected ResourceKey<V> resourceKey;

    protected T thingToBeRegistered;

    protected Register(Registry<V> registry, Identifier id) {
        this.id = NullCheck.requireNonNull(id, "id");
        this.registry = NullCheck.requireNonNull(registry, "registry");
        this.resourceKey = ResourceKey.create(
            registry.key(),
            this.id
        );
    }


    @Override
    public @NotNull ResourceKey<V> resourceKey() {
        return resourceKey;
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
