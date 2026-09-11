package io.github.piscescup.fabricmc.impl.registers.item;

import io.github.piscescup.fabricmc.api.registers.item.ItemPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.item.ItemPreRegistrable;
import io.github.piscescup.fabricmc.constants.MCLanguage;
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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ItemRegister<I extends Item>
    extends Register<Item, I, ItemPreRegistrable<I>, ItemPostRegistrable<I>>
    implements ItemPreRegistrable<I>, ItemPostRegistrable<I>
{
    private Function<Item.Properties, I> itemFactory;
    private Item.Properties itemProperties = new Item.Properties();

    ItemRegister(@NotNull Identifier id, Function<Item.Properties, I> factory) {
        NullCheck.requireNonNull(id, "id");
        super(BuiltInRegistries.ITEM, id);
        this.itemFactory = NullCheck.requireNonNull(factory, "factory");
    }

    @Override
    public ItemPreRegistrable<I> properties(@NotNull Item.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");

        this.itemProperties = properties;
        return this;
    }

    @Override
    public @NonNull ItemRegister<I> translate(@NotNull MCLanguage lang, @NotNull String translation) {
        return this;
    }

    /**
     * Registers the configured object and enters the
     * post-registration stage.
     *
     * @return the corresponding post-registration stage
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
