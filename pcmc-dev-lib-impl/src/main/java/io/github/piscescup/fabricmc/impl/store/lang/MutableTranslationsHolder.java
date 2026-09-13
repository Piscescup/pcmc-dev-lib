package io.github.piscescup.fabricmc.impl.store.lang;

import io.github.piscescup.fabricmc.api.store.lang.TranslationsHolder;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.util.validation.NullCheck;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum MutableTranslationsHolder implements TranslationsHolder<MutableLangTranslations> {
    INSTANCE;

    private final Map<MCLanguage, MutableLangTranslations> translations =
        new EnumMap<>(MCLanguage.class);

    @Override
    public void add(
        @NotNull MCLanguage language,
        @NotNull String key,
        @NotNull String translation
    ) {
        mutableTranslations(language).add(key, translation);
    }

    @Override
    public @NotNull MutableLangTranslations translations(
        @NotNull MCLanguage language
    ) {
        return mutableTranslations(language);
    }

    @Override
    public @NotNull Collection<? extends MutableLangTranslations> all() {
        return Collections.unmodifiableCollection(
            translations.values()
        );
    }

    private @NotNull MutableLangTranslations mutableTranslations(@NotNull MCLanguage language) {
        NullCheck.requireNonNull(language, "language");
        return translations.computeIfAbsent(
            language,
            _ -> new MutableLangTranslations()
        );
    }
}
