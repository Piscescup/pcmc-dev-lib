package io.github.piscescup.fabricmc.api.trade;


import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerProfessionTradesPostRegistrable {
    EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades();
}
