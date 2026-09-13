package io.github.piscescup.fabricmc.api.store.lang;

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
public interface LangTranslations {

    /**
     * Returns an unmodifiable view of the translations.
     *
     * @return the translations indexed by translation key
     */
    @NotNull
    Map<String, String> translations();

}