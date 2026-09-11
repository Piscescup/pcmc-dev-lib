package io.github.piscescup.fabricmc.impl.registers.item;

import io.github.piscescup.fabricmc.api.registers.item.ItemPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.item.ItemPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ItemRegisterFactory extends RegisterFactoryImpl<Item> {

    private final List<ItemPostRegistrable<?>> entries = new ArrayList<>();

    protected ItemRegisterFactory(String namespace) {
        super(Registries.ITEM, namespace);
    }

    @Contract("_ -> new")
    @NotNull
    public static ItemRegisterFactory ofNamespace(@NotNull String namespace) {
        return new ItemRegisterFactory(namespace);
    }

    public ItemPreRegistrable<Item> pathSimple(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return new ItemRegister<>(id, Item::new);
    }

    public <I extends Item> ItemPreRegistrable<I> path(@NotNull String path, @NotNull Function<Item.Properties, I> factory) {
        NullCheck.requireNonNull(path, "path");
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return new ItemRegister<>(id, factory);
    }
}
