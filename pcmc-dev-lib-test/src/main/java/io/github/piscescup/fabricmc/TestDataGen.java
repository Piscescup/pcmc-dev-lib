package io.github.piscescup.fabricmc;


import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.DataGeneratorCollectors;
import io.github.piscescup.fabricmc.impl.store.MutableReciperegistrablesHolder;
import io.github.piscescup.fabricmc.impl.store.lang.MutableTranslationsHolder;
import io.github.piscescup.fabricmc.impl.store.tag.MutableTagKeysHolder;
import io.github.piscescup.fabricmc.impl.store.village.trade.MutableVillagerTradeHolder;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestDataGen implements DataGeneratorEntrypoint {
    private final DataGeneratorCollectors collectors = DataGeneratorCollectors.configuration()
        .translationHolder(MutableTranslationsHolder.INSTANCE)
            .tagKeyHolder(MutableTagKeysHolder.INSTANCE)
            .recipeHolder(MutableReciperegistrablesHolder.INSTANCE)
            .villagerTradesHolder(MutableVillagerTradeHolder.INSTANCE)
            .build()
            .langProvider(MCLanguage.EN_US)
            .tagProvider(Registries.ITEM)
            .tagProvider(Registries.BLOCK)
            .tagProvider(Registries.POINT_OF_INTEREST_TYPE)
            .recipesProvider()
            .villagerTradesProvider();


    @Override
    public void onInitializeDataGenerator(@NotNull FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        this.collectors.generate(pack);
    }

    @Override
    public void buildRegistry(@NotNull RegistrySetBuilder registryBuilder) {
        this.collectors.buildRegistry(registryBuilder);
    }
}
