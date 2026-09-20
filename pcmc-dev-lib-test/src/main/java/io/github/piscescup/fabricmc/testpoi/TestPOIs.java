package io.github.piscescup.fabricmc.testpoi;

import io.github.piscescup.fabricmc.impl.registers.poi.POIRegisterFactory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Blocks;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;
import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class TestPOIs {
    private static final POIRegisterFactory POIS = POIRegisterFactory.ofNamespace(MOD_ID);

    public static final ResourceKey<PoiType> TEST_POI_ON_ACACIA_FENCE_GATE = POIS.path("test_poi1")
        .matchingStatesFrom(Blocks.ACACIA_FENCE_GATE)
        .maxTickets(12)
        .validRange(12)
        .register()
        .resourceKey();


    public static void registerPOIs() {
        MOD_LOGGER.info("Registering POIs");
    }
}
