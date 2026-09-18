package io.github.piscescup.fabricmc.impl.registers.villager.trade;

import io.github.piscescup.fabricmc.api.registers.villager.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.registers.villager.trade.VillagerTradePostRegistrable;
import io.github.piscescup.fabricmc.api.registers.villager.trade.VillagerTradePreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.fabricmc.impl.store.village.trade.MutableVillagerTradeHolder;
import io.github.piscescup.fabricmc.impl.store.village.trade.VillagerTradesImpl;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    @Override
    public VillagerTradePreRegistrable level(TradeLevel level, Consumer<VillagerLevelTradeBuilder> action) {
        NullCheck.requireNonNull(level, "level");
        NullCheck.requireNonNull(action, "action");

        LevelTradeBuilder builder = new LevelTradeBuilder(level);

        action.accept(builder);

        builder.trades.forEach((path, trade) -> {
            ResourceKey<VillagerTrade> tradeResourceKey = ResourceKey.create(
                Registries.VILLAGER_TRADE,
                Identifier.fromNamespaceAndPath(id.getNamespace(), path)
            );

            allTrades.add(level, tradeResourceKey);
        });

        return this;
    }

    @Contract(pure = true)
    @Override
    protected @NotNull String translateKey() {
        return "";
    }
    @Override
    public @NotNull VillagerTradePostRegistrable register() {
        MutableVillagerTradeHolder.INSTANCE.add(this.resourceKey, this.allTrades);
        return this;
    }

    private static final class LevelTradeBuilder
        implements VillagerLevelTradeBuilder
    {
        private final TradeLevel level;
        private final Map<String, VillagerTrade> trades;

        LevelTradeBuilder(TradeLevel level) {
            this.level = level;
            this.trades = new LinkedHashMap<>();
        }

        @Override
        public VillagerLevelTradeBuilder add(String path, VillagerTrade trade) {
            String tradePath = level.level() + "/" + path;
            trades.put(tradePath, trade);
            return this;
        }

        public TradeLevel getLevel() {
            return level;
        }

        public  Map<String, VillagerTrade> getTrades() {
            return trades;
        }
    }
}
