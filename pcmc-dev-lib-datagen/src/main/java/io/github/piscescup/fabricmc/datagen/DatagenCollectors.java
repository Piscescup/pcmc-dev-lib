package io.github.piscescup.fabricmc.datagen;

import io.github.piscescup.exception.IllegalBuilderPatternConfigurationException;
import io.github.piscescup.fabricmc.store.lang.ReadableTranslationsHolder;
import io.github.piscescup.fabricmc.store.tag.ReadableTagKeysHolder;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.lang.LanguageProvider;
import io.github.piscescup.fabricmc.datagen.tag.TagProvider;
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
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class DatagenCollectors {
    private ReadableTranslationsHolder translationsHolder;

    private ReadableTagKeysHolder tagKeysHolder;


    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<? extends DataProvider>> registryDependentFactories = new ArrayList<>();

    private final List<DataProvider.Factory<? extends DataProvider>> factories = new ArrayList<>();


    @Contract(" -> new")
    @NotNull
    public static DataProviderConfiguration configuration() {
        return new DataProviderConfiguration();
    }

    public DatagenCollectors langProvider(MCLanguage language) {
        NullCheck.requireNonNull(language, "language");
        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new LanguageProvider(packOutput, lookupProvider, language, translationsHolder.translations(language))
        );
        return this;
    }

    public <T> DatagenCollectors tagProvider(ResourceKey<? extends Registry<T>> registry) {
        NullCheck.requireNonNull(registry, "registry");

        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new TagProvider<>(packOutput, registry, lookupProvider, this.tagKeysHolder)
        );

        return this;
    }


    public void generate(@NotNull FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        registryDependentFactories.forEach(pack::addProvider);
        factories.forEach(pack::addProvider);

    }

    /**
     *
     * @author REN YuanTong
     * @since 1.0.0
     */
    public static final class DataProviderConfiguration
        implements Builder<DatagenCollectors>
    {
        private DatagenCollectors collectors;


        DataProviderConfiguration() {
            this.collectors = new DatagenCollectors();
        }

        public DataProviderConfiguration translationHolder(ReadableTranslationsHolder translationsHolder) {
            this.collectors.translationsHolder = translationsHolder;
            return this;
        }

        public DataProviderConfiguration tagKeyHolder(ReadableTagKeysHolder tagKeysHolder) {
            this.collectors.tagKeysHolder = tagKeysHolder;
            return this;
        }

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

            return this.collectors;
        }


    }
}
