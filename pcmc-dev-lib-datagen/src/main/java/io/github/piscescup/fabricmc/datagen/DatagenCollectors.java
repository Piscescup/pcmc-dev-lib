package io.github.piscescup.fabricmc.datagen;

import io.github.piscescup.exception.IllegalBuilderPatternConfigurationException;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.lang.LanguageProvider;
import io.github.piscescup.fabricmc.datagen.recipe.RecipesGenerator;
import io.github.piscescup.fabricmc.datagen.tag.TagProvider;
import io.github.piscescup.fabricmc.store.lang.ReadableTranslationsHolder;
import io.github.piscescup.fabricmc.store.recipe.ReadableRecipeRegistrablesHolder;
import io.github.piscescup.fabricmc.store.tag.ReadableTagKeysHolder;
import io.github.piscescup.interfaces.Builder;
import io.github.piscescup.util.validation.NullCheck;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Configures language, tag, and recipe data providers for a Fabric data pack.
 *
 * <p>Start with {@link #configuration()}, supply the readable translation,
 * tag, and recipe holders, and call {@link DataProviderConfiguration#build()}.
 * Then select languages, registries, and recipe generation before attaching
 * the providers to a {@link FabricDataGenerator} through
 * {@link #generate(FabricDataGenerator.Pack)}.</p>
 *
 * <p>Typical usage in a data-generator entrypoint:</p>
 * <pre>{@code
 * FabricDataGenerator.Pack pack = generator.createPack();
 *
 * DatagenCollectors.configuration()
 *     .translationHolder(MutableTranslationsHolder.INSTANCE)
 *     .tagKeyHolder(MutableTagKeysHolder.INSTANCE)
 *     .recipeHolder(MutableReciperegistrablesHolder.INSTANCE)
 *     .build()
 *     .langProvider(MCLanguage.EN_US)
 *     .tagProvider(Registries.ITEM)
 *     .tagProvider(Registries.BLOCK)
 *     .recipesProvider()
 *     .generate(pack);
 * }</pre>
 *
 * <p>Provider-selection methods queue factories. The holders are consulted when
 * those factories create providers, and Fabric runs the providers as part of
 * its data-generation lifecycle. Finish declaring translations, tag members,
 * and recipe definitions before generation starts. A tag provider retains the
 * collector list obtained at construction, while the supplied recipe holder
 * exposes a live collection to its provider.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see DataProviderConfiguration
 * @see LanguageProvider
 * @see TagProvider
 * @see RecipesGenerator
 */
public final class DatagenCollectors {
    /**
     * The {@link ReadableTranslationsHolder} selected by the configuration builder.
     * Initially {@code null}; queued {@link LanguageProvider} factories consult
     * this reference when they are invoked.
     */
    private ReadableTranslationsHolder translationsHolder;

    /**
     * The {@link ReadableTagKeysHolder} selected by the configuration builder.
     * Initially {@code null}; queued {@link TagProvider} factories consult
     * this reference when they are invoked.
     */
    private ReadableTagKeysHolder tagKeysHolder;


    /**
     * The {@link ReadableRecipeRegistrablesHolder} selected by the configuration builder.
     * Initially {@code null}; queued {@link RecipesGenerator} factories consult
     * this reference when they are invoked.
     */
    private ReadableRecipeRegistrablesHolder recipesHolder;

    /**
     * Ordered {@link FabricDataGenerator.Pack.RegistryDependentFactory} queue.
     * Initially empty; {@link #langProvider(MCLanguage)},
     * {@link #tagProvider(ResourceKey)}, and {@link #recipesProvider()} append
     * factories for {@link #generate(FabricDataGenerator.Pack)}.
     */
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<? extends DataProvider>> registryDependentFactories = new ArrayList<>();

    /**
     * Ordered {@link DataProvider.Factory} queue processed after registry-dependent factories.
     * The current public configuration methods do not populate this queue.
     */
    private final List<DataProvider.Factory<? extends DataProvider>> factories = new ArrayList<>();

    /**
     * Creates an unconfigured collector with empty provider queues.
     *
     * <p>All three holder references are initially {@code null}. Use
     * {@link #configuration()} to select and validate the holders before
     * attaching language, tag, or recipe providers to a {@link FabricDataGenerator}.</p>
     *
     * @see #configuration()
     */
    public DatagenCollectors() {}


    /**
     * Starts a new configuration with independent provider queues.
     *
     * @return a new configuration for selecting the translation, tag, and recipe holders
     */
    @Contract(" -> new")
    @NotNull
    public static DataProviderConfiguration configuration() {
        return new DataProviderConfiguration();
    }

    /**
     * Queues a {@link LanguageProvider} for the selected {@link MCLanguage}.
     *
     * <p>The provider factory obtains the language's translation set from the
     * configured holder when invoked. Repeated calls enqueue additional factories;
     * this method does not deduplicate languages.</p>
     *
     * @param language the {@link MCLanguage} whose translations should be generated; must not be {@code null}
     * @return this collector for further provider selection
     * @throws NullPointerException if {@code language} is {@code null}
     * @see #generate(FabricDataGenerator.Pack)
     */
    public DatagenCollectors langProvider(MCLanguage language) {
        NullCheck.requireNonNull(language, "language");
        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new LanguageProvider(packOutput, lookupProvider, language, translationsHolder.translations(language))
        );
        return this;
    }

    /**
     * Queues a {@link TagProvider} for the selected {@link Registry}.
     *
     * <p>The provider reads the configured tag holder when constructed.
     * Repeated calls enqueue additional factories without deduplicating registries.</p>
     *
     * @param registry the {@link Registry} key whose tag declarations should be generated; must not be {@code null}
     * @param <T>      the {@link Registry} value type
     * @return this collector for further provider selection
     * @throws NullPointerException if {@code registry} is {@code null}
     * @see #generate(FabricDataGenerator.Pack)
     */
    public <T> DatagenCollectors tagProvider(ResourceKey<? extends Registry<T>> registry) {
        NullCheck.requireNonNull(registry, "registry");

        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new TagProvider<>(packOutput, registry, lookupProvider, this.tagKeysHolder)
        );

        return this;
    }

    /**
     * Queues a {@link RecipesGenerator} for the configured recipe holder.
     *
     * <p>Repeated calls enqueue additional providers. The holder is consulted
     * when Fabric invokes the queued factory, and its returned recipe collection
     * is retained by the resulting provider.</p>
     *
     * @return this collector for further provider selection
     * @see #generate(FabricDataGenerator.Pack)
     */
    public DatagenCollectors recipesProvider() {
        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new RecipesGenerator(packOutput, lookupProvider, this.recipesHolder)
        );

        return this;
    }


    /**
     * Attaches the queued provider factories to a {@link FabricDataGenerator.Pack}.
     *
     * <p>Registry-dependent factories are attached first, followed by ordinary
     * factories, preserving order within each queue. This method registers the
     * providers with Fabric; file generation is performed by the data generator.
     * The queues are retained, so another call attempts to attach them to another pack.</p>
     *
     * @param pack the data-generator pack receiving the queued providers; must not be {@code null}
     * @throws NullPointerException if {@code pack} is {@code null}, or a
     *         queued provider factory is invoked while its required holder is {@code null}
     */
    public void generate(@NotNull FabricDataGenerator.Pack pack) {

        registryDependentFactories.forEach(pack::addProvider);
        factories.forEach(pack::addProvider);

    }

    /**
     * Selects the readable stores required by a {@link DatagenCollectors} instance.
     *
     * <p>All three holders must be supplied before {@link #build()}, even when only
     * one provider category will be used. The builder owns one collector and
     * returns that same instance on each successful build. Holder references
     * are assigned directly and are not copied or frozen.</p>
     *
     * @author REN YuanTong
     * @since 1.0.0
     * @see DatagenCollectors#configuration()
     */
    public static final class DataProviderConfiguration
        implements Builder<DatagenCollectors>
    {
        /**
         * The {@link DatagenCollectors} created with this configuration.
         * Setters mutate this same instance, and each successful {@link #build()}
         * returns it without copying its holders or provider queues.
         */
        private final DatagenCollectors collectors;


        /**
         * Creates a configuration owning a new, initially unconfigured {@link DatagenCollectors}.
         */
        DataProviderConfiguration() {
            this.collectors = new DatagenCollectors();
        }

        /**
         * Selects the {@link ReadableTranslationsHolder} used to obtain per-language translations.
         *
         * <p>The reference replaces the previous selection. A {@code null}
         * selection is rejected when {@link #build()} validates the configuration.</p>
         *
         * @param translationsHolder the holder required by {@link LanguageProvider};
         *                           may be {@code null} here but must be set before {@link #build()}
         * @return this configuration builder
         */
        public DataProviderConfiguration translationHolder(ReadableTranslationsHolder translationsHolder) {
            this.collectors.translationsHolder = translationsHolder;
            return this;
        }

        /**
         * Selects the {@link ReadableTagKeysHolder} used to obtain tag declarations for each registry.
         *
         * <p>The reference replaces the previous selection. A {@code null}
         * selection is rejected when {@link #build()} validates the configuration.</p>
         *
         * @param tagKeysHolder the holder required by {@link TagProvider};
         *                      may be {@code null} here but must be set before {@link #build()}
         * @return this configuration builder
         */
        public DataProviderConfiguration tagKeyHolder(ReadableTagKeysHolder tagKeysHolder) {
            this.collectors.tagKeysHolder = tagKeysHolder;
            return this;
        }

        /**
         * Selects the recipe holder used by {@link RecipesGenerator}.
         *
         * <p>The reference replaces the previous selection. A {@code null}
         * selection is rejected when {@link #build()} validates the configuration.</p>
         *
         * @param recipesHolder the holder required by the recipe provider;
         *                      may be {@code null} here but must be set before {@link #build()}
         * @return this configuration builder
         */
        public DataProviderConfiguration recipeHolder(ReadableRecipeRegistrablesHolder recipesHolder) {
            this.collectors.recipesHolder = recipesHolder;
            return this;
        }

        /**
         * Validates all holder selections and returns the configured collector.
         *
         * <p>The returned collector is the same mutable instance owned by this
         * builder. Building does not create providers or run data generation.</p>
         *
         * @return the configured {@link DatagenCollectors} for selecting providers
         * @throws IllegalBuilderPatternConfigurationException if any required holder is missing
         */
        @Override
        public DatagenCollectors build() {
            if (this.collectors.translationsHolder == null)
                throw IllegalBuilderPatternConfigurationException.missing(
                    "translationsHolder"
                );

            if (this.collectors.tagKeysHolder == null)
                throw IllegalBuilderPatternConfigurationException.missing(
                    "registryDependentFactories"
                );

            if (this.collectors.recipesHolder == null)
                throw IllegalBuilderPatternConfigurationException.missing(
                    "recipeHolder"
                );

            return this.collectors;
        }


    }
}
