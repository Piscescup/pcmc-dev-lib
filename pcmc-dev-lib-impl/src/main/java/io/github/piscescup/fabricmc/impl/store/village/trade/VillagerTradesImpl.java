package io.github.piscescup.fabricmc.impl.store.village.trade;

import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.fabricmc.store.villager.trade.VillagerTrades;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.*;

/**
 * Stores mutable lists of trade keys grouped by career level.
 *
 * <p>Levels follow enum order. Each level's list preserves insertion order and
 * accepts duplicate keys. {@link #trades()} exposes the backing map directly.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class VillagerTradesImpl
    implements VillagerTrades
{
    /** Mutable level mapping, populated on the first addition to each level. */
    private final Map<TradeLevel, Collection<ResourceKey<VillagerTrade>>> trades = new EnumMap<>(TradeLevel.class);

    /**
     * Appends a trade key to a career level, creating its list if necessary.
     *
     * @param level the career level receiving the trade; must not be {@code null}
     * @param tradeKey the trade registry key to append; must not be {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
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

    /** {@inheritDoc} */
    @Override
    public Map<TradeLevel, Collection<ResourceKey<VillagerTrade>>> trades() {
        return trades;
    }
}
