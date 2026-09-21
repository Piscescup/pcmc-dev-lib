package io.github.piscescup.fabricmc.store.villager.trade;

/**
 * Maps the five villager career stages to their numeric trade-set levels.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum TradeLevel {
    /** The initial career stage, level 1. */
    NOVICE(1),

    /** The second career stage, level 2. */
    APPRENTICE(2),

    /** The third career stage, level 3. */
    JOURNEYMAN(3),

    /** The fourth career stage, level 4. */
    EXPERT(4),

    /** The final career stage, level 5. */
    MASTER(5);

    /**
     * All career stages in declaration order. This shared array is mutable;
     * use {@link #values()} when an independent array is needed.
     */
    public static final TradeLevel[] TRADE_LEVELS = TradeLevel.values();

    private final int level;

    TradeLevel(final int level) {
        this.level = level;
    }

    /**
     * Returns the numeric career level used by profession trade-set mappings.
     *
     * @return the career level, from 1 through 5
     */
    public int level() {
        return level;
    }
}
