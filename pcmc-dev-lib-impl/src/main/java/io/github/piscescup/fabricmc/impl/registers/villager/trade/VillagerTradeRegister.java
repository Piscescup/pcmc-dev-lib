package io.github.piscescup.fabricmc.impl.registers.villager.trade;

import io.github.piscescup.fabricmc.api.registers.villager.trade.VillagerTradePostRegistrable;
import io.github.piscescup.fabricmc.api.registers.villager.trade.VillagerTradePreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.fabricmc.impl.store.village.trade.MutableVillagerTradeHolder;
import io.github.piscescup.fabricmc.impl.store.village.trade.VillagerTradesImpl;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerTradeRegister
    extends Register<VillagerTrade, VillagerTrade, VillagerTradePreRegistrable, VillagerTradePostRegistrable>
    implements VillagerTradePreRegistrable, VillagerTradePostRegistrable
{
    private final VillagerTradesImpl allTrades;

    VillagerTradeRegister(Identifier id) {
        super(Registries.VILLAGER_TRADE, id);
        this.allTrades = new VillagerTradesImpl();
    }

    @Contract(pure = true)
    @Override
    protected @NotNull String translateKey() {
        return "";
    }

    @Override
    public VillagerTradePreRegistrable novice(VillagerTrade noviceTrade) {
        this.allTrades.add(TradeLevel.NOVICE, noviceTrade);
        return this;
    }

    @Override
    public VillagerTradePreRegistrable apprentice(VillagerTrade apprenticeTrade) {
        this.allTrades.add(TradeLevel.APPRENTICE, apprenticeTrade);
        return this;
    }

    @Override
    public VillagerTradePreRegistrable journeyman(VillagerTrade journeymanTrade) {
        this.allTrades.add(TradeLevel.JOURNEYMAN, journeymanTrade);
        return this;
    }

    @Override
    public VillagerTradePreRegistrable expert(VillagerTrade expertTrade) {
        this.allTrades.add(TradeLevel.EXPERT, expertTrade);
        return this;
    }

    @Override
    public VillagerTradePreRegistrable master(VillagerTrade masterTrade) {
        this.allTrades.add(TradeLevel.MASTER, masterTrade);
        return this;
    }

    @Override
    public @NotNull VillagerTradePostRegistrable register() {
        MutableVillagerTradeHolder.INSTANCE.add(this.resourceKey, this.allTrades);
        return this;
    }
}
