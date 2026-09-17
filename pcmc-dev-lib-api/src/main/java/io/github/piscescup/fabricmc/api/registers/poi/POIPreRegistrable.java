package io.github.piscescup.fabricmc.api.registers.poi;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface POIPreRegistrable
    extends PreRegistrable<PoiType, PoiType, POIPreRegistrable, POIPostRegistrable>
{
    interface MatchingStates {
        MaxTickets matchingStates(@NotNull Set<BlockState> matchingStates);

        default MaxTickets matchingStatesFrom(@NotNull Block block) {
            return matchingStates(
                ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates())
            );
        }
    }

    interface MaxTickets {
        ValidRange maxTickets(int maxTickets);
    }

    interface ValidRange {
        POIPreRegistrable validRange(int validRange);
    }
}
