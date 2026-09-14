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
 * Creates {@link CreativeModeTab} pre-registration builders within one namespace.
 *
 * <p>Each {@link #path(String)} call creates an independent builder. Configure
 * the icon, contents, and layout before registering the tab, then use its
 * post-registration stage to record a localized title.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * CreativeModeTabRegisterFactory TABS =
 *     CreativeModeTabRegisterFactory.ofNamespace("example");
 * CreativeModeTab tab = TABS.path("example_tab")
 *     .iconFromItem(Items.IRON_INGOT)
 *     .addItems(Items.IRON_INGOT, Items.GOLD_INGOT)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Example Tab")
 *     .get();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see CreativeModeTabRegister
 * @see CreativeModeTabPreRegistrable
 * @see io.github.piscescup.fabricmc.api.registers.creativetab.CreativeModeTabPostRegistrable
 */
public class CreativeModeTabRegisterFactory
    extends RegisterFactoryImpl<CreativeModeTab>
{
    /**
     * Creates a factory targeting {@link Registries#CREATIVE_MODE_TAB}.
     *
     * @param namespace the namespace for {@link CreativeModeTab} identifiers; must not be {@code null}
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    CreativeModeTabRegisterFactory(String namespace) {
        super(Registries.CREATIVE_MODE_TAB, namespace);
    }

    /**
     * Creates a new {@link CreativeModeTab} factory for the given namespace.
     *
     * @param namespace the namespace, usually a mod ID; must not be {@code null}
     * @return a new factory scoped to the creative mode tab registry and namespace
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    public static @NotNull CreativeModeTabRegisterFactory ofNamespace(@NotNull String namespace) {
        NullCheck.requireNonNull(namespace, "namespace");
        return new CreativeModeTabRegisterFactory(namespace);
    }

    /**
     * Starts a {@link CreativeModeTab} registration under this factory's namespace.
     *
     * <p>The path is combined with this factory's namespace to form the
     * {@link Identifier} retained by the new {@link CreativeModeTabRegister}.
     * The tab is constructed when its registration stage is completed.</p>
     *
     * @param path the tab path without a namespace prefix; must not be {@code null}
     * @return a new pre-registration stage for configuring the tab
     * @throws NullPointerException if {@code path} is {@code null}
     */
    public CreativeModeTabPreRegistrable path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        return new CreativeModeTabRegister(Identifier.fromNamespaceAndPath(namespace, path));
    }
}
