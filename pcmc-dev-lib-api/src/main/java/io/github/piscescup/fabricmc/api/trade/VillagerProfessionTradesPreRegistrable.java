package io.github.piscescup.fabricmc.api.trade;

import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;

import java.util.function.Consumer;

/**
 * Declares trade-building callbacks for a villager profession's career levels.
 *
 * <p>Callbacks are retained until the completed declaration is supplied to
 * {@link io.github.piscescup.fabricmc.api.villager.VillagerProfessionPreRegistrable#tradeSetsByLevel(VillagerProfessionTradesPostRegistrable)
 * VillagerProfessionPreRegistrable.tradeSetsByLevel(...)}. Each configured level
 * then receives a fresh {@link VillagerLevelTradeBuilder}. Unconfigured levels
 * contribute no trade set.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * VillagerProfessionTradesPostRegistrable trades =
 *     VillagerProfessionTradesRegisterFactory.create()
 *         .novice(level -> level
 *             .add("coal_for_emerald", VillagerLevelTradeBuilder.trade(
 *                 Items.COAL, 16, Items.EMERALD, 1, 16, 2))
 *             .amount(1))
 *         .register();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionTradesPostRegistrable
 */
public interface VillagerProfessionTradesPreRegistrable {
    /**
     * Selects the callback for one career level.
     *
     * <p>The supplied implementation replaces any callback already assigned
     * to this level; callbacks are not combined or executed here.</p>
     *
     * @param level the career level to configure; must not be {@code null}
     * @param action the callback that configures that level; must not be {@code null}
     * @return this pre-registration stage
     * @throws NullPointerException if {@code level} or {@code action} is {@code null}
     */
    VillagerProfessionTradesPreRegistrable level(
        TradeLevel level,
        Consumer<VillagerLevelTradeBuilder> action
    );

    /**
     * Selects the callback for {@link TradeLevel#NOVICE} (level 1).
     *
     * @param action the callback for novice trades
     * @return this pre-registration stage
     * @see #level(TradeLevel, Consumer)
     */
    default VillagerProfessionTradesPreRegistrable novice(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.NOVICE, action);
    }

    /**
     * Selects the callback for {@link TradeLevel#APPRENTICE} (level 2).
     *
     * @param action the callback for apprentice trades
     * @return this pre-registration stage
     * @see #level(TradeLevel, Consumer)
     */
    default VillagerProfessionTradesPreRegistrable apprentice(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.APPRENTICE, action);
    }

    /**
     * Selects the callback for {@link TradeLevel#JOURNEYMAN} (level 3).
     *
     * @param action the callback for journeyman trades
     * @return this pre-registration stage
     * @see #level(TradeLevel, Consumer)
     */
    default VillagerProfessionTradesPreRegistrable journeyman(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.JOURNEYMAN, action);
    }

    /**
     * Selects the callback for {@link TradeLevel#EXPERT} (level 4).
     *
     * @param action the callback for expert trades
     * @return this pre-registration stage
     * @see #level(TradeLevel, Consumer)
     */
    default VillagerProfessionTradesPreRegistrable expert(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.EXPERT, action);
    }

    /**
     * Selects the callback for {@link TradeLevel#MASTER} (level 5).
     *
     * @param action the callback for master trades
     * @return this pre-registration stage
     * @see #level(TradeLevel, Consumer)
     */
    default VillagerProfessionTradesPreRegistrable master(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.MASTER, action);
    }

    /**
     * Exposes the configured callbacks as a completed trade declaration.
     *
     * <p>The supplied implementation returns the same object without invoking
     * callbacks, freezing the configuration, or registering registry entries.
     * Finish configuring this declaration before attaching it to a profession.</p>
     *
     * @return the post-registration view of this declaration
     */
    VillagerProfessionTradesPostRegistrable register();
}
