package io.github.piscescup.fabricmc.datagen;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.lang.LanguageProvider;
import io.github.piscescup.fabricmc.datagen.tag.TagProvider;
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
public final class DatagenCollector {
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<? extends DataProvider>> registryDependentFactories = new ArrayList<>();
    private final List<DataProvider.Factory<? extends DataProvider>> factories = new ArrayList<>();

    @Contract(" -> new")
    @NotNull
    public static DatagenCollector create() {
        return new DatagenCollector();
    }

    public DatagenCollector langProvider(MCLanguage language) {
        NullCheck.requireNonNull(language, "language");
        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new LanguageProvider(packOutput, lookupProvider, language)
        );
        return this;
    }

    public <T> DatagenCollector tagProvider(ResourceKey<? extends Registry<T>> registry) {
        NullCheck.requireNonNull(registry, "registry");

        registryDependentFactories.add(
            (packOutput, lookupProvider) ->
                new TagProvider<>(packOutput, registry, lookupProvider)
        );

        return this;
    }


    public void generate(@NotNull FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        registryDependentFactories.forEach(pack::addProvider);
        factories.forEach(pack::addProvider);

    }

}
