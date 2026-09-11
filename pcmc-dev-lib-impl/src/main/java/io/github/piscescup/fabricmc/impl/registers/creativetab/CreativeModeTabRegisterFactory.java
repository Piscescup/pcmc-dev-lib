package io.github.piscescup.fabricmc.impl.registers.creativetab;

import io.github.piscescup.fabricmc.api.registers.creativetab.CreativeModeTabPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class CreativeModeTabRegisterFactory
    extends RegisterFactoryImpl<CreativeModeTab>
{
    CreativeModeTabRegisterFactory(String namespace) {
        super(Registries.CREATIVE_MODE_TAB, namespace);
    }

    @Contract("_ -> new")
    public static @NotNull CreativeModeTabRegisterFactory ofNamespace(@NotNull String namespace) {
        NullCheck.requireNonNull(namespace, "namespace");
        return new CreativeModeTabRegisterFactory(namespace);
    }

    public CreativeModeTabPreRegistrable path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        return new CreativeModeTabRegister(Identifier.fromNamespaceAndPath(namespace, path));
    }
}
