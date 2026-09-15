package io.github.piscescup.fabricmc;


import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.DatagenCollectors;
import io.github.piscescup.fabricmc.impl.store.MutableReciperegistrablesHolder;
import io.github.piscescup.fabricmc.impl.store.lang.MutableTranslationsHolder;
import io.github.piscescup.fabricmc.impl.store.tag.MutableTagKeysHolder;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(@NotNull FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        DatagenCollectors.configuration()
            .translationHolder(MutableTranslationsHolder.INSTANCE)
            .tagKeyHolder(MutableTagKeysHolder.INSTANCE)
            .recipeHolder(MutableReciperegistrablesHolder.INSTANCE)
            .build()
            .langProvider(MCLanguage.EN_US)
            .tagProvider(Registries.ITEM)
            .tagProvider(Registries.BLOCK)
            .recipesProvider()
            .generate(pack);
    }
}
