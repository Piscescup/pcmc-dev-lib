package io.github.piscescup.fabricmc.impl.registers.block;

import io.github.piscescup.fabricmc.api.registers.block.BlockPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class BlockRegistryFactory extends RegisterFactoryImpl<Block> {

    BlockRegistryFactory(String namespace) {
        super(Registries.BLOCK, namespace);
    }

    @Contract("_ -> new")
    public static @NotNull BlockRegistryFactory ofNamespace(@NotNull String namespace) {
        NullCheck.requireNonNull(namespace, "namespace");
        return new BlockRegistryFactory(namespace);
    }

    @NotNull
    public BlockPreRegistrable<Block> path(@NotNull String path) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return path(id, id);
    }

    @NotNull
    public BlockPreRegistrable<Block> path(@NotNull Identifier blockId, @NotNull Identifier itemId) {
        NullCheck.requireNonNull(blockId, "blockId");
        NullCheck.requireNonNull(itemId, "itemId");
        return new BlockRegister<>(BlockItemId.create(blockId, itemId), Block::new);
    }

    @NotNull
    public <B extends Block> BlockPreRegistrable<B> path(
        @NotNull String path, @NotNull Function<BlockBehaviour.Properties, B> blockFactory
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return path(id, id, blockFactory);
    }

    @NotNull
    public <B extends Block> BlockPreRegistrable<B> path(
        @NotNull Identifier blockId, @NotNull Identifier itemId,
        Function<BlockBehaviour.Properties, B> blockFactory
    ) {
        NullCheck.requireNonNull(blockId, "blockId");
        NullCheck.requireNonNull(itemId, "itemId");
        NullCheck.requireNonNull(blockFactory, "blockFactory");
        return new BlockRegister<>(BlockItemId.create(blockId, itemId), blockFactory);
    }
}
