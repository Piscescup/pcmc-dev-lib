package io.github.piscescup.fabricmc;

import io.github.piscescup.fabricmc.testblocks.TestBlocks;
import io.github.piscescup.fabricmc.testitems.TestItems;
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
    }
}
