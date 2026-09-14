package io.github.piscescup.fabricmc.impl.registers.block;

import io.github.piscescup.fabricmc.api.registers.block.BlockPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.block.BlockPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Implements registration of a block and its associated item on one builder.
 *
 * <p>Registration adds the {@link Block} first and then constructs and registers
 * its associated item. After successful registration, this same instance exposes
 * both results through {@link BlockPostRegistrable}.</p>
 *
 * <p>Instances are created by {@link BlockRegistryFactory} and are not intended
 * to be reused: {@link #register()} performs an unconditional
 * {@link Registry#register(Registry, Identifier, Object) register} on each
 * invocation, so calling it more than once on the same instance can attempt a
 * duplicate registration and will overwrite {@link #blockItem}.</p>
 *
 * @param <B> the concrete {@link Block} type constructed and registered by this builder
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see BlockRegistryFactory
 * @see BlockPreRegistrable
 * @see BlockPostRegistrable
 */
public class BlockRegister<B extends Block>
    extends Register<Block, B, BlockPreRegistrable<B>, BlockPostRegistrable<B>>
    implements BlockPreRegistrable<B>, BlockPostRegistrable<B>
{
    /**
     * The combined block/item identifier pair supplied at construction time.
     * The {@link Block} identifier is used by the parent {@link Register} for
     * the block registration; the {@link Item} identifier is used in
     * {@link #register()}.
     */
    private final BlockItemId blockItemId;

    /**
     * Mutable {@link Block} properties retained until {@link #register()} is
     * invoked. Defaults to {@link BlockBehaviour.Properties#of()} and is
     * replaced by {@link #blockProperties(BlockBehaviour.Properties)}.
     */
    private BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of();

    /**
     * Factory invoked during {@link #register()} to construct the {@link Block}
     * from its properties. Never {@code null} after construction.
     */
    private final Function<BlockBehaviour.Properties, B> blockFactory;

    /**
     * The {@link Item} registered together with the {@link Block}, assigned
     * during {@link #register()} and exposed via {@link #blockItem()}.
     */
    private Item blockItem;

    /**
     * Factory used to construct the associated {@link Item}. Defaults to
     * {@link BlockItem#BlockItem(Block, Item.Properties)} and may be replaced
     * with {@link #blockItemFactory(BiFunction)}.
     */
    private BiFunction<? super B, Item.Properties, ? extends Item> blockItemFactory = BlockItem::new;

    /**
     * Mutable {@link Item} properties retained until {@link #register()} is
     * invoked. Defaults to a fresh {@link Item.Properties} instance.
     */
    private Item.Properties itemProperties = new Item.Properties();

    /**
     * Creates a builder for a {@link Block} and {@link Item} identifier pair.
     *
     * @param blockItemId  the resource keys selected for the {@link Block} and {@link Item}; must not be {@code null}
     * @param blockFactory the function invoked to construct the {@link Block} during registration; must not be {@code null}
     * @throws NullPointerException if {@code blockItemId} or {@code blockFactory} is {@code null}
     */
    BlockRegister(BlockItemId blockItemId, Function<BlockBehaviour.Properties, B> blockFactory) {
        NullCheck.requireNonNull(blockItemId, "blockItemId");
        super(BuiltInRegistries.BLOCK, blockItemId.block().identifier());
        this.blockFactory = NullCheck.requireNonNull(blockFactory, "factory");
        this.blockItemId = blockItemId;
    }

    /**
     * Immediately applies an updater to the currently retained {@link Item} properties.
     *
     * <p>The updater is applied eagerly; if it returns {@code null}, the
     * retained properties become {@code null} and the next registration will
     * fail. Callers should ensure the returned properties are non-null.</p>
     *
     * @param propertiesFunction the updater returning the non-null {@link Item.Properties} to retain; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code propertiesFunction} is {@code null}
     * @see #blockItemProperties(Item.Properties)
     */
    @Override
    public @NotNull BlockPreRegistrable<B> blockItemProperties(@NotNull UnaryOperator<Item.Properties> propertiesFunction) {
        NullCheck.requireNonNull(propertiesFunction, "propertiesFunction");
        itemProperties = propertiesFunction.apply(this.itemProperties);
        return this;
    }

    /**
     * Replaces the properties retained for the associated {@link Item}.
     *
     * <p>Registration assigns the {@link Item}'s resource key and enables the
     * block description prefix on this same properties instance. The supplied
     * instance is stored as-is and mutated during {@link #register()}.</p>
     *
     * @param properties the {@link Item.Properties} to retain directly; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code properties} is {@code null}
     */
    @Override
    public @NotNull BlockPreRegistrable<B> blockItemProperties(@NotNull Item.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");
        itemProperties = properties;
        return this;
    }

    /**
     * Replaces the factory used to construct the {@link Block}'s associated {@link Item}.
     *
     * <p>The factory is retained until {@link #register()} and invoked with the
     * registered {@link Block} instance and the retained {@link Item.Properties}.</p>
     *
     * @param blockItemFactory the function receiving the registered {@link Block} and {@link Item}
     *                         properties and returning a non-null {@link Item}; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code blockItemFactory} is {@code null}
     */
    @Override
    public @NotNull BlockPreRegistrable<B> blockItemFactory(
        BiFunction<? super B, Item.Properties, ? extends Item> blockItemFactory
    ) {
        NullCheck.requireNonNull(blockItemFactory, "blockItemFactory");

        this.blockItemFactory = blockItemFactory;
        return this;
    }

    /**
     * Replaces the properties retained for {@link Block} construction.
     *
     * <p>The supplied instance is stored as-is; during {@link #register()} it
     * receives the {@link Block}'s resource key via
     * {@link BlockBehaviour.Properties#setId(net.minecraft.resources.ResourceKey)}
     * and is then passed to the block factory.</p>
     *
     * @param properties the {@link BlockBehaviour.Properties} that receive the {@link Block} resource key during registration; must not be {@code null}
     * @return this instance as its pre-registration stage
     * @throws NullPointerException if {@code properties} is {@code null}
     */
    @Override
    public @NotNull BlockPreRegistrable<B> blockProperties(@NotNull BlockBehaviour.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");
        this.blockProperties = properties;
        return this;
    }

    /**
     * Constructs and registers the {@link Block}, followed by its associated {@link Item}.
     *
     * <p>Both property instances receive their respective resource keys. If
     * the {@link Item} factory produces a {@link BlockItem}, its block associations are
     * added to {@link Item#BY_BLOCK} before {@link Item} registration. Repeated calls
     * are not guarded and can attempt duplicate registrations.</p>
     *
     * <p>The {@link Block} is registered into {@link BuiltInRegistries#BLOCK}, and the
     * {@link Item} into {@link BuiltInRegistries#ITEM}, using the identifiers supplied
     * to the constructor. After a successful call, {@link #blockItem()} returns
     * the registered {@link Item} and the inherited registry accessors expose the {@link Block}.</p>
     *
     * @return this instance as the block post-registration stage
     * @throws NullPointerException if the block factory, block properties, {@link Item}
     *         properties, or {@link Item} factory returns or holds a {@code null} value
     */
    @Override
    public @NotNull BlockPostRegistrable<B> register() {
        B block = this.blockFactory.apply(this.blockProperties
            .setId(this.resourceKey));

        this.thingToBeRegistered = Registry.register(
            this.registry,
            this.id,
            block
        );

        // Create the block item instance
        Item item = this.blockItemFactory.apply(
            block,
            itemProperties.useBlockDescriptionPrefix().setId(blockItemId.item())
        );

        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        this.blockItem = Registry.register(BuiltInRegistries.ITEM, blockItemId.item(), item);

        return this;
    }

    /**
     * Returns the registered {@link Block}'s description ID for language data generation.
     *
     * <p>This method is only meaningful after a successful {@link #register()};
     * before that, {@code thingToBeRegistered} is {@code null} and this call
     * will fail with a {@link NullPointerException}.</p>
     *
     * @return the description ID of the successfully registered {@link Block}
     */
    @Override
    protected String translateKey() {
        return thingToBeRegistered.getDescriptionId();
    }

    /**
     * Returns the identifier selected for the associated {@link Item}.
     *
     * <p>This is the {@link Item} identifier supplied at construction time; it may
     * differ from the {@link Block} identifier in both path and namespace.</p>
     *
     * @return the {@link Item} identifier, which may differ from the {@link Block} identifier
     */
    @Override
    public @NotNull Identifier blockItemId() {
        return blockItemId.item().identifier();
    }

    /**
     * Returns the {@link Item} stored after successful block-and-item registration.
     *
     * <p>This method returns the retained field directly. Before the item
     * registration completes, that field is {@code null}; no stage check is performed.</p>
     *
     * @return the registered {@link Item} produced by the selected item factory,
     *         or {@code null} until the item registration result has been assigned
     */
    @Override
    public @NotNull Item blockItem() {
        return this.blockItem;
    }

}
