package io.github.piscescup.fabricmc.api.registers.creativetab;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface CreativeModeTabPreRegistrable
    extends PreRegistrable<CreativeModeTab, CreativeModeTab, CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable>
{
    CreativeModeTabPreRegistrable position(CreativeModeTab.Row row, int column);

    CreativeModeTabPreRegistrable icon(@NotNull Supplier<ItemStack> item);

    default CreativeModeTabPreRegistrable iconFromItem(@Nullable ItemLike item) {
        Supplier<ItemStack> icon = item == null ?
            () -> ItemStack.EMPTY:
            () -> new ItemStack(item.asItem());
        return icon(icon);
    }

    CreativeModeTabPreRegistrable addBy(Consumer<CreativeModeTab.ItemDisplayParameters> para);

    CreativeModeTabPreRegistrable addStack(
        final Supplier<ItemStack> stack,
        final CreativeModeTab.TabVisibility tabVisibility
    );

    default CreativeModeTabPreRegistrable addStack(final Supplier<ItemStack> stack) {
        return this.addStack(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default CreativeModeTabPreRegistrable add(
        final ItemLike item,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        return this.addStack(() -> new ItemStack(item), tabVisibility);
    }

    default CreativeModeTabPreRegistrable add(final ItemLike item) {
        return this.addStack(() -> new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default CreativeModeTabPreRegistrable addItem(final Item item) {
        this.addStack(() -> new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        return this;
    }

    default CreativeModeTabPreRegistrable addItem(
        final Item item,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        this.addStack(() -> new ItemStack(item), tabVisibility);
        return this;
    }

    default CreativeModeTabPreRegistrable addItems(final Collection<Item> items) {
        items.forEach(this::addItem);
        return this;
    }

    default CreativeModeTabPreRegistrable addItems(
        final Collection<Item> items,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        items.forEach(item -> addItems(items, tabVisibility));
        return this;
    }

    default CreativeModeTabPreRegistrable addItems(final Item... items) {
        return this.addItems(List.of(items));
    }

    default CreativeModeTabPreRegistrable addStacks(
        final Collection<Supplier<ItemStack>> stacks,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        stacks.forEach(stack -> this.addStack(stack, tabVisibility));
        return this;
    }

    default CreativeModeTabPreRegistrable addStacks(final Collection<Supplier<ItemStack>> stacks) {
        this.addStacks(stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        return this;
    }

    CreativeModeTabPreRegistrable noScrollBar();

    CreativeModeTabPreRegistrable hideTitle();

    CreativeModeTabPreRegistrable alignedRight();

    CreativeModeTabPreRegistrable background(@NotNull String path);

    CreativeModeTabPreRegistrable backgroundWithDefaultNamespace(@NotNull String path);

}
