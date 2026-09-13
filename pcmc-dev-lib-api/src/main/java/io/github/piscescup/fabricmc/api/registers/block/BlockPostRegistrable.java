package io.github.piscescup.fabricmc.api.registers.block;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Provides post-registration operations for a {@link Block} and its block {@link Item}.
 *
 * <p>The standard post-registration methods refer to the block itself. This
 * stage additionally exposes the block item's identifier and registered value.</p>
 *
 * @param <B> the concrete registered block type
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockPostRegistrable<B extends Block>
    extends PostRegistrable<Block, B, BlockPostRegistrable<B>>
{
    /**
     * Returns the namespaced {@link Identifier} of the matching block item.
     *
     * @return the block-item identifier
     */
    @NotNull
    Identifier blockItemId();

    /**
     * Returns the namespaced {@link Identifier} of the block.
     *
     * @return the block identifier
     */
    @NotNull
    default Identifier blockId() {
        return identifier();
    }

    /**
     * Returns the {@link Item} registered for this block.
     *
     * @return the registered block item
     */
    @NotNull
    Item blockItem();
}
