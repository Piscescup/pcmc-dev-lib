package io.github.piscescup.fabricmc;

import io.github.piscescup.fabricmc.testblocks.TestBlocks;
import io.github.piscescup.fabricmc.testcreativetabs.TestCreativeModeTabs;
import io.github.piscescup.fabricmc.testitems.TestItems;
import io.github.piscescup.fabricmc.testpoi.TestPOIs;
import io.github.piscescup.fabricmc.testrecipes.TestCraftingRecipes;
import io.github.piscescup.fabricmc.testtags.TestBlockTags;
import io.github.piscescup.fabricmc.testtags.TestItemTagKeys;
import net.fabricmc.api.ModInitializer;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestMod implements ModInitializer {
    @Override
    public void onInitialize() {
        TestItems.registerModItems();
        TestBlocks.registerModBlocks();
        TestCreativeModeTabs.registerModCreativeTabs();

        TestItemTagKeys.registerItemTags();
        TestBlockTags.registerBlockTags();

        TestCraftingRecipes.registerRecipes();

        TestPOIs.registerPOIs();
    }
}
