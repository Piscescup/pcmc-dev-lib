package io.github.piscescup.fabricmc.api.registers.items;

import net.minecraft.world.item.Item;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemFactory<T extends Item> {
    Item create(Item.Properties properties);

    @SuppressWarnings("unchecked")
    default T cast(Item.Properties properties) {
        return (T) create(properties);
    }
}
