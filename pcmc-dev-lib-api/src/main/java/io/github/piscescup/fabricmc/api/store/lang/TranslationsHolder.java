package io.github.piscescup.fabricmc.api.store.lang;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TranslationsHolder<LT extends LangTranslations<LT>> {
    /**
     * Adds a translation.
     *
     * @param language    the target language
     * @param key         the translation key
     * @param translation the translated text
     */
    void add(
        @NotNull MCLanguage language,
        @NotNull String key,
        @NotNull String translation
    );

    /**
     * Returns the translations for the specified language.
     *
     * @param language the target language
     *
     * @return the translations
     */
    @NotNull
    LT translations(
        @NotNull MCLanguage language
    );

    /**
     * Returns all non-empty language translation containers.
     *
     * @return all stored translations
     */
    @NotNull
    Collection<? extends LT> all();
}
