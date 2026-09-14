package io.github.piscescup.fabricmc.api.registers;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents the post-registration customization stage of a registration operation.
 *
 * <p>This stage is returned by {@link PreRegistrable#register()} after the
 * pre-registration configuration has been completed. It provides access to the
 * registered object and its identity, records localized text for data generation,
 * and can add the object to a {@link Collection} while preserving fluent chaining.</p>
 *
 * <p>Typical usage, taking item registration as an example:</p>
 * <pre>{@code
 * public static final Item ITEM1 = ITEMS.path("item1")
 *     .register()
 *     .translate(MCLanguage.EN_US, "Item 1")
 *     .translate(MCLanguage.ZH_CN, "物品 1")
 *     .collectsTo(REGISTERED_ITEMS)
 *     .get();
 * }</pre>
 *
 * <p>Implementations may use the same stateful object for both registration
 * stages. After registration, use that object through this interface for further
 * customization. Accessors return the existing registration result; they do not
 * create or register another object.</p>
 *
 * @param <V>    the base type accepted by the target registry,
 *               such as {@code Item}
 * @param <T>    the concrete registered type; must be a subtype of {@code V}
 * @param <POST> the concrete post-registration stage type (self type),
 *               used to preserve the fluent API across customization calls,
 *               such as {@code ItemPostRegistrable<I>}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 */
public interface PostRegistrable<V, T extends V, POST extends PostRegistrable<V, T, POST>> {

    /**
     * Adds a localized display value for the registered object.
     *
     * <p>The translation key is derived from the registered object by the
     * implementation. The supplied implementation stores this entry for language
     * data generation; calling this method does not write a language file or
     * reload client resources. Repeating the same key and text is allowed, but
     * assigning different text to an existing key in the same language fails.</p>
     *
     * @param lang        the target {@link MCLanguage Minecraft language}
     * @param translation the localized text
     * @return this post-registration stage
     * @throws IllegalStateException if the supplied implementation already holds
     *                               different text for the same language and key
     */
    @NotNull
    POST translate(@NotNull MCLanguage lang, @NotNull String translation);

    /**
     * Returns the registered object.
     *
     * <p>Call this method after {@link PreRegistrable#register()} has completed
     * successfully. Repeated calls return the same registered instance.</p>
     *
     * @return the registered object, retaining its concrete type {@code T}
     * @see PreRegistrable#register()
     */
    @NotNull
    T get();

    /**
     * Returns the namespaced {@link Identifier} of the registered object.
     *
     * <p>The identifier combines the namespace and path, for example
     * {@code example:item1}. It identifies the value within its target registry.</p>
     *
     * @return the registered identifier
     * @see #resourceKey()
     */
    @NotNull
    Identifier identifier();

    /**
     * Returns the {@link ResourceKey} of the registered object.
     *
     * <p>The key associates {@link #identifier()} with the target registry.
     * Stages that describe tag keys may not support this operation; use
     * {@link io.github.piscescup.fabricmc.api.registers.tag.TagKeyPostRegistrable#tagResourceKey()
     * TagKeyPostRegistrable.tagResourceKey()} to obtain the registry targeted by a tag.</p>
     *
     * @return the registered resource key, typed to the registry's base type
     * @throws UnsupportedOperationException if this stage does not represent
     *                                       an entry with a resource key
     * @see #identifier()
     */
    @NotNull
    ResourceKey<V> resourceKey();

    /**
     * Adds the registered object to a {@link Collection}.
     *
     * <p>This method immediately calls {@link Collection#add(Object)} with
     * {@link #get()} and returns this stage. The collection determines ordering
     * and duplicate handling; its return value is ignored. The collection must
     * support insertion, and any exception from it is propagated.</p>
     *
     * @param collection the collection that receives the registered object
     * @return this post-registration stage
     * @throws UnsupportedOperationException if the collection does not support insertion
     * @see #get()
     */
    @SuppressWarnings("unchecked")
    default POST collectsTo(@NotNull Collection<V> collection) {
        collection.add(get());
        return (POST) this;
    }
}
