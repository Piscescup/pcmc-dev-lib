package io.github.piscescup.fabricmc.store.villager.trade;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum TradeLevel {
    NOVICE(1),

    APPRENTICE(2),

    JOURNEYMAN(3),

    EXPERT(4),

    MASTER(5);

    public static final TradeLevel[] TRADE_LEVELS = TradeLevel.values();

    private final int level;

    TradeLevel(final int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}
