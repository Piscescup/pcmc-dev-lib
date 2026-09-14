package io.github.piscescup.fabricmc.impl.store.lang;

import io.github.piscescup.fabricmc.store.lang.LangTranslations;
import io.github.piscescup.util.validation.NullCheck;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Stores the translation entries for one Minecraft language.
 *
 * <p>Instances are created by {@link MutableTranslationsHolder}. Entries are
 * ordered by their translation keys using a {@link TreeMap}. Consumers receive
 * an unmodifiable live view through {@link #translations()}, while additions
 * are made through {@link #add(String, String)}.</p>
 *
 * <p>Repeating the same key and text is allowed. Declaring different text for an
 * existing key fails and leaves the original mapping intact. This store is
 * mutable and does not synchronize access.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see MutableTranslationsHolder
 * @see LangTranslations
 */
public class MutableLangTranslations
    implements LangTranslations
{
    /**
     * Mutable translation-key mappings ordered naturally by key in a {@link TreeMap}.
     * Initially empty; {@link #translations()} wraps this same map in an
     * unmodifiable view, and {@link #add(String, String)} populates it.
     */
    private final NavigableMap<String, String> translations =
        new TreeMap<>();

    /**
     * Creates an empty translation set for use by {@link MutableTranslationsHolder}.
     */
    MutableLangTranslations() {}

    /**
     * Returns an unmodifiable live {@link Map} view of this language's translation entries.
     *
     * <p>Iteration follows natural key order. Later successful additions are
     * visible through previously obtained views; copy the map for a snapshot.</p>
     *
     * @return translation keys mapped to localized text, possibly empty
     */
    @Override
    public @NotNull Map<String, String> translations() {
        return Collections.unmodifiableMap(translations);
    }

    /**
     * Adds a translation unless its key already maps to the same text.
     *
     * <p>A conflicting value is rejected without replacing the existing text.
     * Localized text may be empty, but the translation key must not be blank.</p>
     *
     * @param key         the non-blank translation key; must not be {@code null}
     * @param translation the localized text associated with the key; must not be {@code null}
     * @throws NullPointerException if {@code key} or {@code translation} is {@code null}
     * @throws IllegalArgumentException if the key is blank
     * @throws IllegalStateException if the key already maps to different text
     */
    public void add(
        @NotNull String key,
        @NotNull String translation
    ) {
        NullCheck.requireNonNull(key, "key");
        NullCheck.requireNonNull(translation, "translation");

        if (key.isBlank()) {
            throw new IllegalArgumentException(
                "Translation key cannot be blank."
            );
        }

        String previous =
            translations.putIfAbsent(key, translation);

        if (previous != null && !previous.equals(translation)) {
            throw new IllegalStateException(
                "Translation key '%s' is already mapped to '%s'."
                    .formatted(key, previous)
            );
        }
    }

}
