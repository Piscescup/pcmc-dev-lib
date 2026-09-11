package io.github.piscescup.fabricmc.api.registers.blocks;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockPreRegistrable<B extends Block>
    extends PreRegistrable<Block, B, BlockPreRegistrable<B>, BlockPostRegistrable<B>>
{
    @NotNull
    BlockPreRegistrable<B> blockProperties(@NotNull BlockBehaviour.Properties properties);

    @NotNull
    BlockPreRegistrable<B> blockItemProperties(@NotNull UnaryOperator<Item.Properties> propertiesFunction);

    @NotNull
    BlockPreRegistrable<B> blockItemProperties(@NotNull Item.Properties properties);

    @NotNull
    BlockPreRegistrable<B> blockItemFactory(
        BiFunction<? super B, Item.Properties, ? extends Item> factory
    );
}
