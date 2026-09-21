package io.github.piscescup.fabricmc.store.villager.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.Collection;
import java.util.Map;

/**
 * Groups villager trade registry keys by career level.
 *
 * <p>This view contains trade references. Use {@link ReadableVillagerTradesHolder}
 * for profession-scoped definitions and trade-set selection metadata.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerTrades {

    /**
     * Returns the trade references grouped by career level.
     *
     * <p>The supplied implementation exposes its mutable backing map and
     * collections. Collections retain insertion order and may contain duplicates.</p>
     *
     * @return the live level-to-trade mapping
     */
    Map<TradeLevel, Collection<ResourceKey<VillagerTrade>>> trades();

}
