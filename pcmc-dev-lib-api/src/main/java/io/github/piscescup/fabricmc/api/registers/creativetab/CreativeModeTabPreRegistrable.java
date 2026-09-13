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
 * Configures a {@link CreativeModeTab} before registration.
 *
 * <p>This stage covers the tab icon, displayed {@link Item items} and
 * {@link ItemStack stacks}, menu position,
 * visibility, title layout, and background. Entries retain their addition order,
 * and suppliers allow stacks to be created when the tab requests them:</p>
 * <pre>{@code
 * public static final CreativeModeTab TEST_TAB1 = CREATIVE_TABS.path("test_itemgroup")
 *     .iconFromItem(TestItems.CUSTOM_ITEM)
 *     .addItems(TestItems.CUSTOM_ITEM, Items.IRON_INGOT, Items.GOLD_INGOT)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Test ItemGroup 1")
 *     .get();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface CreativeModeTabPreRegistrable
    extends PreRegistrable<CreativeModeTab, CreativeModeTab, CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable>
{
    /**
     * Sets the {@link CreativeModeTab.Row row} and column occupied by the tab in
     * the creative mode menu.
     *
     * @param row the menu row
     * @param column the column within the row
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable position(CreativeModeTab.Row row, int column);

    /**
     * Sets the {@link ItemStack} used as the tab icon.
     *
     * @param item the icon stack supplier
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable icon(@NotNull Supplier<ItemStack> item);

    /**
     * Sets the tab icon from an {@link ItemLike} value.
     *
     * @param item the icon item, or {@code null} to use an empty stack
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable iconFromItem(@Nullable ItemLike item) {
        Supplier<ItemStack> icon = item == null ?
            () -> ItemStack.EMPTY:
            () -> new ItemStack(item.asItem());
        return icon(icon);
    }

    /**
     * Adds an action that receives the current
     * {@link CreativeModeTab.ItemDisplayParameters} whenever the tab contents
     * are requested.
     *
     * <p>Multiple actions are retained and run in addition order.</p>
     *
     * @param para the display-parameter action
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable addBy(Consumer<CreativeModeTab.ItemDisplayParameters> para);

    /**
     * Adds an {@link ItemStack} with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * @param stack the stack supplier
     * @param tabVisibility where the stack is visible
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable addStack(
        final Supplier<ItemStack> stack,
        final CreativeModeTab.TabVisibility tabVisibility
    );

    /**
     * Adds an {@link ItemStack} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * @param stack the stack supplier
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addStack(final Supplier<ItemStack> stack) {
        return this.addStack(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    /**
     * Adds an {@link ItemLike} value with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * @param item the value to add
     * @param tabVisibility where the item is visible
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable add(
        final ItemLike item,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        return this.addStack(() -> new ItemStack(item), tabVisibility);
    }

    /**
     * Adds an {@link ItemLike} value with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * @param item the value to add
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable add(final ItemLike item) {
        return this.addStack(() -> new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    /**
     * Adds an {@link Item} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * @param item the item to add
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addItem(final Item item) {
        this.addStack(() -> new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        return this;
    }

    /**
     * Adds an {@link Item} with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * @param item the item to add
     * @param tabVisibility where the item is visible
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addItem(
        final Item item,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        this.addStack(() -> new ItemStack(item), tabVisibility);
        return this;
    }

    /**
     * Adds a {@link Collection} of {@link Item items} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * @param items the items to add
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addItems(final Collection<Item> items) {
        items.forEach(this::addItem);
        return this;
    }

    /**
     * Adds a {@link Collection} of {@link Item items} with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * @param items the items to add
     * @param tabVisibility where the items are visible
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addItems(
        final Collection<Item> items,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        items.forEach(item -> addItems(items, tabVisibility));
        return this;
    }

    /**
     * Adds {@link Item items} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * @param items the items to add
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addItems(final Item... items) {
        return this.addItems(List.of(items));
    }

    /**
     * Adds a {@link Collection} of {@link Supplier suppliers} for
     * {@link ItemStack item stacks} with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * @param stacks the stack suppliers to add
     * @param tabVisibility where the stacks are visible
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addStacks(
        final Collection<Supplier<ItemStack>> stacks,
        final CreativeModeTab.TabVisibility tabVisibility
    ) {
        stacks.forEach(stack -> this.addStack(stack, tabVisibility));
        return this;
    }

    /**
     * Adds a {@link Collection} of {@link Supplier suppliers} for
     * {@link ItemStack item stacks} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * @param stacks the stack suppliers to add
     * @return this pre-registration stage
     */
    default CreativeModeTabPreRegistrable addStacks(final Collection<Supplier<ItemStack>> stacks) {
        this.addStacks(stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        return this;
    }

    /**
     * Hides the scroll bar in the tab content area.
     *
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable noScrollBar();

    /**
     * Hides the tab title.
     *
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable hideTitle();

    /**
     * Aligns the tab title to the right.
     *
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable alignedRight();

    /**
     * Sets a background texture in the tab's namespace.
     *
     * @param path the background texture path
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable background(@NotNull String path);

    /**
     * Sets a background texture in the default Minecraft namespace.
     *
     * @param path the background texture path
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable backgroundWithDefaultNamespace(@NotNull String path);

}
