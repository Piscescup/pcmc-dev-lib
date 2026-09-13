package io.github.piscescup.fabricmc.api.registers;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents the operations available after an object has been registered.
 *
 * <p>This stage provides access to the registered value and its identity,
 * supports localized values selected with {@link MCLanguage}, and can place the
 * value into a {@link Collection} while preserving fluent chaining.</p>
 *
 * @param <V> the base type accepted by the target registry
 * @param <T> the concrete registered type
 * @param <POST> the concrete post-registration stage type
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface PostRegistrable<V, T extends V, POST extends PostRegistrable<V, T, POST>> {

    /**
     * Adds a localized display value for the registered object.
     *
     * @param lang the target {@link MCLanguage Minecraft language}
     * @param translation the localized text
     * @return this post-registration stage
     */
    @NotNull
    POST translate(@NotNull MCLanguage lang, @NotNull String translation);

    /**
     * Returns the registered object.
     *
     * @return the registered object
     */
    @NotNull
    T get();

    /**
     * Returns the namespaced {@link Identifier} of the registered object.
     *
     * @return the registered identifier
     */
    @NotNull
    Identifier identifier();

    /**
     * Returns the {@link ResourceKey} of the registered object.
     *
     * @return the registered resource key
     */
    @NotNull
    ResourceKey<V> resourceKey();

    /**
     * Adds the registered object to a {@link Collection}.
     *
     * @param collection the collection that receives the registered object
     * @return this post-registration stage
     */
    @SuppressWarnings("unchecked")
    default POST collectsTo(@NotNull Collection<V> collection) {
        collection.add(get());
        return (POST) this;
    }
}
