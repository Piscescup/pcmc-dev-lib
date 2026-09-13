package io.github.piscescup.fabricmc.api.store.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Stores translations for a specific language.
 *
 * @param <SELF> the concrete translation container type
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface LangTranslations<SELF extends LangTranslations<SELF>> {

    /**
     * Returns an unmodifiable view of the translations.
     *
     * @return the translations indexed by translation key
     */
    @NotNull
    Map<String, String> translations();

    /**
     * Adds a translation.
     *
     * @param key         the translation key
     * @param translation the translated text
     *
     * @return this translation container
     *
     * @throws IllegalStateException if the key already has a
     *                               different translation
     */
    @Contract("_, _ -> this")
    @NotNull
    SELF add(
        @NotNull String key,
        @NotNull String translation
    );
}