package io.github.piscescup.fabricmc.impl.store.village.trade;

import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.fabricmc.store.villager.trade.VillagerTrades;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class VillagerTradesImpl
    implements VillagerTrades
{
    private final Map<TradeLevel, Collection<ResourceKey<VillagerTrade>>> trades = new EnumMap<>(TradeLevel.class);

    public void add(
        TradeLevel level,
        ResourceKey<VillagerTrade> tradeKey
    ) {
        Objects.requireNonNull(level, "key");
        Objects.requireNonNull(tradeKey, "tradeKey");

        trades
            .computeIfAbsent(level, _ -> new ArrayList<>())
            .add(tradeKey);

    }

    @Override
    public Map<TradeLevel, Collection<ResourceKey<VillagerTrade>>> trades() {
        return trades;
    }
}
