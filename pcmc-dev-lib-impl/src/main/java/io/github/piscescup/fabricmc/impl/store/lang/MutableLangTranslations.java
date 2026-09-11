package io.github.piscescup.fabricmc.impl.store.lang;

import io.github.piscescup.fabricmc.api.store.lang.LangTranslations;
import io.github.piscescup.util.validation.NullCheck;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class MutableLangTranslations
    implements LangTranslations<MutableLangTranslations>
{
    private final NavigableMap<String, String> translations =
        new TreeMap<>();

    MutableLangTranslations() {}
    @Override
    public @NotNull Map<String, String> translations() {
        return Collections.unmodifiableMap(translations);
    }

    @Override
    public @NotNull MutableLangTranslations add(
        @NotNull String key,
        @NotNull String translation
    ) {
        NullCheck.requireNonNull(key, "key");
        NullCheck.requireNonNull(translation, "translation");

        if (key.isBlank()) {
            throw new IllegalArgumentException(
                "Translation key cannot be blank."
            );
        }

        String previous =
            translations.putIfAbsent(key, translation);

        if (previous != null && !previous.equals(translation)) {
            throw new IllegalStateException(
                "Translation key '%s' is already mapped to '%s'."
                    .formatted(key, previous)
            );
        }

        return this;
    }

}
