package io.github.piscescup.fabricmc.api.registers.villager.trade;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.function.Consumer;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerTradePreRegistrable
    extends PreRegistrable<VillagerTrade, VillagerTrade, VillagerTradePreRegistrable, VillagerTradePostRegistrable>
{

    VillagerTradePreRegistrable level(
        TradeLevel level,
        Consumer<VillagerLevelTradeBuilder> action
    );

    default VillagerTradePreRegistrable novice(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.NOVICE, action);
    }

    default VillagerTradePreRegistrable apprentice(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.APPRENTICE, action);
    }

    default VillagerTradePreRegistrable journeyman(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.JOURNEYMAN, action);
    }

    default VillagerTradePreRegistrable expert(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.EXPERT, action);
    }

    default VillagerTradePreRegistrable master(
        Consumer<VillagerLevelTradeBuilder> action
    ) {
        return level(TradeLevel.MASTER, action);
    }

}
