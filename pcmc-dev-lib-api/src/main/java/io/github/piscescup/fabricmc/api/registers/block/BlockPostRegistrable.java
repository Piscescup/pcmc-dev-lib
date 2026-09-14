package io.github.piscescup.fabricmc.api.registers.block;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Provides post-registration operations for a {@link Block} and its block {@link Item}.
 *
 * <p>This stage is returned by {@link BlockPreRegistrable#register()} after both
 * the block and its item have been registered. The inherited accessors and
 * collection operation refer to the block. The additional accessors expose
 * the corresponding item's identifier and registered instance.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * BlockPostRegistrable<Block> registered = BLOCKS.path("test_block1")
 *     .register()
 *     .translate(MCLanguage.EN_US, "Test Block 1");
 * Block block = registered.get();
 * Item blockItem = registered.blockItem();
 * Identifier blockItemId = registered.blockItemId();
 * }</pre>
 *
 * <p>The supplied implementation records translations under the block's
 * description ID. The default block item uses the block description prefix,
 * allowing it to share that translated name.</p>
 *
 * @param <B> the concrete registered block type; must be a subtype of {@link Block}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see BlockPreRegistrable
 * @see PostRegistrable
 */
public interface BlockPostRegistrable<B extends Block>
    extends PostRegistrable<Block, B, BlockPostRegistrable<B>>
{
    /**
     * Returns the namespaced {@link Identifier} of the matching block item.
     *
     * <p>The item belongs to the item registry and may have an identifier
     * different from {@link #blockId()}, as selected by the registration factory.</p>
     *
     * @return the block-item identifier
     * @see #blockItem()
     */
    @NotNull
    Identifier blockItemId();

    /**
     * Returns the namespaced {@link Identifier} of the block.
     *
     * <p>This is a convenience alias for the inherited {@link #identifier()}.</p>
     *
     * @return the block identifier
     * @see #identifier()
     */
    @NotNull
    default Identifier blockId() {
        return identifier();
    }

    /**
     * Returns the {@link Item} registered for this block.
     *
     * <p>This is the existing result of the selected block-item factory. A
     * custom factory may produce an item other than the standard
     * {@link net.minecraft.world.item.BlockItem BlockItem}, so callers should
     * not assume a more specific return type.</p>
     *
     * @return the registered block item
     * @see #blockItemId()
     */
    @NotNull
    Item blockItem();
}
