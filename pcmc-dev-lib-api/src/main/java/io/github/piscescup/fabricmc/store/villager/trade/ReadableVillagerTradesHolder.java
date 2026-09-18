package io.github.piscescup.fabricmc.store.villager.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.Map;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ReadableVillagerTradesHolder {

    Map<ResourceKey<VillagerTrade>, VillagerTrades> all();
}
