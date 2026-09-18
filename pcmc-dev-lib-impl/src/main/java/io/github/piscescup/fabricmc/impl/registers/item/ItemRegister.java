package io.github.piscescup.fabricmc.impl.registers.item;

import io.github.piscescup.fabricmc.api.item.ItemPostRegistrable;
import io.github.piscescup.fabricmc.api.item.ItemPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

/**
 * Implements the pre-registration and post-registration stages for one {@link Item}.
 *
 * <p>Instances are created through {@link ItemRegisterFactory}. The selected
 * item factory and properties are retained until {@link #register()}, which
 * creates the item, adds it to {@link BuiltInRegistries#ITEM}, and returns this
 * same instance as an {@link ItemPostRegistrable}.</p>
 *
 * <p>The initial properties are a fresh {@link Item.Properties} instance.
 * Configuration is mutable and should be completed before registration.
 * Register each builder once, then use the returned stage for translations
 * and access to the registered item.</p>
 *
 * @param <I> the concrete {@link Item} type constructed and registered by this builder
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ItemRegisterFactory
 * @see ItemPreRegistrable
 * @see ItemPostRegistrable
 */
public class ItemRegister<I extends Item>
    extends Register<Item, I, ItemPreRegistrable<I>, ItemPostRegistrable<I>>
    implements ItemPreRegistrable<I>, ItemPostRegistrable<I>
{
    /**
     * Factory invoked during {@link #register()} to construct the {@link Item}
     * from its retained properties. Never {@code null} after construction.
     */
    private Function<Item.Properties, I> itemFactory;

    /**
     * Mutable {@link Item.Properties} retained until {@link #register()}.
     * Defaults to a fresh instance and is replaced by {@link #properties(Item.Properties)}.
     */
    private Item.Properties itemProperties = new Item.Properties();

    /**
     * Creates an {@link Item} builder with default {@link Item.Properties}.
     *
     * @param id      the {@link Item} identifier; must not be {@code null}
     * @param factory the function constructing the {@link Item} during registration; must not be {@code null}
     * @throws NullPointerException if {@code id} or {@code factory} is {@code null}
     */
    ItemRegister(@NotNull Identifier id, Function<Item.Properties, I> factory) {
        NullCheck.requireNonNull(id, "id");
        super(BuiltInRegistries.ITEM, id);
        this.itemFactory = NullCheck.requireNonNull(factory, "factory");
    }

    /**
     * Replaces the {@link Item.Properties} retained for {@link Item} construction.
     *
     * <p>The supplied instance is retained directly and receives the item's
     * resource key during registration. Use a separate instance per builder.</p>
     *
     * @param properties the {@link Item.Properties} to pass to the factory; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code properties} is {@code null}
     */
    @Override
    public ItemPreRegistrable<I> properties(@NotNull Item.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");

        this.itemProperties = properties;
        return this;
    }

    /**
     * Returns the registered {@link Item}'s description ID for language data generation.
     *
     * <p>The inherited registration result is dereferenced directly. Calling
     * this method before a successful {@link #register()} fails.</p>
     *
     * @return the description ID of the successfully registered {@link Item}
     * @throws NullPointerException if the registration result is still {@code null}
     */
    @Override
    protected String translateKey() {
        return thingToBeRegistered.getDescriptionId();
    }

    /**
     * Constructs the configured {@link Item} and registers it in {@link BuiltInRegistries#ITEM}.
     *
     * <p>Assigns the resource key to the retained properties, invokes the item
     * factory, and stores the registry result. Repeated calls are not guarded
     * and can attempt another registration under the same identifier.</p>
     *
     * @return this instance as the item post-registration stage
     * @throws NullPointerException if the retained properties or factory is
     *         {@code null}, or the factory produces a {@code null} item
     * @see ItemPostRegistrable
     */
    @Override
    public @NonNull ItemPostRegistrable<I> register() {
        I item = itemFactory.apply(this.itemProperties
            .setId(this.resourceKey));

        this.thingToBeRegistered = Registry.register(
            this.registry,
            this.id,
            item
        );

        return this;
    }


}
