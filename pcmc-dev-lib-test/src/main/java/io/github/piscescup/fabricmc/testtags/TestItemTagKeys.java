package io.github.piscescup.fabricmc.testtags;

import io.github.piscescup.fabricmc.impl.registers.tag.TagKeyRegisterFactory;
import io.github.piscescup.fabricmc.testitems.TestItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;
import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class TestItemTagKeys {
    private static final TagKeyRegisterFactory<Item> ITEM_TAG_KEYS =
        TagKeyRegisterFactory.itemsTagOfNamespace(MOD_ID);

    public static final TagKey<Item> TEST_ITEM_TAG = ITEM_TAG_KEYS.path("test1")
        .addEntries(Items.PAPER, Items.GOLD_INGOT, Items.IRON_INGOT, Items.DIAMOND)
        .addEntries(TestItems.CUSTOM_ITEM, TestItems.ITEM1)
        .register()
        .get();

    public static void registerItemTags() {
        MOD_LOGGER.info("Registering item Tags");
    }
}
