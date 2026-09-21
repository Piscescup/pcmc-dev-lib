package io.github.piscescup.fabricmc.api.trade;


import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Exposes the level callbacks of a completed profession trade declaration.
 *
 * <p>Pass this stage to
 * {@link io.github.piscescup.fabricmc.api.villager.VillagerProfessionPreRegistrable#tradeSetsByLevel(VillagerProfessionTradesPostRegistrable)
 * VillagerProfessionPreRegistrable.tradeSetsByLevel(...)} to build trade metadata
 * for that profession. A declaration can be reused for multiple professions;
 * its callbacks are executed separately for each application.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionTradesPreRegistrable
 */
public interface VillagerProfessionTradesPostRegistrable {
    /**
     * Returns the configured callbacks indexed by career level.
     *
     * <p>The supplied implementation returns its mutable backing map. Changes
     * affect subsequent applications of this declaration, while metadata already
     * built for a profession is unaffected.</p>
     *
     * @return the live callback map, in enum order; possibly empty
     */
    EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades();
}
