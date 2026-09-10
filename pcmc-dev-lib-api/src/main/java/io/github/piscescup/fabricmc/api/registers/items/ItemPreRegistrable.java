package io.github.piscescup.fabricmc.api.registers.items;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemPreRegistrable<I extends Item>
    extends PreRegistrable<Item, I, ItemPreRegistrable<I>, ItemPostRegistrable<I>>
{

    ItemPreRegistrable<I> properties(@NotNull Item.Properties properties);
}
