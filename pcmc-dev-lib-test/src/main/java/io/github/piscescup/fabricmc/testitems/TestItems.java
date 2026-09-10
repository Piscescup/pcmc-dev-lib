package io.github.piscescup.fabricmc.testitems;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.registers.items.ItemRegisterFactory;
import net.minecraft.world.item.Item;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestItems {
    private static final ItemRegisterFactory ITEMS = ItemRegisterFactory.of(MOD_ID);

    public static final Item ITEM1 = ITEMS.pathSimple("item1")
        .properties(new Item.Properties())
        .register()
        .translate(MCLanguage.EN_US, "Item 1")
        .get();

    public static final CustomItem CUSTOM_ITEM = ITEMS.path("custom_item", CustomItem::new)
        .properties(new Item.Properties())
        .register()
        .translate(MCLanguage.EN_US, "Custom Item")
        .get();

    public static void registerModItems() {

    }

}
