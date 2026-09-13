package io.github.piscescup.fabricmc.store.lang;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ReadableTranslationsHolder {

    /**
     * Returns the translations for the specified language.
     *
     * @param language the target language
     *
     * @return the translations
     */
    @NotNull
    LangTranslations translations(
        @NotNull MCLanguage language
    );

}
