package io.github.piscescup.fabricmc.store.lang;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Provides read-only {@link LangTranslations translation sets} grouped by
 * {@link MCLanguage Minecraft language}.
 *
 * <p>This view supplies the language entries declared through registration
 * APIs to consumers such as data-generation providers. Each language has a
 * separate set of translation keys and localized values.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * LangTranslations english = holder.translations(MCLanguage.EN_US);
 * Map<String, String> entries = english.translations();
 * entries.forEach(translationBuilder::add);
 * }</pre>
 *
 * <p>The supplied implementation creates an empty translation set on the first
 * lookup for a language and reuses that set for later lookups. Its unmodifiable
 * map reflects translations subsequently added for the same language.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see LangTranslations
 * @see MCLanguage
 */
public interface ReadableTranslationsHolder {

    /**
     * Returns the {@link LangTranslations} declared for a language.
     *
     * <p>An unpopulated language produces an empty translation set in the
     * supplied implementation. Reading that set does not generate a language
     * file; a data provider consumes its entries separately.</p>
     *
     * @param language the target {@link MCLanguage}
     * @return the translations for that language, possibly empty
     * @see LangTranslations#translations()
     */
    @NotNull
    LangTranslations translations(
        @NotNull MCLanguage language
    );

}
