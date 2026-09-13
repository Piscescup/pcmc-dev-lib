package io.github.piscescup.fabricmc.datagen.lang;

import io.github.piscescup.fabricmc.api.store.lang.LangTranslations;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.util.validation.NullCheck;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class LanguageProvider extends FabricLanguageProvider {
    private final LangTranslations translations;

    public LanguageProvider(
        FabricPackOutput packOutput,
        CompletableFuture<HolderLookup.Provider> registryLookup,
        MCLanguage language,
        LangTranslations translations
    ) {
        super(packOutput, language.getCode(), registryLookup);

        this.translations = NullCheck.requireNonNull(
            translations,
            "translations"
        );
    }

    @Override
    public void generateTranslations(
        HolderLookup.@NotNull Provider registryLookup,
        TranslationBuilder translationBuilder
    ) {
        this.translations
            .translations()
            .forEach(translationBuilder::add);
    }
}
