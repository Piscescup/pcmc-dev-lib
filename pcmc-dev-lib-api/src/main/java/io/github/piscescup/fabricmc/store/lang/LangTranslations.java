package io.github.piscescup.fabricmc.store.lang;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Provides a read-only {@link Map} of translations for one Minecraft language.
 *
 * <p>Each entry maps a translation key, such as {@code item.example.item1}
 * or {@code itemGroup.example.test_tab}, to the localized text intended for that
 * language. The language is selected by the containing
 * {@link ReadableTranslationsHolder}, so it is not stored in each map entry.</p>
 *
 * <p>Typical usage when forwarding entries to a language data provider:</p>
 * <pre>{@code
 * LangTranslations english = holder.translations(MCLanguage.EN_US);
 * english.translations().forEach(translationBuilder::add);
 * }</pre>
 *
 * <p>Read-only access prevents consumers from modifying the map directly. In
 * the supplied implementation, the map is a live view of its backing storage,
 * so translations added through registration APIs remain visible.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ReadableTranslationsHolder
 */
public interface LangTranslations {

    /**
     * Returns an unmodifiable view of the translations.
     *
     * <p>The map may be empty. The supplied implementation exposes entries in
     * natural key order and reflects later additions to the same translation
     * set. Copy the map if a stable snapshot is required.</p>
     *
     * @return a non-null, unmodifiable map from translation keys to localized text
     */
    @NotNull
    Map<String, String> translations();

}
