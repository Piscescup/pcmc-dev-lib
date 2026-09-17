package io.github.piscescup.fabricmc.store.villager.trade;

import net.minecraft.world.item.trading.VillagerTrade;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;

import static io.github.piscescup.fabricmc.store.villager.trade.TradeLevel.TRADE_LEVELS;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerTrades {

    Optional<Collection<VillagerTrade>> tradeSetByLevel(TradeLevel level);

    default void forEach(
        BiConsumer<TradeLevel, Collection<VillagerTrade>> action
    ) {
        for (TradeLevel level : TRADE_LEVELS) {
            tradeSetByLevel(level).ifPresent(trades -> action.accept(level, trades));
        }
    }
}
