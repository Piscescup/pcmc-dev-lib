package io.github.piscescup.fabricmc.api.registers.item;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.world.item.Item;

/**
 * Provides post-registration operations for an {@link Item}.
 *
 * <p>Use this stage to add localized item names, inspect the registration
 * identity, collect the item, or obtain the registered instance.</p>
 *
 * @param <I> the concrete registered item type
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemPostRegistrable<I extends Item>
    extends PostRegistrable<Item, I, ItemPostRegistrable<I>>
{

}
