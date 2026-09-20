package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPostRegistrable;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPreRegistrable;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerProfessionTradesRegister
    implements VillagerProfessionTradesPreRegistrable, VillagerProfessionTradesPostRegistrable
{

    private final EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades = new EnumMap<>(TradeLevel.class);

    @Override
    public VillagerProfessionTradesPreRegistrable level(TradeLevel level, Consumer<VillagerLevelTradeBuilder> action) {
        NullCheck.requireNonNull(action, "action");

        this.trades.put(level, action);

        return this;
    }

    @Override
    public VillagerProfessionTradesPostRegistrable register() {

        return this;
    }


    @Override
    public EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades() {
        return trades;
    }
}
