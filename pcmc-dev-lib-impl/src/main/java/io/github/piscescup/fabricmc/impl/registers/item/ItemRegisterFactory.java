package io.github.piscescup.fabricmc.impl.registers.item;

import io.github.piscescup.fabricmc.api.registers.item.ItemPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.item.ItemPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Creates {@link Item} pre-registration builders within one namespace.
 *
 * <p>Use {@link #path(String)} for a standard {@link Item}, or
 * {@link #path(String, Function)} to supply a custom item constructor. Each
 * call creates an independent builder; the item is constructed and registered
 * when that builder's {@code register()} method is called.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * ItemRegisterFactory ITEMS = ItemRegisterFactory.ofNamespace("example");
 * Item item = ITEMS.path("example_item")
 *     .properties(new Item.Properties().fireResistant())
 *     .register()
 *     .translate(MCLanguage.EN_US, "Example Item")
 *     .get();
 * }</pre>
 *
 * <p>Typical usage with a custom item type:</p>
 * <pre>{@code
 * ItemRegisterFactory ITEMS = ItemRegisterFactory.ofNamespace("example");
 * MyItem item = ITEMS.path("my_item", MyItem::new)
 *     .properties(new Item.Properties().fireResistant())
 *     .register()
 *     .get();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ItemRegister
 * @see ItemPreRegistrable
 * @see ItemPostRegistrable
 */
public final class ItemRegisterFactory extends RegisterFactoryImpl<Item> {

    /**
     * Factory-local list of {@link ItemPostRegistrable} stages.
     * The current path methods return builders directly and do not populate this list.
     */
    private final List<ItemPostRegistrable<?>> entries = new ArrayList<>();

    /**
     * Creates a factory targeting {@link Registries#ITEM}.
     *
     * @param namespace the namespace for {@link Item} identifiers; must not be {@code null}
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    protected ItemRegisterFactory(String namespace) {
        super(Registries.ITEM, namespace);
    }

    /**
     * Creates a new {@link Item} factory for the given namespace.
     *
     * @param namespace the namespace, usually a mod ID; must not be {@code null}
     * @return a new factory scoped to the item registry and namespace
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static ItemRegisterFactory ofNamespace(@NotNull String namespace) {
        return new ItemRegisterFactory(namespace);
    }

    /**
     * Starts registration of a standard {@link Item} using {@code Item::new}.
     *
     * <p>The path is combined with this factory's namespace to construct the
     * {@link Identifier}. The returned builder starts with default
     * {@link Item.Properties}; item construction is deferred until registration.</p>
     *
     * @param path the {@link Item} path without a namespace prefix; must not be {@code null}
     * @return a new pre-registration stage with default item properties
     * @throws NullPointerException if {@code path} is {@code null}
     * @see #path(String, Function)
     */
    public ItemPreRegistrable<Item> path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return new ItemRegister<>(id, Item::new);
    }

    /**
     * Starts registration of an {@link Item} created by the supplied {@link Function}.
     *
     * <p>The factory is retained until registration, when it receives the
     * selected properties with the item's resource key assigned.</p>
     *
     * @param path    the {@link Item} path without a namespace prefix; must not be {@code null}
     * @param factory the function returning the {@link Item} to register; must not be {@code null}
     * @param <I>     the concrete {@link Item} type preserved by both registration stages
     * @return a new pre-registration stage for the concrete item type
     * @throws NullPointerException if {@code path} or {@code factory} is {@code null}
     */
    public <I extends Item> ItemPreRegistrable<I> path(@NotNull String path, @NotNull Function<Item.Properties, I> factory) {
        NullCheck.requireNonNull(path, "path");
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return new ItemRegister<>(id, factory);
    }
}
