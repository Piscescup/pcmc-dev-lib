package io.github.piscescup.fabricmc.impl.store.village.trade;

import io.github.piscescup.fabricmc.store.villager.trade.VillagerTrades;
import io.github.piscescup.fabricmc.store.villager.trade.VillagerTradesHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum MutableVillagerTradeHolder
    implements VillagerTradesHolder
{
    INSTANCE;

    private final Map<ResourceKey<VillagerTrade>, VillagerTrades> trades =
        new LinkedHashMap<>();

    public void add(
        ResourceKey<VillagerTrade> key,
        VillagerTrades trade
    ) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(trade, "trade");

        VillagerTrades previous = trades.putIfAbsent(key, trade);

        if (previous != null) {
            throw new IllegalStateException(
                "Duplicate villager trade declaration: " + key.identifier()
            );
        }
    }

    @NotNull
    @Override
    public Optional<VillagerTrades> searchBy(
        ResourceKey<VillagerTrade> villagerTrade
    ) {
        Objects.requireNonNull(villagerTrade, "villagerTrade");
        return Optional.ofNullable(
            trades.get(villagerTrade)
        );
    }

    @NotNull
    @Override
    @UnmodifiableView
    public Collection<VillagerTrades> all() {
        return Collections.unmodifiableCollection(trades.values());
    }

    @Override
    public boolean contains(
        ResourceKey<VillagerTrade> villagerTrade
    ) {
        return trades.containsKey(villagerTrade);
    }
}
