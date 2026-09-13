package io.github.piscescup.fabricmc.impl.registers.creativetab;

import io.github.piscescup.fabricmc.api.registers.creativetab.CreativeModeTabPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.creativetab.CreativeModeTabPreRegistrable;
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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class CreativeModeTabRegister
    extends Register<CreativeModeTab, CreativeModeTab, CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable>
    implements CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable
{
    private CreativeModeTab.Builder builder = CreativeModeTab.builder(null, -1);

    private Consumer<CreativeModeTab.ItemDisplayParameters> paramAction =
        parameters -> {};

    private final Map<Supplier<ItemStack>, CreativeModeTab.TabVisibility> entries =
        new LinkedHashMap<>();

    CreativeModeTabRegister(Identifier id) {
        super(BuiltInRegistries.CREATIVE_MODE_TAB, id);
    }

    @Override
    public CreativeModeTabPreRegistrable position(CreativeModeTab.Row row, int column) {
        this.builder = CreativeModeTab.builder(row, column);
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable icon(@NotNull Supplier<ItemStack> item) {
        NullCheck.requireNonNull(item, "item");
        builder.icon(item);
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable addBy(Consumer<CreativeModeTab.ItemDisplayParameters> param) {
        NullCheck.requireNonNull(param, "param");
        this.paramAction = paramAction.andThen(param);
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable addStack(Supplier<ItemStack> stack, CreativeModeTab.TabVisibility tabVisibility) {
        NullCheck.requireNonNull(stack, "stack");
        NullCheck.requireNonNull(tabVisibility, "tabVisibility");
        this.entries.put(stack, tabVisibility);
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable noScrollBar() {
        builder.noScrollBar();
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable hideTitle() {
        builder.hideTitle();
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable alignedRight() {
        builder.alignedRight();
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable background(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        builder.backgroundTexture(Identifier.fromNamespaceAndPath(id.getNamespace(), path));
        return this;
    }

    @Override
    public CreativeModeTabPreRegistrable backgroundWithDefaultNamespace(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        builder.backgroundTexture(Identifier.withDefaultNamespace(path));
        return this;
    }

    /**
     * Registers the configured object and enters the
     * post-registration stage.
     *
     * @return the corresponding post-registration stage
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

    @NotNull
    protected String translateKey() {
        return "itemGroup.%s.%s".formatted(id.getNamespace(), id.getPath());
    }
}
