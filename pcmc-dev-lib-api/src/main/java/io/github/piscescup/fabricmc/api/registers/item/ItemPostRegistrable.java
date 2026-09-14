package io.github.piscescup.fabricmc.api.registers.item;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.world.item.Item;

/**
 * Provides post-registration operations for an {@link Item}.
 *
 * <p>This stage is returned by {@link ItemPreRegistrable#register()} after the
 * item has been constructed and registered. Use the inherited methods to record
 * localized names for language data generation, inspect the identifier and
 * resource key, collect the item, or retrieve it through {@link #get()}.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * public static final Item ITEM1 = ITEMS.path("item1")
 *     .register()
 *     .translate(MCLanguage.EN_US, "Item 1")
 *     .collectsTo(REGISTERED_ITEMS)
 *     .get();
 * }</pre>
 *
 * <p>The translation key is the registered item's description ID in the
 * supplied implementation. Fluent customization methods return this item stage,
 * and {@link #get()} retains the concrete item type.</p>
 *
 * @param <I> the concrete registered item type; must be a subtype of {@link Item}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ItemPreRegistrable
 * @see PostRegistrable
 */
public interface ItemPostRegistrable<I extends Item>
    extends PostRegistrable<Item, I, ItemPostRegistrable<I>>
{

}
