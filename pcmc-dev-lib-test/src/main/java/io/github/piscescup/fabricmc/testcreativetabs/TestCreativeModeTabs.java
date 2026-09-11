package io.github.piscescup.fabricmc.testcreativetabs;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.registers.creativetab.CreativeModeTabRegisterFactory;
import io.github.piscescup.fabricmc.testitems.TestItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;
import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestCreativeModeTabs {
    private static final CreativeModeTabRegisterFactory CREATIVE_TABS =
        CreativeModeTabRegisterFactory.ofNamespace(MOD_ID);

    public static final CreativeModeTab TEST_TAB1 = CREATIVE_TABS.path("test_itemgroup")
        .iconFromItem(() -> TestItems.CUSTOM_ITEM)
        .addItems(TestItems.CUSTOM_ITEM)
        .addItems(Items.IRON_INGOT, Items.GOLD_INGOT)
        .register()
        .translate(MCLanguage.EN_US, "Test ItemGroup 1")
        .get();

    public static void registerModCreativeTabs() {
        MOD_LOGGER.info("Register Mod Creative Tabs");
    }
}
