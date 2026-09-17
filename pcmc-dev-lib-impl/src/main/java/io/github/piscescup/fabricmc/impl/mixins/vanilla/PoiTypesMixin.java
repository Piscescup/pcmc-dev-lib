package io.github.piscescup.fabricmc.impl.mixins.vanilla;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@Mixin(PoiTypes.class)
public interface PoiTypesMixin {

    @Invoker("registerBlockStates")
    static void pcmcDevLib$registerBlockStates(
        Holder<PoiType> type,
        Set<BlockState> matchingStates
    ) {
        throw new AssertionError("Untransformed @Invoker");
    }
}
