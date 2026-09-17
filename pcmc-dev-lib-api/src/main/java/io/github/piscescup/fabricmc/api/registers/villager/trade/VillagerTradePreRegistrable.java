package io.github.piscescup.fabricmc.api.registers.villager.trade;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerTradePreRegistrable
    extends PreRegistrable<VillagerTrade, VillagerTrade, VillagerTradePreRegistrable, VillagerTradePostRegistrable>
{
    VillagerTradePreRegistrable novice(VillagerTrade noviceTrade);

    default VillagerTradePreRegistrable novice(@NotNull Collection<VillagerTrade> noviceTrades) {
        noviceTrades.forEach(this::novice);
        return this;
    }

    VillagerTradePreRegistrable apprentice(VillagerTrade apprenticeTrade);

    default VillagerTradePreRegistrable apprentice(@NotNull Collection<VillagerTrade> apprenticeTrades) {
        apprenticeTrades.forEach(this::apprentice);
        return this;
    }

    VillagerTradePreRegistrable journeyman(VillagerTrade journeymanTrade);

    default VillagerTradePreRegistrable journeyman(@NotNull Collection<VillagerTrade> journeymanTrades) {
        journeymanTrades.forEach(this::journeyman);
        return this;
    }

    VillagerTradePreRegistrable expert(VillagerTrade expertTrade);

    default VillagerTradePreRegistrable expert(@NotNull Collection<VillagerTrade> expertTrades) {
        expertTrades.forEach(this::expert);
        return this;
    }

    VillagerTradePreRegistrable master(VillagerTrade masterTrade);

    default VillagerTradePreRegistrable master(@NotNull Collection<VillagerTrade> masterTrades) {
        masterTrades.forEach(this::master);
        return this;
    }
}
