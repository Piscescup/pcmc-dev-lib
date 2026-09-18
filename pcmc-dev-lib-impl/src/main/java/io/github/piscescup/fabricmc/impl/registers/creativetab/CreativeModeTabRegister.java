package io.github.piscescup.fabricmc.impl.registers.creativetab;

import io.github.piscescup.fabricmc.api.creativetab.CreativeModeTabPostRegistrable;
import io.github.piscescup.fabricmc.api.creativetab.CreativeModeTabPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Implements both registration stages for one {@link CreativeModeTab}.
 *
 * <p>Instances are created through {@link CreativeModeTabRegisterFactory}.
 * Builder options are applied immediately, while stack suppliers and display
 * parameter actions are retained for evaluation when the tab builds its contents.
 * Actions run in addition order before suppliers are evaluated in insertion order.</p>
 *
 * <p>{@link #register()} builds and registers the tab and returns this instance
 * as a {@link CreativeModeTabPostRegistrable}. The title uses the translation key
 * {@code itemGroup.<namespace>.<path>}. Complete configuration before registering
 * once, then record localized titles through the returned stage.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see CreativeModeTabRegisterFactory
 * @see CreativeModeTabPreRegistrable
 * @see CreativeModeTabPostRegistrable
 */
public final class CreativeModeTabRegister
    extends Register<CreativeModeTab, CreativeModeTab, CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable>
    implements CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable
{
    /**
     * Mutable {@link CreativeModeTab.Builder} used to configure and build the tab.
     * Initially uses {@code null} as its row and {@code -1} as its column;
     * {@link #position(CreativeModeTab.Row, int)} replaces the builder entirely.
     */
    private CreativeModeTab.Builder builder = CreativeModeTab.builder(null, -1);

    /**
     * Composed action receiving the current {@link CreativeModeTab.ItemDisplayParameters}.
     * Starts as a no-op and is extended in addition order by {@link #addBy(Consumer)}.
     */
    private Consumer<CreativeModeTab.ItemDisplayParameters> paramAction =
        parameters -> {};

    /**
     * Display-stack suppliers mapped to {@link CreativeModeTab.TabVisibility}.
     * The {@link LinkedHashMap} preserves insertion order; adding an equal supplier
     * replaces its visibility. Suppliers are evaluated when tab contents are built.
     */
    private final Map<Supplier<ItemStack>, CreativeModeTab.TabVisibility> entries =
        new LinkedHashMap<>();

    /**
     * Creates a {@link CreativeModeTab} builder for the selected {@link Identifier}.
     *
     * @param id the namespaced {@link CreativeModeTab} identifier; must not be {@code null}
     * @throws NullPointerException if {@code id} is {@code null}
     */
    CreativeModeTabRegister(Identifier id) {
        super(BuiltInRegistries.CREATIVE_MODE_TAB, id);
    }

    /**
     * Replaces the {@link CreativeModeTab.Builder} with one using the selected menu position.
     *
     * <p>Set the position before other builder options: replacing the builder
     * discards its icon, title-visibility, scroll-bar, alignment, and background
     * settings. Retained entries and display-parameter actions are unaffected.</p>
     *
     * @param row    the {@link CreativeModeTab.Row}, passed directly to the tab builder
     * @param column the column within that row
     * @return this instance as its pre-registration stage
     */
    @Override
    public CreativeModeTabPreRegistrable position(CreativeModeTab.Row row, int column) {
        this.builder = CreativeModeTab.builder(row, column);
        return this;
    }

    /**
     * Passes an {@link ItemStack} icon supplier to the {@link CreativeModeTab.Builder}.
     *
     * <p>The supplier is retained by the builder and evaluated when the icon is
     * requested. Passing a non-null supplier does not validate its later result.</p>
     *
     * @param item the supplier returning a non-null {@link ItemStack}; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code item} is {@code null}
     */
    @Override
    public CreativeModeTabPreRegistrable icon(@NotNull Supplier<ItemStack> item) {
        NullCheck.requireNonNull(item, "item");
        builder.icon(item);
        return this;
    }

    /**
     * Appends an action to run before the tab's stack suppliers are evaluated.
     *
     * <p>Actions receive the current display parameters whenever contents are
     * built and run in addition order.</p>
     *
     * @param param the action consuming {@link CreativeModeTab.ItemDisplayParameters}; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code param} is {@code null}
     */
    @Override
    public CreativeModeTabPreRegistrable addBy(Consumer<CreativeModeTab.ItemDisplayParameters> param) {
        NullCheck.requireNonNull(param, "param");
        this.paramAction = paramAction.andThen(param);
        return this;
    }

    /**
     * Stores an {@link ItemStack} supplier and its {@link CreativeModeTab.TabVisibility}.
     *
     * <p>Suppliers are map keys. Adding an equal supplier updates its visibility
     * without changing its insertion position. The supplier is invoked each time
     * tab contents are built and must return a valid display stack.</p>
     *
     * @param stack         the supplier of the display {@link ItemStack}; must not be {@code null}
     * @param tabVisibility the parent/search-tab visibility; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code stack} or {@code tabVisibility} is {@code null}
     */
    @Override
    public CreativeModeTabPreRegistrable addStack(Supplier<ItemStack> stack, CreativeModeTab.TabVisibility tabVisibility) {
        NullCheck.requireNonNull(stack, "stack");
        NullCheck.requireNonNull(tabVisibility, "tabVisibility");
        this.entries.put(stack, tabVisibility);
        return this;
    }

    /**
     * Applies {@link CreativeModeTab.Builder#noScrollBar()} to hide the content scroll bar.
     *
     * @return this instance as its pre-registration stage
     */
    @Override
    public CreativeModeTabPreRegistrable noScrollBar() {
        builder.noScrollBar();
        return this;
    }

    /**
     * Applies {@link CreativeModeTab.Builder#hideTitle()} to hide the title.
     *
     * @return this instance as its pre-registration stage
     */
    @Override
    public CreativeModeTabPreRegistrable hideTitle() {
        builder.hideTitle();
        return this;
    }

    /**
     * Applies {@link CreativeModeTab.Builder#alignedRight()} to enable right alignment.
     *
     * @return this instance as its pre-registration stage
     */
    @Override
    public CreativeModeTabPreRegistrable alignedRight() {
        builder.alignedRight();
        return this;
    }

    /**
     * Selects a background texture in this tab's namespace.
     *
     * <p>The path and this tab's namespace form the {@link Identifier} passed
     * to {@link CreativeModeTab.Builder#backgroundTexture(Identifier)}.</p>
     *
     * @param path the texture path without a namespace prefix; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code path} is {@code null}
     * @see #backgroundWithDefaultNamespace(String)
     */
    @Override
    public CreativeModeTabPreRegistrable background(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        builder.backgroundTexture(Identifier.fromNamespaceAndPath(id.getNamespace(), path));
        return this;
    }

    /**
     * Selects a background texture in the {@code minecraft} namespace.
     *
     * <p>The {@link Identifier} is constructed with
     * {@link Identifier#withDefaultNamespace(String)} and passed to the tab builder.</p>
     *
     * @param path the texture path without a namespace prefix; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code path} is {@code null}
     * @see #background(String)
     */
    @Override
    public CreativeModeTabPreRegistrable backgroundWithDefaultNamespace(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        builder.backgroundTexture(Identifier.withDefaultNamespace(path));
        return this;
    }

    /**
     * Builds the configured {@link CreativeModeTab} and registers it in
     * {@link BuiltInRegistries#CREATIVE_MODE_TAB}.
     *
     * <p>Installs a translatable title and a content callback that invokes the
     * retained actions and suppliers. The registered tab is stored for
     * subsequent access through {@link #get()}. Each call builds another tab and
     * attempts registration under the same identifier; reuse is not guarded.</p>
     *
     * @return this instance as the creative mode tab post-registration stage
     */
    @Override
    public @NotNull CreativeModeTabPostRegistrable register() {
        CreativeModeTab tab = builder
            .title(Component.translatable(translateKey()))
            .displayItems((params, output) -> {
                paramAction.accept(params);
                entries.forEach((entry, tabVisibility) -> {
                    output.accept(entry.get(), tabVisibility);
                });
            })
            .build();

        this.thingToBeRegistered = Registry.register(
            this.registry,
            this.resourceKey,
            tab
        );

        return this;
    }

    /**
     * Derives the tab title's translation key from its identifier.
     *
     * @return a key in the form {@code itemGroup.<namespace>.<path>}
     */
    @NotNull
    protected String translateKey() {
        return "itemGroup.%s.%s".formatted(id.getNamespace(), id.getPath());
    }
}
