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
 * Supplies collected {@link LangTranslations} entries to {@link FabricLanguageProvider}.
 *
 * <p>Each provider targets one {@link MCLanguage}, using
 * {@link MCLanguage#getCode()} as the language code. It retains the supplied
 * {@link LangTranslations} and reads its map when
 * {@link #generateTranslations(HolderLookup.Provider, TranslationBuilder)} runs.
 * If that map is a live view, additions made before generation remain visible.</p>
 *
 * <p>Typical usage when adding a provider directly to a data pack:</p>
 * <pre>{@code
 * pack.addProvider((output, lookup) -> new LanguageProvider(
 *     output, lookup, MCLanguage.EN_US,
 *     translationsHolder.translations(MCLanguage.EN_US)
 * ));
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see LangTranslations
 * @see io.github.piscescup.fabricmc.datagen.DatagenCollectors
 */
public class LanguageProvider extends FabricLanguageProvider {
    /**
     * The {@link LangTranslations} supplied at construction time and retained as-is.
     * Its map is read during {@link #generateTranslations(HolderLookup.Provider, TranslationBuilder)};
     * live views therefore expose additions made before generation begins.
     */
    private final LangTranslations translations;

    /**
     * Creates a {@link LanguageProvider} backed by the supplied {@link LangTranslations}.
     *
     * <p>The translation set is retained directly rather than copied. Its
     * entries must correspond to the selected language.</p>
     *
     * @param packOutput     the {@link FabricPackOutput} used for generated resources; must not be {@code null}
     * @param registryLookup the future {@link HolderLookup.Provider} supplied by Fabric; must not be {@code null}
     * @param language       the {@link MCLanguage} selecting the output language; must not be {@code null}
     * @param translations   the {@link LangTranslations} read during generation; must not be {@code null}
     * @throws NullPointerException if {@code language} or {@code translations} is {@code null}
     */
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

    /**
     * Forwards each collected translation key and value to a {@link TranslationBuilder}.
     *
     * <p>Entries are read when this method runs and forwarded in the source map's
     * iteration order. This provider does not derive additional keys or use
     * registry lookups to transform the collected text.</p>
     *
     * @param registryLookup     the {@link HolderLookup.Provider} supplied by Fabric, unused by this implementation
     * @param translationBuilder the {@link TranslationBuilder} receiving each entry; must not be {@code null}
     * @throws NullPointerException if {@code translationBuilder} is {@code null}
     */
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
