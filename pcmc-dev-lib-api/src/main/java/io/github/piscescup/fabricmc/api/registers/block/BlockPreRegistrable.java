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
 * selected independently. Registering this stage makes both values available
 * through the matching {@link BlockPostRegistrable post-registration stage}:</p>
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
 * @param <B> the concrete block type being registered
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockPreRegistrable<B extends Block>
    extends PreRegistrable<Block, B, BlockPreRegistrable<B>, BlockPostRegistrable<B>>
{
    /**
     * Sets the {@link BlockBehaviour.Properties} of the block to register.
     *
     * @param properties the block properties
     * @return this pre-registration stage
     */
    @NotNull
    BlockPreRegistrable<B> blockProperties(@NotNull BlockBehaviour.Properties properties);

    /**
     * Configures the block item's {@link Item.Properties} with an updating function.
     *
     * <p>This overload is useful for changing selected properties without
     * replacing the current property set.</p>
     *
     * @param propertiesFunction the block-item property updater
     * @return this pre-registration stage
     */
    @NotNull
    BlockPreRegistrable<B> blockItemProperties(@NotNull UnaryOperator<Item.Properties> propertiesFunction);

    /**
     * Sets the {@link Item.Properties} of the matching block item.
     *
     * @param properties the block-item properties
     * @return this pre-registration stage
     */
    @NotNull
    BlockPreRegistrable<B> blockItemProperties(@NotNull Item.Properties properties);

    /**
     * Selects the {@link Item} type associated with the block.
     *
     * <p>Omit this option when a standard block item is sufficient.</p>
     *
     * @param factory a function that creates an {@link Item} from the block and its properties
     * @return this pre-registration stage
     */
    @NotNull
    BlockPreRegistrable<B> blockItemFactory(
        BiFunction<? super B, Item.Properties, ? extends Item> factory
    );
}
