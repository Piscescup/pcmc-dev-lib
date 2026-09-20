package io.github.piscescup.fabricmc.datagen.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.store.villager.trade.ReadableVillagerTradesHolder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerTradeDatagenModel {
    private final Map<ResourceKey<VillagerTrade>, VillagerTrade> trades;
    private final Map<ResourceKey<TradeSet>, VillagerLevelTradeMetadata> tradeSets;

    private VillagerTradeDatagenModel(
        Map<ResourceKey<VillagerTrade>, VillagerTrade> trades,
        Map<ResourceKey<TradeSet>, VillagerLevelTradeMetadata> tradeSets
    ) {
        this.trades = Map.copyOf(trades);
        this.tradeSets = Map.copyOf(tradeSets);
    }

    public static VillagerTradeDatagenModel create(
        ReadableVillagerTradesHolder holder
    ) {
        Map<ResourceKey<VillagerTrade>, VillagerTrade> trades =
            new LinkedHashMap<>();

        Map<ResourceKey<TradeSet>, VillagerLevelTradeMetadata> tradeSets =
            new LinkedHashMap<>();

        holder.allTrades().values().stream()
            .flatMap(levels -> levels.values().stream())
            .forEach(metadata -> {
                metadata.declaredTrades().forEach((key, trade) -> {
                    if (trades.putIfAbsent(key, trade) != null) {
                        throw new IllegalStateException(
                            "Duplicate villager trade key: " + key.identifier()
                        );
                    }
                });

                if (tradeSets.putIfAbsent(
                    metadata.tradeSetKey(),
                    metadata
                ) != null) {
                    throw new IllegalStateException(
                        "Duplicate trade set key: "
                        + metadata.tradeSetKey().identifier()
                    );
                }
            });

        return new VillagerTradeDatagenModel(trades, tradeSets);
    }

    public Set<ResourceKey<VillagerTrade>> tradeKeys() {
        return trades.keySet();
    }

    public Set<ResourceKey<TradeSet>> tradeSetKeys() {
        return tradeSets.keySet();
    }

    public void bootstrapTrades(
        BootstrapContext<VillagerTrade> context
    ) {
        trades.forEach(context::register);
    }

    public void bootstrapTradeSets(
        BootstrapContext<TradeSet> context
    ) {
        HolderGetter<VillagerTrade> tradeLookup =
            context.lookup(Registries.VILLAGER_TRADE);

        tradeSets.forEach((key, metadata) -> {
            TradeSet tradeSet = new TradeSet(
                tradeLookup.getOrThrow(metadata.tradeTag()),
                metadata.amount(),
                metadata.allowDuplicates(),
                metadata.randomSequence()
            );

            context.register(key, tradeSet);
        });
    }

    public void registerBootstraps(RegistrySetBuilder builder) {
        builder.add(
            Registries.VILLAGER_TRADE,
            this::bootstrapTrades
        );

        builder.add(
            Registries.TRADE_SET,
            this::bootstrapTradeSets
        );
    }
}
