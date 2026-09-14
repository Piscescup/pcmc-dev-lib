package io.github.piscescup.fabricmc.api.registers.block;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * Configures a {@link Block} and its matching {@link Item} before registration.
 *
 * <p>The {@link BlockBehaviour.Properties block properties},
 * {@link Item.Properties block-item properties}, and block-item type can be
 * selected independently. The factory that creates this stage determines the
 * block type and the identifiers used for the block and its item.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * public static final Block TEST_BLOCK1 = BLOCKS.path("test_block1")
 *     .blockProperties(BlockBehaviour.Properties.of().strength(1.5F, 1.0F))
 *     .blockItemProperties(new Item.Properties().fireResistant())
 *     .blockItemFactory(BlockItem::new)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Test Block 1")
 *     .get();
 * }</pre>
 *
 * <p>The supplied implementation defaults to
 * {@link BlockBehaviour.Properties#of()}, a fresh {@link Item.Properties}
 * instance, and a standard {@link net.minecraft.world.item.BlockItem BlockItem}
 * factory. Calling {@link #register()} constructs and registers the block,
 * followed by its item, and returns the matching
 * {@link BlockPostRegistrable post-registration stage} for both results.
 * Complete all configuration before registering this stateful builder once.</p>
 *
 * @param <B> the concrete block type being registered; must be a subtype of
 *            {@link Block} and is preserved by the post-registration stage
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PreRegistrable
 * @see BlockPostRegistrable
 */
public interface BlockPreRegistrable<B extends Block>
    extends PreRegistrable<Block, B, BlockPreRegistrable<B>, BlockPostRegistrable<B>>
{
    /**
     * Sets the {@link BlockBehaviour.Properties} of the block to register.
     *
     * <p>The supplied implementation retains this instance and replaces the
     * previous properties. During registration it assigns the block resource
     * key before passing the properties to the block factory. Use a separate
     * properties instance for each registration.</p>
     *
     * @param properties the properties to pass to the block factory
     * @return this pre-registration stage
     */
    @NotNull
    BlockPreRegistrable<B> blockProperties(@NotNull BlockBehaviour.Properties properties);

    /**
     * Configures the block item's {@link Item.Properties} with an updating function.
     *
     * <p>The supplied implementation invokes the function immediately with the
     * currently selected properties and retains its result. The function may
     * update and return that instance or return a replacement, but its result
     * must not be {@code null}. Repeated calls operate on the previous result.</p>
     *
     * @param propertiesFunction the updater that returns the properties to retain
     * @return this pre-registration stage
     * @see #blockItemProperties(Item.Properties)
     */
    @NotNull
    BlockPreRegistrable<B> blockItemProperties(@NotNull UnaryOperator<Item.Properties> propertiesFunction);

    /**
     * Sets the {@link Item.Properties} of the matching block item.
     *
     * <p>The supplied implementation retains this instance, replacing the
     * previous properties. During registration it selects the block description
     * prefix and assigns the item resource key before invoking the item factory.</p>
     *
     * @param properties the properties to pass to the block-item factory
     * @return this pre-registration stage
     * @see #blockItemProperties(UnaryOperator)
     */
    @NotNull
    BlockPreRegistrable<B> blockItemProperties(@NotNull Item.Properties properties);

    /**
     * Selects the factory for the {@link Item} associated with the block.
     *
     * <p>The supplied implementation invokes this factory after registering the
     * block, passing that block and the configured item properties. The factory
     * must return a non-null item to register. Omit this option to use a standard
     * {@link net.minecraft.world.item.BlockItem BlockItem}.</p>
     *
     * @param factory a function that creates an {@link Item} from the block and its properties
     * @return this pre-registration stage
     */
    @NotNull
    BlockPreRegistrable<B> blockItemFactory(
        BiFunction<? super B, Item.Properties, ? extends Item> factory
    );
}
