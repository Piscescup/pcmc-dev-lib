package io.github.piscescup.fabricmc.store.lang;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Provides read-only {@link LangTranslations translation sets} grouped by
 * {@link MCLanguage Minecraft language}.
 *
 * <p>This view can supply the language entries declared through registration
 * APIs to consumers such as data-generation providers.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ReadableTranslationsHolder {

    /**
     * Returns the {@link LangTranslations} declared for a language.
     *
     * @param language the target {@link MCLanguage}
     * @return the translations for that language, possibly empty
     */
    @NotNull
    LangTranslations translations(
        @NotNull MCLanguage language
    );

}
