package io.github.piscescup.fabricmc.datagen.tag;

import io.github.piscescup.fabricmc.store.villager.trade.ReadableVillagerTradesHolder;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.fabricmc.store.villager.trade.VillagerTrades;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class VillagerTradeGenerator
    extends FabricTagsProvider<VillagerTrade>
{
    private final Map<ResourceKey<VillagerTrade>, VillagerTrades> trades;

    /**
     * Constructs a new {@link FabricTagsProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output               the {@link FabricPackOutput} instance
     * @param registryKey
     * @param registryLookupFuture the backing registry for the tag type
     */
    public VillagerTradeGenerator(
        FabricPackOutput output,
        CompletableFuture<HolderLookup.Provider> registryLookupFuture,
        ReadableVillagerTradesHolder tradesHolder
    ) {
        super(
            output, Registries.VILLAGER_TRADE, registryLookupFuture
        );

        trades = tradesHolder.all();
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        HolderLookup.RegistryLookup<VillagerTrade> tradeLookup = registries.lookupOrThrow(Registries.VILLAGER_TRADE);
        for (var tradeEntry : this.trades.entrySet()) {
            ResourceKey<VillagerTrade> tradeKey = tradeEntry.getKey();

            Map<TradeLevel, Collection<ResourceKey<VillagerTrade>>> trades = tradeEntry.getValue().trades();

            Identifier identifier = tradeKey.identifier();

            String namespace = identifier.getNamespace();
            String path = identifier.getPath();

            for (var trade : trades.entrySet()) {
                TradeLevel level = trade.getKey();
                Collection<ResourceKey<VillagerTrade>> tradesOnLevel = trade.getValue();

                TagKey<VillagerTrade> tradeTag = TagKey.create(
                    Registries.VILLAGER_TRADE,
                    Identifier.fromNamespaceAndPath(namespace, path + "/level_" + level.level())
                );

                TagAppender<VillagerTrade> tagAppender = tag(tradeTag);

                tagAppender.addAll(tradesOnLevel);

            }

        }
    }

}
