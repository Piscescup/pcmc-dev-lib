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
 * {@link ItemStack stacks}, menu position, visibility, alignment, and background.
 * The supplied implementation retains stack suppliers in insertion order and
 * evaluates them whenever the tab's contents are built.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * public static final CreativeModeTab TEST_TAB1 = CREATIVE_TABS.path("test_itemgroup")
 *     .iconFromItem(TestItems.CUSTOM_ITEM)
 *     .addItems(TestItems.CUSTOM_ITEM, Items.IRON_INGOT, Items.GOLD_INGOT)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Test ItemGroup 1")
 *     .get();
 * }</pre>
 *
 * <p>Calling {@link #register()} builds and registers the tab, then returns
 * the matching {@link CreativeModeTabPostRegistrable post-registration stage}
 * for localized titles and access to the result. Complete this stateful
 * builder's configuration before registering it once. When setting an explicit
 * {@link #position(CreativeModeTab.Row, int) position}, select it before other
 * builder options because the supplied implementation replaces its tab builder.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 * @see CreativeModeTabPostRegistrable
 */
public interface CreativeModeTabPreRegistrable
    extends PreRegistrable<CreativeModeTab, CreativeModeTab, CreativeModeTabPreRegistrable, CreativeModeTabPostRegistrable>
{
    /**
     * Sets the {@link CreativeModeTab.Row row} and column occupied by the tab in
     * the creative mode menu.
     *
     * <p>The supplied implementation creates a new tab builder for this
     * position. Call this method before configuring the icon, scroll bar,
     * title visibility, alignment, or background, since those options are reset.
     * Previously added entries and display-parameter actions are retained.</p>
     *
     * @param row    the menu row
     * @param column the column within the row
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable position(CreativeModeTab.Row row, int column);

    /**
     * Sets the supplier of the {@link ItemStack} used as the tab icon.
     *
     * <p>The supplier is passed to the tab builder for evaluation when the
     * icon is requested. It must return a non-null stack; use
     * {@link ItemStack#EMPTY} when no icon is desired.</p>
     *
     * @param item the supplier that creates the icon stack
     * @return this pre-registration stage
     * @see #iconFromItem(ItemLike)
     */
    CreativeModeTabPreRegistrable icon(@NotNull Supplier<ItemStack> item);

    /**
     * Sets the tab icon from an {@link ItemLike} value.
     *
     * <p>A non-null value is wrapped in a supplier that creates a new stack of
     * its item. Passing {@code null} installs a supplier of {@link ItemStack#EMPTY}.</p>
     *
     * @param item the icon item, or {@code null} to use an empty stack
     * @return this pre-registration stage
     * @see #icon(Supplier)
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
     * <p>Multiple actions are retained and run in addition order. The supplied
     * implementation runs them before evaluating the stored stack suppliers.
     * The callback receives display parameters only, so entries are supplied
     * through the {@code add} methods on this stage.</p>
     *
     * @param para the display-parameter action
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable addBy(Consumer<CreativeModeTab.ItemDisplayParameters> para);

    /**
     * Adds a supplier of an {@link ItemStack} with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * <p>The supplied implementation evaluates the supplier each time the tab's
     * contents are built, after the {@link #addBy(Consumer)} actions. Suppliers
     * retain their insertion order. Adding an equal supplier again replaces
     * its visibility without moving it to the end.</p>
     *
     * @param stack         the supplier that returns a non-null display stack
     * @param tabVisibility where the stack is visible
     * @return this pre-registration stage
     * @see #addStack(Supplier)
     */
    CreativeModeTabPreRegistrable addStack(
        final Supplier<ItemStack> stack,
        final CreativeModeTab.TabVisibility tabVisibility
    );

    /**
     * Adds an {@link ItemStack} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * <p>Delegates to {@link #addStack(Supplier, CreativeModeTab.TabVisibility)},
     * making the supplied stack visible in both the parent and search tabs.</p>
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
     * <p>Wraps the value in a supplier that creates a fresh stack whenever
     * the tab's contents are built. This accepts blocks as well as items.</p>
     *
     * @param item          the value whose item form is displayed
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
     * <p>A fresh stack of the value's item form is supplied each time the
     * tab's contents are built.</p>
     *
     * @param item the value to add
     * @return this pre-registration stage
     * @see #add(ItemLike, CreativeModeTab.TabVisibility)
     */
    default CreativeModeTabPreRegistrable add(final ItemLike item) {
        return this.addStack(() -> new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    /**
     * Adds an {@link Item} with
     * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} visibility.
     *
     * <p>A fresh stack is supplied each time the tab's contents are built.</p>
     *
     * @param item the item to add
     * @return this pre-registration stage
     * @see #addItem(Item, CreativeModeTab.TabVisibility)
     */
    default CreativeModeTabPreRegistrable addItem(final Item item) {
        this.addStack(() -> new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        return this;
    }

    /**
     * Adds an {@link Item} with explicit
     * {@link CreativeModeTab.TabVisibility parent-tab and search-tab visibility}.
     *
     * <p>Wraps the item in a supplier that creates a fresh stack whenever
     * the tab's contents are built.</p>
     *
     * @param item          the item to add
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
     * <p>The collection is traversed immediately in its iteration order, and
     * each item is passed to {@link #addItem(Item)}. Later structural changes
     * to the collection do not add or remove tab entries.</p>
     *
     * @param items the items to add in collection iteration order
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
     * @param items         the items to add
     * @param tabVisibility where the items are visible
     * @return this pre-registration stage
     * @see #addItem(Item, CreativeModeTab.TabVisibility)
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
     * <p>Delegates to {@link #addItems(Collection)}, preserving argument order.</p>
     *
     * @param items the items to add in argument order
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
     * <p>The collection is traversed immediately in its iteration order. The
     * suppliers are retained for later evaluation when tab contents are built;
     * later structural changes to the collection do not change the entries.</p>
     *
     * @param stacks        the stack suppliers to add in collection iteration order
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
     * <p>Delegates to
     * {@link #addStacks(Collection, CreativeModeTab.TabVisibility)}, preserving
     * collection iteration order and making stacks visible in both tabs.</p>
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
     * <p>Applies the tab builder's {@code noScrollBar} option.</p>
     *
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable noScrollBar();

    /**
     * Hides the tab title.
     *
     * <p>This controls whether the title is shown; it does not change the
     * translation key used for the tab's name.</p>
     *
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable hideTitle();

    /**
     * Enables the tab builder's right-alignment option.
     *
     * @return this pre-registration stage
     */
    CreativeModeTabPreRegistrable alignedRight();

    /**
     * Sets a background texture in the tab's namespace.
     *
     * <p>The supplied implementation combines the registration namespace with
     * this path and passes the resulting identifier to the tab builder.</p>
     *
     * @param path the background texture path without a namespace prefix
     * @return this pre-registration stage
     * @see #backgroundWithDefaultNamespace(String)
     */
    CreativeModeTabPreRegistrable background(@NotNull String path);

    /**
     * Sets a background texture in the default Minecraft namespace.
     *
     * <p>The supplied implementation passes an identifier in the
     * {@code minecraft} namespace to the tab builder.</p>
     *
     * @param path the background texture path without a namespace prefix
     * @return this pre-registration stage
     * @see #background(String)
     */
    CreativeModeTabPreRegistrable backgroundWithDefaultNamespace(@NotNull String path);

}
