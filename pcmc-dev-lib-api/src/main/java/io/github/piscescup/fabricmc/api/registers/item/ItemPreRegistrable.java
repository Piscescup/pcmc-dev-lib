package io.github.piscescup.fabricmc.api.registers.item;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 * Configures an {@link Item} before registration.
 *
 * <p>Both standard and custom item types can use this stage.
 * {@link Item.Properties} are optional and may be supplied before registration:</p>
 * <pre>{@code
 * public static final Item ITEM1 = ITEMS.pathSimple("item1")
 *     .properties(new Item.Properties())
 *     .register()
 *     .translate(MCLanguage.EN_US, "Item 1")
 *     .get();
 * }</pre>
 *
 * @param <I> the concrete item type being registered
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemPreRegistrable<I extends Item>
    extends PreRegistrable<Item, I, ItemPreRegistrable<I>, ItemPostRegistrable<I>>
{
    /**
     * Sets the {@link Item.Properties} of the item to register.
     *
     * @param properties the item properties
     * @return this pre-registration stage
     */
    ItemPreRegistrable<I> properties(@NotNull Item.Properties properties);
}
