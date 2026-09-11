package io.github.piscescup.fabricmc;


import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.datagen.DatagenCollector;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(@NotNull FabricDataGenerator generator) {
        DatagenCollector.create()
            .langProvider(MCLanguage.EN_US)
            .generate(generator);
    }
}
