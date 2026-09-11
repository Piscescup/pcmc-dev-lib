package io.github.piscescup.fabricmc.api.registers.item;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.world.item.Item;

/**
 *
 * @author REN YuanTong
 * @since
 */
public interface ItemPostRegistrable<I extends Item>
    extends PostRegistrable<Item, I, ItemPostRegistrable<I>>
{

}
