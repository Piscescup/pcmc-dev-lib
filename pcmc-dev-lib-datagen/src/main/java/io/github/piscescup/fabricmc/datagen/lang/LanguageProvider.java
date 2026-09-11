package io.github.piscescup.fabricmc.datagen.lang;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.store.lang.MutableLangTranslations;
import io.github.piscescup.fabricmc.impl.store.lang.MutableTranslationsHolder;
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
    private final MutableLangTranslations langTranslations;

    public LanguageProvider(
        FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup,
        MCLanguage language
    ) {
        super(packOutput, language.getCode(), registryLookup);
        langTranslations = MutableTranslationsHolder.INSTANCE.translations(language);
    }

    @Override
    public void generateTranslations(HolderLookup.@NotNull Provider registryLookup, @NotNull TranslationBuilder translationBuilder) {
        langTranslations.translations().forEach(translationBuilder::add);
    }
}
