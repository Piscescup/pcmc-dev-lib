package io.github.piscescup.fabricmc.api.registers.blocks;

import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockPostRegistrable<B extends Block>
    extends PostRegistrable<Block, B, BlockPostRegistrable<B>>
{
    @NotNull
    Identifier blockItemId();

    @NotNull
    default Identifier blockId() {
        return identifier();
    }

    @NotNull
    Item blockItem();
}
