package io.github.piscescup.fabricmc.api.trade;

import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;

import java.util.function.Consumer;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerProfessionTradesPreRegistrable {
    VillagerProfessionTradesPreRegistrable level(
        TradeLevel level,
        Consumer<VillagerLevelTradeBuilder> action
    );

    default VillagerProfessionTradesPreRegistrable novice(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.NOVICE, action);
    }

    default VillagerProfessionTradesPreRegistrable apprentice(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.APPRENTICE, action);
    }

    default VillagerProfessionTradesPreRegistrable journeyman(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.JOURNEYMAN, action);
    }

    default VillagerProfessionTradesPreRegistrable expert(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.EXPERT, action);
    }

    default VillagerProfessionTradesPreRegistrable master(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.MASTER, action);
    }

    VillagerProfessionTradesPostRegistrable register();
}
