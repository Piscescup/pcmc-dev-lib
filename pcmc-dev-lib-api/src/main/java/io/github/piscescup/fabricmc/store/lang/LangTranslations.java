package io.github.piscescup.fabricmc.store.lang;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Provides a read-only {@link Map} of translations for one Minecraft language.
 *
 * <p>Each entry maps a translation key, such as an item or creative-tab key,
 * to the localized text intended for that language.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface LangTranslations {

    /**
     * Returns an unmodifiable view of the translations.
     *
     * @return translation keys mapped to localized text
     */
    @NotNull
    Map<String, String> translations();

}
