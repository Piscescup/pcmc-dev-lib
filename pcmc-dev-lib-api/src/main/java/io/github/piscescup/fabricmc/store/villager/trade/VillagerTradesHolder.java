package io.github.piscescup.fabricmc.store.villager.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.Collection;
import java.util.Optional;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerTradesHolder {
    Optional<VillagerTrades> searchBy(
        ResourceKey<VillagerTrade> villagerTrade
    );

    Collection<VillagerTrades> all();

    boolean contains(ResourceKey<VillagerTrade> villagerTrade);
}
