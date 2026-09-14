package io.github.piscescup.fabricmc.impl.store.lang;

import io.github.piscescup.fabricmc.api.store.lang.ReadableTranslationsHolder;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * Holds the shared translation sets populated by registration operations.
 *
 * <p>Each {@link MCLanguage} has a lazily created {@link MutableLangTranslations}
 * set. The {@link ReadableTranslationsHolder} interface exposes these sets to
 * data-generation consumers, while this implementation also supports mutation.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * MutableTranslationsHolder.INSTANCE.add(
 *     MCLanguage.EN_US, "item.example.example_item", "Example Item"
 * );
 * LangTranslations english =
 *     MutableTranslationsHolder.INSTANCE.translations(MCLanguage.EN_US);
 * }</pre>
 *
 * <p>The singleton retains declarations for its lifetime and does not synchronize
 * access. Complete registration-time population before concurrent consumption.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see MutableLangTranslations
 * @see ReadableTranslationsHolder
 */
public enum MutableTranslationsHolder implements ReadableTranslationsHolder {
    /**
     * The shared {@link MutableTranslationsHolder} used by the registration implementations.
     */
    INSTANCE;

    /**
     * Per-language stores indexed by {@link MCLanguage} in an {@link EnumMap}.
     * Initially empty; {@link #translations(MCLanguage)} creates and retains a
     * {@link MutableLangTranslations} instance on first lookup for each language.
     */
    private final Map<MCLanguage, MutableLangTranslations> translations =
        new EnumMap<>(MCLanguage.class);

    /**
     * Initializes the shared holder with no language sets.
     */
    private MutableTranslationsHolder() {
    }

    /**
     * Adds a localized value to the selected language's translation set.
     *
     * <p>The language set is created if needed. Duplicate and conflict handling
     * is delegated to {@link MutableLangTranslations#add(String, String)}.</p>
     *
     * @param language    the target {@link MCLanguage}; must not be {@code null}
     * @param key         the non-blank translation key; must not be {@code null}
     * @param translation the localized text; must not be {@code null}
     * @throws NullPointerException if any argument is {@code null}
     * @throws IllegalArgumentException if the translation key is blank
     * @throws IllegalStateException if different text already exists for the language and key
     */
    public void add(
        @NotNull MCLanguage language,
        @NotNull String key,
        @NotNull String translation
    ) {
        translations(language).add(key, translation);
    }

    /**
     * Returns the {@link MutableLangTranslations} associated with an {@link MCLanguage}.
     *
     * <p>The first lookup creates an empty set. Subsequent lookups return the
     * same set, whose unmodifiable map reflects later additions.</p>
     *
     * @param language the target {@link MCLanguage}; must not be {@code null}
     * @return the existing or newly created translation set
     * @throws NullPointerException if {@code language} is {@code null}
     */
    @Override
    public @NotNull MutableLangTranslations translations(
        @NotNull MCLanguage language
    ) {
        return translations.computeIfAbsent(
            language,
            _ -> new MutableLangTranslations()
        );
    }
}
