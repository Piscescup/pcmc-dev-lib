package io.github.piscescup.fabricmc.impl.store.lang;

import io.github.piscescup.fabricmc.api.store.lang.ReadableTranslationsHolder;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum MutableTranslationsHolder implements ReadableTranslationsHolder {
    INSTANCE;

    private final Map<MCLanguage, MutableLangTranslations> translations =
        new EnumMap<>(MCLanguage.class);

    private MutableTranslationsHolder() {
    }

    public void add(
        @NotNull MCLanguage language,
        @NotNull String key,
        @NotNull String translation
    ) {
        translations(language).add(key, translation);
    }

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
