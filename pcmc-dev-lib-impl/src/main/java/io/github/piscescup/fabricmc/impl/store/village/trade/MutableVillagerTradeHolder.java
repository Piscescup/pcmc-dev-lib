package io.github.piscescup.fabricmc.impl.store.village.trade;

import io.github.piscescup.fabricmc.store.villager.trade.ReadableVillagerTradesHolder;
import io.github.piscescup.fabricmc.store.villager.trade.VillagerTrades;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum MutableVillagerTradeHolder
    implements ReadableVillagerTradesHolder
{
    INSTANCE;

    private final Map<ResourceKey<VillagerTrade>, VillagerTrades> levels =
        new LinkedHashMap<>();

    public void add(ResourceKey<VillagerTrade> key, VillagerTrades trades) {
        VillagerTrades pre = levels.put(key, trades);

        if (pre != null) {
            throw new IllegalStateException(
                "Duplicate trades: " + pre
            );
        }
    }


    @Override
    public Map<ResourceKey<VillagerTrade>, VillagerTrades> all() {
        return levels;
    }
}
