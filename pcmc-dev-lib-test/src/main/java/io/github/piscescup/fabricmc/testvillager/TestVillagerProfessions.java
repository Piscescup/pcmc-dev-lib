package io.github.piscescup.fabricmc.testvillager;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.registers.villager.VillagerProfessionRegisterFactory;
import io.github.piscescup.fabricmc.testpoi.TestPOIs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;
import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;
import static io.github.piscescup.fabricmc.testvillager.TestTrades.TEST_TRADES;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class TestVillagerProfessions {
    private static final VillagerProfessionRegisterFactory PROFESSIONS = VillagerProfessionRegisterFactory.ofNamespace(MOD_ID);
    public static final VillagerProfession TEST_TRADER = PROFESSIONS.path("test_trader")
        .heldJobSite(TestPOIs.TEST_POI_ON_ACACIA_FENCE_GATE)
        .workSound(SoundEvents.ANVIL_USE)
        .tradeSetsByLevel(TEST_TRADES)
        .register()
        .translate(MCLanguage.EN_US, "Test Trader")
        .translate(MCLanguage.ZH_CN, "测试商人")
        .get();

    public static void registerTestVillagerProfessions() {
        MOD_LOGGER.info("Registering test villager professions");
    }

}
