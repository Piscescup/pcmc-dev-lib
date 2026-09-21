package io.github.piscescup.fabricmc.datagen;

import io.github.piscescup.exception.IllegalBuilderPatternConfigurationException;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.lang.LanguageGenerator;
import io.github.piscescup.fabricmc.datagen.recipe.RecipesGenerator;
import io.github.piscescup.fabricmc.datagen.tag.TagGenerator;
import io.github.piscescup.fabricmc.datagen.tag.VillagerTradeTagGenerator;
import io.github.piscescup.fabricmc.datagen.trade.TradeSetRegistryGenerator;
import io.github.piscescup.fabricmc.datagen.trade.VillagerTradeDatagenModel;
import io.github.piscescup.fabricmc.datagen.trade.VillagerTradeRegistryGenerator;
import io.github.piscescup.fabricmc.store.lang.ReadableTranslationsHolder;
import io.github.piscescup.fabricmc.store.recipe.ReadableRecipeRegistrablesHolder;
import io.github.piscescup.fabricmc.store.tag.ReadableTagKeysHolder;
import io.github.piscescup.fabricmc.store.villager.trade.ReadableVillagerTradesHolder;
import io.github.piscescup.interfaces.Builder;
import io.github.piscescup.util.validation.NullCheck;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Configures language, tag, recipe, and villager trade providers for a Fabric data pack.
 *
 * <p>Start with {@link #configuration()}, supply the readable translation,
 * tag, recipe, and villager trade holders, and call {@link DataProviderConfiguration#build()}.
 * Then select the desired data providers before attaching
 * the providers to a {@link FabricDataGenerator} through
 * {@link #generate(FabricDataGenerator.Pack)}.</p>
 *
 * <p>Typical usage in a data-generator entrypoint:</p>
 * <pre>{@code
 * FabricDataGenerator.Pack pack = generator.createPack();
 *
 * DataGeneratorCollectors.configuration()
 *     .translationHolder(MutableTranslationsHolder.INSTANCE)
 *     .tagKeyHolder(MutableTagKeysHolder.INSTANCE)
 *     .recipeHolder(MutableReciperegistrablesHolder.INSTANCE)
 *     .villagerTradesHolder(MutableVillagerTradeHolder.INSTANCE)
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
 * <p>{@link #villagerTradesProvider()} also snapshots the current trade
 * declarations and queues dynamic-registry bootstraps. Select it after profession
 * registration, then call {@link #buildRegistry(RegistrySetBuilder)} from the
 * data-generator entrypoint's registry-building hook and {@link #generate(FabricDataGenerator.Pack)}
 * when attaching providers. Use the same collector instance for both hooks.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see DataProviderConfiguration
 * @see LanguageGenerator
 * @see TagGenerator
 * @see RecipesGenerator
 * @see VillagerTradeDatagenModel
 */
public final class DataGeneratorCollectors {
    /**
     * The {@link ReadableTranslationsHolder} selected by the configuration builder.
     * Initially {@code null}; queued {@link LanguageGenerator} factories consult
     * this reference when they are invoked.
     */
    private ReadableTranslationsHolder translationsHolder;

    /**
     * The {@link ReadableTagKeysHolder} selected by the configuration builder.
     * Initially {@code null}; queued {@link TagGenerator} factories consult
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
     * Trade metadata source, initially {@code null}. Registry declarations are
     * captured by {@link #villagerTradesProvider()}, while the tag provider
     * reads this holder during generation.
     */
    private ReadableVillagerTradesHolder villagerTradesHolder;

    /**
     * Ordered {@link FabricDataGenerator.Pack.RegistryDependentFactory} queue.
     * Initially empty; {@link #langProvider(MCLanguage)},
     * {@link #tagProvider(ResourceKey)}, {@link #recipesProvider()}, and
     * {@link #villagerTradesProvider()} append factories for
     * {@link #generate(FabricDataGenerator.Pack)}.
     */
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<? extends DataProvider>> registryDependentFactories = new ArrayList<>();

    /**
     * Ordered {@link DataProvider.Factory} queue processed after registry-dependent factories.
     * The current public configuration methods do not populate this queue.
     */
    private final List<DataProvider.Factory<? extends DataProvider>> factories = new ArrayList<>();

    /**
     * Dynamic-registry bootstraps queued by {@link #villagerTradesProvider()}
     * and applied through {@link #buildRegistry(RegistrySetBuilder)}.
     */
    private final List<Consumer<RegistrySetBuilder>> registryConfigurations =
        new ArrayList<>();

    /**
     * Creates an unconfigured collector with empty provider queues.
     *
     * <p>All four holder references are initially {@code null}. Use
     * {@link #configuration()} to select and validate the holders before
     * attaching providers to a {@link FabricDataGenerator}.</p>
     *
     * @see #configuration()
     */
    public DataGeneratorCollectors() {}


    /**
     * Starts a new configuration with independent provider queues.
     *
     * @return a new configuration for selecting the translation, tag, recipe, and trade holders
     */
    @Contract(" -> new")
    @NotNull
    public static DataProviderConfiguration configuration() {
        return new DataProviderConfiguration();
    }

    /**
     * Queues a {@link LanguageGenerator} for the selected {@link MCLanguage}.
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
    public DataGeneratorCollectors langProvider(MCLanguage language) {
        NullCheck.requireNonNull(language, "language");
        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new LanguageGenerator(packOutput, lookupProvider, language, translationsHolder.translations(language))
        );
        return this;
    }

    /**
     * Queues a {@link TagGenerator} for the selected {@link Registry}.
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
    public <T> DataGeneratorCollectors tagProvider(ResourceKey<? extends Registry<T>> registry) {
        NullCheck.requireNonNull(registry, "registry");

        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new TagGenerator<>(packOutput, registry, lookupProvider, this.tagKeysHolder)
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
    public DataGeneratorCollectors recipesProvider() {
        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new RecipesGenerator(packOutput, lookupProvider, this.recipesHolder)
        );

        return this;
    }

    /**
     * Queues generation of villager trades, trade sets, and their level tags.
     *
     * <p>This call immediately snapshots the holder into a
     * {@link VillagerTradeDatagenModel} and queues its registry bootstraps. Call
     * it once after profession registration has populated the holder. Apply the
     * bootstraps through {@link #buildRegistry(RegistrySetBuilder)} and attach
     * the three providers through {@link #generate(FabricDataGenerator.Pack)}.</p>
     *
     * <p>The tag provider consults the holder later, so finish all trade
     * declarations before this call to keep registry and tag output consistent.</p>
     *
     * @return this collector for fluent provider selection
     * @throws NullPointerException if the villager trade holder is unset
     * @throws IllegalStateException if the holder declares duplicate trade or trade-set keys
     * @see DataProviderConfiguration#villagerTradesHolder(ReadableVillagerTradesHolder)
     */
    public DataGeneratorCollectors villagerTradesProvider() {
        VillagerTradeDatagenModel model =
            VillagerTradeDatagenModel.create(villagerTradesHolder);

        this.registryConfigurations.add(
            model::registerBootstraps
        );

        // data/<namespace>/villager_trade/*.json
        this.registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new VillagerTradeRegistryGenerator(
                    packOutput,
                    lookupProvider,
                    model
                )
        );

        // data/<namespace>/trade_set/*.json
        this.registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new TradeSetRegistryGenerator(
                    packOutput,
                    lookupProvider,
                    model
                )
        );

        // data/<namespace>/tags/villager_trade/*.json
        this.registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new VillagerTradeTagGenerator(
                    packOutput,
                    lookupProvider,
                    villagerTradesHolder
                )
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
        NullCheck.requireNonNull(pack, "pack");
        registryDependentFactories.forEach(pack::addProvider);
        factories.forEach(pack::addProvider);

    }

    /**
     * Adds the queued dynamic-registry bootstraps to Fabric's registry builder.
     *
     * <p>Invoke this method from the data-generator entrypoint's registry-building
     * hook after selecting {@link #villagerTradesProvider()}. The queue is
     * retained, so each call applies all configured bootstraps again.</p>
     *
     * @param registryBuilder the registry builder receiving the queued bootstraps
     * @throws NullPointerException if {@code registryBuilder} is {@code null}
     */
    public void buildRegistry(@NotNull RegistrySetBuilder registryBuilder) {
        NullCheck.requireNonNull(registryBuilder, "registryBuilder");
        this.registryConfigurations.forEach(c -> c.accept(registryBuilder));
    }

    /**
     * Selects the readable stores required by a {@link DataGeneratorCollectors} instance.
     *
     * <p>All four holders must be supplied before {@link #build()}, even when only
     * one provider category will be used. The builder owns one collector and
     * returns that same instance on each successful build. Holder references
     * are assigned directly and are not copied or frozen.</p>
     *
     * @author REN YuanTong
     * @since 1.0.0
     * @see DataGeneratorCollectors#configuration()
     */
    public static final class DataProviderConfiguration
        implements Builder<DataGeneratorCollectors>
    {
        /**
         * The {@link DataGeneratorCollectors} created with this configuration.
         * Setters mutate this same instance, and each successful {@link #build()}
         * returns it without copying its holders or provider queues.
         */
        private final DataGeneratorCollectors collectors;


        /**
         * Creates a configuration owning a new, initially unconfigured {@link DataGeneratorCollectors}.
         */
        DataProviderConfiguration() {
            this.collectors = new DataGeneratorCollectors();
        }

        /**
         * Selects the {@link ReadableTranslationsHolder} used to obtain per-language translations.
         *
         * <p>The reference replaces the previous selection. A {@code null}
         * selection is rejected when {@link #build()} validates the configuration.</p>
         *
         * @param translationsHolder the holder required by {@link LanguageGenerator};
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
         * @param tagKeysHolder the holder required by {@link TagGenerator};
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
         * Selects the metadata holder used by villager trade data generation.
         *
         * <p>The reference replaces the previous selection. A {@code null}
         * selection is rejected when {@link #build()} validates the configuration.</p>
         *
         * @param tradesHolder the trade metadata holder; must be set before {@link #build()}
         * @return this configuration builder
         * @see DataGeneratorCollectors#villagerTradesProvider()
         */
        public DataProviderConfiguration villagerTradesHolder(ReadableVillagerTradesHolder tradesHolder) {
            this.collectors.villagerTradesHolder = tradesHolder;
            return this;
        }


        /**
         * Validates all holder selections and returns the configured collector.
         *
         * <p>The returned collector is the same mutable instance owned by this
         * builder. Building does not create providers or run data generation.</p>
         *
         * @return the configured {@link DataGeneratorCollectors} for selecting providers
         * @throws IllegalBuilderPatternConfigurationException if any required holder is missing
         */
        @Override
        public DataGeneratorCollectors build() {
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

            if (this.collectors.villagerTradesHolder == null)
                throw IllegalBuilderPatternConfigurationException.missing(
                    "villagerTradesHolder"
                );

            return this.collectors;
        }

    }
}
