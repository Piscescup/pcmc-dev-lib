package io.github.piscescup.fabricmc.api.registers.items;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.world.item.Item;

/**
 *
 * @author REN YuanTong
 * @since
 */
public interface ItemPostRegistrable<T extends Item>
    extends PostRegistrable<Item, T, ItemPostRegistrable<T>>
{

}
