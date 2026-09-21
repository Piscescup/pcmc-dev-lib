package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPostRegistrable;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPreRegistrable;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Stores the career-level callbacks of a profession trade declaration.
 *
 * <p>Each level retains its most recently assigned callback. Calling
 * {@link #register()} returns this object through its post-registration
 * interface; callback execution is deferred until a profession consumes it.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionTradesRegisterFactory
 * @see VillagerProfessionTradesPreRegistrable
 */
public final class VillagerProfessionTradesRegister
    implements VillagerProfessionTradesPreRegistrable, VillagerProfessionTradesPostRegistrable
{

    /** Retained callbacks in career-level order, also exposed by {@link #trades()}. */
    private final EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades = new EnumMap<>(TradeLevel.class);

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionTradesPreRegistrable level(TradeLevel level, Consumer<VillagerLevelTradeBuilder> action) {
        NullCheck.requireNonNull(action, "action");

        this.trades.put(level, action);

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionTradesPostRegistrable register() {

        return this;
    }


    /** {@inheritDoc} */
    @Override
    public EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades() {
        return trades;
    }
}
