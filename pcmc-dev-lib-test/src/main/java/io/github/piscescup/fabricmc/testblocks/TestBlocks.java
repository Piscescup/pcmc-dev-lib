package io.github.piscescup.fabricmc.testblocks;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import io.github.piscescup.fabricmc.impl.registers.block.BlockRegistryFactory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;
import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestBlocks {
    public static final BlockRegistryFactory BLOCKS = BlockRegistryFactory.ofNamespace(MOD_ID);

    public static final Block TEST_BLOCK1 = BLOCKS.path("test_block1")
        .blockProperties(BlockBehaviour.Properties.of()
            .sound(SoundType.ANVIL)
            .strength(1.5F, 1.0F)
            .requiresCorrectToolForDrops()
        )
        .blockItemProperties(new Item.Properties()
            .fireResistant()
        )
        .blockItemFactory(BlockItem::new)
        .register()
        .translate(MCLanguage.EN_US, "Test Block 1")
        .get();

    public static final CustomBlock CUSTOM_BLOCK = BLOCKS.path("custom_block", CustomBlock::new)
        .blockProperties(BlockBehaviour.Properties.of()
            .sound(SoundType.ANVIL)
            .strength(1.5F, 1.0F)
            .replaceable()
            .requiresCorrectToolForDrops()
        )
        .blockItemProperties(new Item.Properties()
            .fireResistant()
        )
        .blockItemFactory(BlockItem::new)
        .register()
        .translate(MCLanguage.EN_US, "Custom Block")
        .get();


    public static void registerModBlocks() {
        MOD_LOGGER.info("Registering Mod Blocks");
    }
}
