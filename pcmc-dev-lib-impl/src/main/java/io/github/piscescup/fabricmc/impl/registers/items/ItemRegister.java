package io.github.piscescup.fabricmc.impl.registers.items;

import io.github.piscescup.fabricmc.api.registers.items.ItemFactory;
import io.github.piscescup.fabricmc.api.registers.items.ItemPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.items.ItemPreRegistrable;
import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ItemRegister<T extends Item>
    extends Register<Item, T, ItemPreRegistrable<T>, ItemPostRegistrable<T>>
    implements ItemPreRegistrable<T>, ItemPostRegistrable<T>
{
    private ItemFactory<T> itemFactory = Item::new;
    private Item.Properties itemProperties = new Item.Properties();

    private ItemRegister(@NotNull String namespace) {
        super(Registries.ITEM, BuiltInRegistries.ITEM, namespace);
        NullCheck.requireNonNull(itemFactory, "itemFactory");
    }

    @Override
    public @NotNull ItemPreRegistrable<T> factory(@NotNull ItemFactory<T> factory) {
        NullCheck.requireNonNull(factory, "factory");
        this.itemFactory = factory;
        return this;
    }

    @Override
    public ItemPreRegistrable<T> properties(@NotNull Item.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");

        this.itemProperties = properties;
        return this;
    }

    @Override
    public @NonNull ItemRegister<T> translate(@NotNull MCLanguage lang, @NotNull String translation) {
        return this;
    }

    /**
     * Registers the configured object and enters the
     * post-registration stage.
     *
     * @return the corresponding post-registration stage
     */
    @Override
    public @NonNull ItemPostRegistrable<T> register() {
        T item = itemFactory.cast(this.itemProperties
            .setId(this.resourceKey));

        this.thingToBeRegistered = Registry.register(
            registerRegistryCategory, resourceKey, item
        );
        return this;
    }
}
