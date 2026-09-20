package io.github.piscescup.fabricmc.testtags;

import io.github.piscescup.fabricmc.impl.registers.tag.TagKeyRegisterFactory;
import io.github.piscescup.fabricmc.testpoi.TestPOIs;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class TestPOITags {
    public static final TagKey<PoiType> JOB = TagKeyRegisterFactory.ofVanilla(PoiTypeTags.ACQUIRABLE_JOB_SITE)
        .addRegistryKey(PoiTypes.BEE_NEST)
        .addRegistryKey(TestPOIs.TEST_POI_ON_ACACIA_FENCE_GATE)
        .register()
        .get();

    public static void registerPOITags() {
        MOD_LOGGER.info("Registering test POI tags");
    }
}
