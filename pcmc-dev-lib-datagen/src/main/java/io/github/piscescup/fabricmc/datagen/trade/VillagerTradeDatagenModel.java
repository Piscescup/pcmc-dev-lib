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
 * Collects trade definitions and level metadata for dynamic-registry generation.
 *
 * <p>{@link #create(ReadableVillagerTradesHolder)} snapshots the holder's
 * declared trades and trade sets. Included trade keys and tags remain external
 * references and are emitted as tag members by the trade tag provider.</p>
 *
 * <p>Register the model's bootstraps through {@link #registerBootstraps(RegistrySetBuilder)}
 * before the dynamic-registry providers resolveSubPath its keys. Complete profession
 * registration before creating this model; later holder additions are absent
 * from its copied maps.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerTradeRegistryGenerator
 * @see TradeSetRegistryGenerator
 */
public final class VillagerTradeDatagenModel {
    /** Immutable map of newly declared trade entries. */
    private final Map<ResourceKey<VillagerTrade>, VillagerTrade> trades;
    /** Immutable map of trade-set keys to their retained level metadata. */
    private final Map<ResourceKey<TradeSet>, VillagerLevelTradeMetadata> tradeSets;

    /**
     * Copies the assembled registry maps, retaining their values.
     *
     * @param trades the newly declared trade definitions
     * @param tradeSets the metadata used to construct trade sets
     */
    private VillagerTradeDatagenModel(
        Map<ResourceKey<VillagerTrade>, VillagerTrade> trades,
        Map<ResourceKey<TradeSet>, VillagerLevelTradeMetadata> tradeSets
    ) {
        this.trades = Map.copyOf(trades);
        this.tradeSets = Map.copyOf(tradeSets);
    }

    /**
     * Creates a model from all profession levels currently exposed by a holder.
     *
     * <p>Repeated trade or trade-set keys are rejected even when their values
     * are equal. The resulting maps are immutable shallow copies.</p>
     *
     * @param holder the populated trade metadata holder
     * @return a new model containing the current declarations
     * @throws NullPointerException if {@code holder} is {@code null}
     * @throws IllegalStateException if a trade or trade-set key is declared more than once
     */
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

    /**
     * Returns the keys of newly declared trades to be emitted by the provider.
     *
     * @return an unmodifiable set of declared trade keys
     */
    public Set<ResourceKey<VillagerTrade>> tradeKeys() {
        return trades.keySet();
    }

    /**
     * Returns the trade-set keys to be emitted by the provider.
     *
     * @return an unmodifiable set of generated trade-set keys
     */
    public Set<ResourceKey<TradeSet>> tradeSetKeys() {
        return tradeSets.keySet();
    }

    /**
     * Registers every declared trade in the supplied bootstrap context.
     *
     * @param context the bootstrap context for {@link Registries#VILLAGER_TRADE}
     */
    public void bootstrapTrades(
        BootstrapContext<VillagerTrade> context
    ) {
        trades.forEach(context::register);
    }

    /**
     * Constructs and registers trade sets from their level metadata.
     *
     * <p>Each set references the level's trade tag and retains its amount
     * provider, duplicate-selection flag, and optional random sequence.</p>
     *
     * @param context the bootstrap context for {@link Registries#TRADE_SET}
     */
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

    /**
     * Adds this model's trade and trade-set bootstraps to a registry builder.
     *
     * @param builder the registry builder receiving both bootstrap callbacks
     * @throws NullPointerException if {@code builder} is {@code null}
     */
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
