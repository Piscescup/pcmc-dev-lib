package io.github.piscescup.fabricmc.api.registers.items;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since
 */
public interface ItemPreRegistrable<T extends Item>
    extends PreRegistrable<Item, T, ItemPreRegistrable<T>, ItemPostRegistrable<T>>
{
    @NotNull
    ItemPreRegistrable<T> factory(@NotNull ItemFactory<T> factory);

    ItemPreRegistrable<T> properties(@NotNull Item.Properties properties);
}
