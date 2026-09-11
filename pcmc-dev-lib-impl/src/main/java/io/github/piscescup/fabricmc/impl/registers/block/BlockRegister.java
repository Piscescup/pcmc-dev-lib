package io.github.piscescup.fabricmc.impl.registers.block;

import io.github.piscescup.fabricmc.api.registers.block.BlockPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.block.BlockPreRegistrable;
import io.github.piscescup.fabricmc.constants.MCLanguage;
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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class BlockRegister<B extends Block>
    extends Register<Block, B, BlockPreRegistrable<B>, BlockPostRegistrable<B>>
    implements BlockPreRegistrable<B>, BlockPostRegistrable<B>
{
    private final BlockItemId blockItemId;

    private BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of();


    private final Function<BlockBehaviour.Properties, B> blockFactory;

    private Item blockItem;

    private BiFunction<? super B, Item.Properties, ? extends Item> blockItemFactory = BlockItem::new;

    private Item.Properties itemProperties = new Item.Properties();

    protected BlockRegister(BlockItemId blockItemId, Function<BlockBehaviour.Properties, B> blockFactory) {
        NullCheck.requireNonNull(blockItemId, "blockItemId");
        super(BuiltInRegistries.BLOCK, blockItemId.block().identifier());
        this.blockFactory = NullCheck.requireNonNull(blockFactory, "factory");
        this.blockItemId = blockItemId;
    }

    @Override
    public @NotNull BlockPreRegistrable<B> blockItemProperties(@NotNull UnaryOperator<Item.Properties> propertiesFunction) {
        NullCheck.requireNonNull(propertiesFunction, "propertiesFunction");
        itemProperties = propertiesFunction.apply(this.itemProperties);
        return this;
    }

    @Override
    public @NotNull BlockPreRegistrable<B> blockItemProperties(@NotNull Item.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");
        itemProperties = properties;
        return this;
    }

    @Override
    public @NotNull BlockPreRegistrable<B> blockItemFactory(
        BiFunction<? super B, Item.Properties, ? extends Item> blockItemFactory
    ) {
        NullCheck.requireNonNull(blockItemFactory, "blockItemFactory");

        this.blockItemFactory = blockItemFactory;
        return this;
    }

    @Override
    public @NotNull BlockPreRegistrable<B> blockProperties(@NotNull BlockBehaviour.Properties properties) {
        NullCheck.requireNonNull(properties, "properties");
        this.blockProperties = properties;
        return this;
    }

    /**
     * Registers the configured object and enters the
     * post-registration stage.
     *
     * @return the corresponding post-registration stage
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

    @Override
    public @NotNull Identifier blockItemId() {
        return blockItemId.item().identifier();
    }

    @Override
    public @NotNull Item blockItem() {
        return this.blockItem;
    }

    @Override
    public @NotNull BlockPostRegistrable<B> translate(@NotNull MCLanguage lang, @NotNull String translation) {
        return this;
    }
}
