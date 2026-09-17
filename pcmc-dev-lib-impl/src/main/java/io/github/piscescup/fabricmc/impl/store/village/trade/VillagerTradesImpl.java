package io.github.piscescup.fabricmc.impl.store.village.trade;

import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.fabricmc.store.villager.trade.VillagerTrades;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Optional;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class VillagerTradesImpl
    implements VillagerTrades
{
    private final EnumMap<TradeLevel, Collection<VillagerTrade>> trades;

    public VillagerTradesImpl() {
        this.trades = new EnumMap<>(TradeLevel.class);
    }

    public boolean add(TradeLevel level, VillagerTrade trade) {
        return this.trades.computeIfAbsent(level, _ -> new ArrayList<>())
            .add(trade);
    }

    @Override
    public Optional<Collection<VillagerTrade>> tradeSetByLevel(TradeLevel level) {
        return Optional.empty();
    }
}
