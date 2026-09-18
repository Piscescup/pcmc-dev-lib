package io.github.piscescup.fabricmc.api.item;

import io.github.piscescup.fabricmc.api.PreRegistrable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 * Configures an {@link Item} before registration.
 *
 * <p>The factory that creates this stage selects the identifier and concrete
 * item type. This stage configures the {@link Item.Properties} passed to that
 * item's constructor. The supplied implementation starts with a fresh
 * {@code Item.Properties} instance when no properties are specified.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * public static final Item ITEM1 = ITEMS.path("item1")
 *     .properties(new Item.Properties())
 *     .register()
 *     .translate(MCLanguage.EN_US, "Item 1")
 *     .get();
 * }</pre>
 *
 * <p>Calling {@link #register()} constructs and registers the item, then returns
 * the matching {@link ItemPostRegistrable post-registration stage} for translation,
 * collection, and access to the result. Treat this as a stateful builder and
 * finish its configuration before registering it once.</p>
 *
 * @param <I> the concrete item type being registered; must be a subtype of
 *            {@link Item} and is preserved by the post-registration stage
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 * @see ItemPostRegistrable
 */
public interface ItemPreRegistrable<I extends Item>
    extends PreRegistrable<Item, I, ItemPreRegistrable<I>, ItemPostRegistrable<I>>
{
    /**
     * Sets the {@link Item.Properties} of the item to register.
     *
     * <p>The supplied implementation retains this instance, replacing any
     * previously selected properties. During {@link #register()}, it sets the
     * registration resource key on these properties before constructing the
     * item. Use a separate properties instance for each registration.</p>
     *
     * @param properties the properties to pass to the item factory
     * @return this pre-registration stage
     * @see #register()
     */
    ItemPreRegistrable<I> properties(@NotNull Item.Properties properties);
}
