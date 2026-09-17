package io.github.piscescup.fabricmc.impl.registers;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.store.lang.MutableTranslationsHolder;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Provides shared state and post-registration operations for {@link Registry} builders.
 *
 * <p>Subclasses implement the matching {@code PRE} and {@code POST} interfaces
 * on the same instance. They construct and register their value in
 * {@link #register()}, assign the result to {@link #thingToBeRegistered}, and
 * return themselves as the post-registration stage.</p>
 *
 * <p>Construction establishes the identifier and resource key but does not
 * register a value. Complete configuration and call {@code register()} once
 * before accessing the result or adding translations. This base class does
 * not enforce the stage transition or prevent repeated registration.</p>
 *
 * <p>Translations are collected through {@link MutableTranslationsHolder} for
 * later language data generation. Subclasses define the translation key through
 * {@link #translateKey()}.</p>
 *
 * @param <V>    the base type accepted by the target registry
 * @param <T>    the concrete registered type; must be a subtype of {@code V}
 * @param <PRE>  the pre-registration stage type implemented by the subclass
 * @param <POST> the post-registration stage type implemented by the subclass,
 *               used to preserve fluent customization
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 * @see PostRegistrable
 */
public abstract class Register<
    V, T extends V,
    PRE extends PreRegistrable<V, T, PRE, POST>,
    POST extends PostRegistrable<V, T, POST>>
    implements PreRegistrable<V, T, PRE, POST>, PostRegistrable<V, T, POST>
{

    /**
     * The namespaced {@link Identifier} supplied at construction time.
     * Used to derive {@link #resourceKey} and exposed by {@link #identifier()}.
     */
    protected Identifier id;

    /**
     * The target {@link Registry} supplied at construction time.
     * Subclasses register their values in this registry during {@link #register()}.
     */
    protected Registry<V> registry;

    /**
     * The {@link ResourceKey} derived from {@link #registry} and {@link #id}
     * during construction. Exposed by {@link #resourceKey()}.
     */
    protected ResourceKey<V> resourceKey;

    /**
     * The registration result assigned by the subclass during {@link #register()}.
     * Initially {@code null}; {@link #get()} returns this field directly.
     */
    protected T thingToBeRegistered;

    /**
     * The {@link MutableTranslationsHolder} supplied at construction time.
     * Receives declarations made through {@link #translate(MCLanguage, String)}.
     */
    protected MutableTranslationsHolder translationsHolder;


    /**
     * Initializes registration state and derives the entry's {@link ResourceKey}.
     *
     * <p>The supplied references are retained directly. The {@link ResourceKey}
     * is created immediately; the registered result remains {@code null} until
     * the subclass assigns it during {@link #register()}.</p>
     *
     * @param registry           the target {@link Registry}; must not be {@code null}
     * @param id                 the namespaced {@link Identifier}; must not be {@code null}
     * @param translationsHolder the holder receiving this entry's translations; must not be {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    protected Register(
        Registry<V> registry,
        Identifier id
    ) {
        this(
            registry.key(),
            id,
            MutableTranslationsHolder.INSTANCE
        );
    }

    protected Register(
        ResourceKey<? extends Registry<V>> registry,
        Identifier id
    ) {
        this(
            registry,
            id,
            MutableTranslationsHolder.INSTANCE
        );
    }

    protected Register(
        ResourceKey<? extends Registry<V>> registry,
        Identifier id,
        MutableTranslationsHolder translationsHolder
    ) {
        this.id = NullCheck.requireNonNull(id, "id");
        this.translationsHolder = NullCheck.requireNonNull(
            translationsHolder,
            "translationsHolder"
        );

        this.resourceKey = ResourceKey.create(
            registry,
            this.id
        );
    }

    /**
     * Derives the translation key used for localized display text.
     *
     * <p>Implementations may obtain the key from the registered value, so this
     * method is used after registration has completed successfully.</p>
     *
     * @return the non-null, non-blank translation key
     */
    protected abstract String translateKey();

    /**
     * Records localized text under the key supplied by {@link #translateKey()}.
     *
     * <p>Identical declarations for the same language and key are accepted.
     * Conflicting text is rejected by the translation holder. The entry is
     * stored immediately for later data generation.</p>
     *
     * @param lang        the target {@link MCLanguage}; must not be {@code null}
     * @param translation the localized text; must not be {@code null}
     * @return this instance as its post-registration stage
     * @throws NullPointerException if {@code lang}, {@code translation}, or the
     *         derived translation key is {@code null}; subclass key derivation
     *         may also fail if registration has not completed
     * @throws IllegalArgumentException if the derived translation key is blank
     * @throws IllegalStateException if different text already exists for the language and key
     */
    @SuppressWarnings("unchecked")
    @Override
    public final @NonNull POST translate(@NotNull MCLanguage lang, @NotNull String translation) {
        NullCheck.requireNonNull(lang, "lang");
        NullCheck.requireNonNull(translation, "translation");

        this.translationsHolder.add(lang, translateKey(), translation);

        return (POST) this;
    }

    /**
     * Returns the {@link ResourceKey} established when this builder was constructed.
     *
     * @return the key combining the target registry and entry identifier
     */
    @Override
    public @NotNull ResourceKey<V> resourceKey() {
        return resourceKey;
    }

    /**
     * Returns the {@link Identifier} selected for this registration.
     *
     * @return the namespaced entry identifier
     */
    @Override
    public @NotNull Identifier identifier() {
        return id;
    }

    /**
     * Returns the registration result stored by the subclass.
     *
     * <p>Call this method only after registration has completed successfully.
     * It directly reads the result field and does not perform a registry lookup
     * or validate the builder's current stage.</p>
     *
     * @return the registered instance, retaining its concrete type; the current
     *         implementation returns {@code null} until the result field is assigned
     */
    @Override
    public @NonNull T get() {
        return thingToBeRegistered;
    }
}
