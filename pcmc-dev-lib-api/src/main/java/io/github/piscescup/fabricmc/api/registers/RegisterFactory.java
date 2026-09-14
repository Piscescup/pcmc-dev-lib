package io.github.piscescup.fabricmc.api.registers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a registration factory scoped to one {@link Registry} and namespace.
 *
 * <p>The {@link Registry} determines the kind of values described by the factory,
 * while the namespace is combined with the paths selected for those values.
 * Specialized factories expose methods that create a
 * {@link PreRegistrable pre-registration stage} for items, blocks, creative mode
 * tabs, or tags. The accessors on this interface only describe the factory's scope.</p>
 *
 * <p>For example, an item factory with namespace {@code example} can use the
 * path {@code item1} to start the following registration:</p>
 * <pre>{@code
 * public static final Item ITEM1 = ITEMS.path("item1")
 *     .properties(new Item.Properties())
 *     .register()
 *     .translate(MCLanguage.EN_US, "Item 1")
 *     .get();
 * }</pre>
 *
 * @param <R> the value type of the target registry, such as {@code Item};
 *            for a tag factory, the type of values that its tags may contain
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 * @see PostRegistrable
 */
public interface RegisterFactory<R> {
    /**
     * Returns the {@link ResourceKey} of the {@link Registry} targeted by this factory.
     *
     * <p>This is the key of the registry itself, not the key of an individual
     * entry produced by a registration stage.</p>
     *
     * @return the target registry key
     */
    @NotNull
    ResourceKey<? extends Registry<R>> registryKey();

    /**
     * Returns the namespace used for registration identifiers.
     *
     * <p>Specialized factories combine this namespace with a selected path.
     * For example, namespace {@code example} and path {@code item1} produce
     * the identifier {@code example:item1}.</p>
     *
     * @return the namespace, usually a mod ID
     */
    @NotNull
    String namespace();

}
