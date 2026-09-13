package io.github.piscescup.fabricmc.testtags;

import io.github.piscescup.fabricmc.impl.registers.tag.TagKeyRegisterFactory;
import io.github.piscescup.fabricmc.testblocks.TestBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;
import static io.github.piscescup.fabricmc.Refs.MOD_LOGGER;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class TestBlockTags {
    private static final TagKeyRegisterFactory<Block> BLOCK_TAG_KEYS =
        TagKeyRegisterFactory.blocksTagOfNamespace(MOD_ID);


    public static final TagKey<Block> VANILLA_NEEDS_IRON_TOOL = TagKeyRegisterFactory.ofVanilla(BlockTags.NEEDS_IRON_TOOL)
        .addEntries(TestBlocks.CUSTOM_BLOCK, TestBlocks.TEST_BLOCK1)
        .register()
        .get();

    public static final TagKey<Block> VANILLA_MINEABLE_WITH_PICKAXE = TagKeyRegisterFactory.ofVanilla(BlockTags.MINEABLE_WITH_PICKAXE)
        .addEntries(TestBlocks.CUSTOM_BLOCK,TestBlocks.TEST_BLOCK1)
        .register()
        .get();

    public static void registerBlockTags() {
        MOD_LOGGER.info("Registering block Tags");
    }
}
