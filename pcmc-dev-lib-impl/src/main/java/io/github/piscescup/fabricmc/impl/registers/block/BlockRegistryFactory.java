package io.github.piscescup.fabricmc.impl.registers.block;

import io.github.piscescup.fabricmc.api.registers.block.BlockPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.block.BlockPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Creates builders that register a {@link Block} together with its associated {@link Item}.
 *
 * <p>String-path overloads apply this factory's namespace and assign the same
 * identifier path in both the {@link Block} and {@link Item} registries.
 * Overloads accepting two {@link Identifier} values allow the {@link Block} and
 * {@link Item} identifiers to be selected independently, including their
 * namespaces. A custom {@link Block} constructor may also be supplied via a
 * {@link Function Function&lt;BlockBehaviour.Properties, B&gt;} so that the
 * concrete {@link Block} type is preserved through both registration stages.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * BlockRegistryFactory BLOCKS = BlockRegistryFactory.ofNamespace("example");
 * Block block = BLOCKS.path("example_block")
 *     .blockProperties(BlockBehaviour.Properties.of().strength(1.5F))
 *     .register()
 *     .translate(MCLanguage.EN_US, "Example Block")
 *     .get();
 * }</pre>
 *
 * <p>Typical usage with a custom block type:</p>
 * <pre>{@code
 * BlockRegistryFactory BLOCKS = BlockRegistryFactory.ofNamespace("example");
 * MyBlock block = BLOCKS.path("my_block", MyBlock::new)
 *     .blockProperties(BlockBehaviour.Properties.of().strength(2.0F))
 *     .register()
 *     .get();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see BlockRegister
 * @see BlockPreRegistrable
 * @see BlockPostRegistrable
 */
public final class BlockRegistryFactory extends RegisterFactoryImpl<Block> {

    /**
     * Creates a factory targeting the {@link Block} registry.
     *
     * @param namespace the namespace used by string-path overloads
     */
    BlockRegistryFactory(String namespace) {
        super(Registries.BLOCK, namespace);
    }

    /**
     * Creates a new {@link Block} factory for the given namespace.
     *
     * @param namespace the namespace, usually a mod ID; must not be {@code null}
     * @return a new factory for {@link Block} and block-item registrations
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    public static @NotNull BlockRegistryFactory ofNamespace(@NotNull String namespace) {
        NullCheck.requireNonNull(namespace, "namespace");
        return new BlockRegistryFactory(namespace);
    }

    /**
     * Starts a standard {@link Block} registration using one path for the block and item.
     *
     * <p>The path is combined with this factory's namespace to form a single
     * {@link Identifier} that is used for both the {@link Block} and its associated {@link Item}.</p>
     *
     * @param path the shared path without a namespace prefix; must not be {@code null}
     * @return a new block pre-registration stage using {@code Block::new}
     * @throws NullPointerException if {@code path} is {@code null}
     */
    @NotNull
    public BlockPreRegistrable<Block> path(@NotNull String path) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return path(id, id);
    }

    /**
     * Starts a standard {@link Block} registration with independently selected identifiers.
     *
     * <p>The supplied identifiers are used as-is; this factory's namespace is
     * not applied to either argument. The {@link Block} is constructed with the default
     * {@code Block::new} factory.</p>
     *
     * @param blockId the full {@link Block} identifier, used without applying this factory's namespace; must not be {@code null}
     * @param itemId  the full {@link Item} identifier, used without applying this factory's namespace; must not be {@code null}
     * @return a new block pre-registration stage using {@code Block::new}
     * @throws NullPointerException if {@code blockId} or {@code itemId} is {@code null}
     */
    @NotNull
    public BlockPreRegistrable<Block> path(@NotNull Identifier blockId, @NotNull Identifier itemId) {
        NullCheck.requireNonNull(blockId, "blockId");
        NullCheck.requireNonNull(itemId, "itemId");
        return new BlockRegister<>(BlockItemId.create(blockId, itemId), Block::new);
    }

    /**
     * Starts a custom {@link Block} registration using one path for the block and item.
     *
     * <p>The path is combined with this factory's namespace to form a single
     * {@link Identifier} that is used for both the {@link Block} and its associated {@link Item}.</p>
     *
     * @param path         the shared path without a namespace prefix; must not be {@code null}
     * @param blockFactory the function invoked to construct the {@link Block} during registration; must not be {@code null}
     * @param <B>          the concrete {@link Block} type preserved by both registration stages
     * @return a new pre-registration stage for the concrete {@link Block} type
     * @throws NullPointerException if {@code path} or {@code blockFactory} is {@code null}
     */
    @NotNull
    public <B extends Block> BlockPreRegistrable<B> path(
        @NotNull String path, @NotNull Function<BlockBehaviour.Properties, B> blockFactory
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, path);
        return path(id, id, blockFactory);
    }

    /**
     * Starts a custom {@link Block} registration with independently selected identifiers.
     *
     * <p>The block factory is retained until registration. The returned stage
     * can separately configure the associated {@link Item}'s properties and factory.
     * The supplied identifiers are used as-is; this factory's namespace is not
     * applied to either argument.</p>
     *
     * @param blockId      the full {@link Block} identifier; must not be {@code null}
     * @param itemId       the full associated-{@link Item} identifier; must not be {@code null}
     * @param blockFactory the function that constructs a non-null {@link Block} from its properties; must not be {@code null}
     * @param <B>          the concrete {@link Block} type preserved by both registration stages
     * @return a new pre-registration stage for the concrete {@link Block} type
     * @throws NullPointerException if {@code blockId}, {@code itemId}, or {@code blockFactory} is {@code null}
     */
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