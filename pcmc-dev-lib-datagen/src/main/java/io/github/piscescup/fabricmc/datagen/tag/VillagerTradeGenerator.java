package io.github.piscescup.fabricmc.datagen.tag;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.store.villager.trade.ReadableVillagerTradesHolder;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class VillagerTradeGenerator
    extends FabricTagsProvider<VillagerTrade>
{
    private final ReadableVillagerTradesHolder holder;

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
        ReadableVillagerTradesHolder holder
    ) {
        super(
            output, Registries.VILLAGER_TRADE, registryLookupFuture
        );
        this.holder = holder;
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        HolderLookup.RegistryLookup<VillagerTrade> tradeLookup = registries.lookupOrThrow(Registries.VILLAGER_TRADE);
        Set<Map.Entry<ResourceKey<VillagerProfession>, Map<TradeLevel, VillagerLevelTradeMetadata>>> tradeEntries = holder.allTrades()
            .entrySet();

        for (Map.Entry<ResourceKey<VillagerProfession>, Map<TradeLevel, VillagerLevelTradeMetadata>> tradeEntry : tradeEntries) {
            Set<Map.Entry<TradeLevel, VillagerLevelTradeMetadata>> trades = tradeEntry.getValue()
                .entrySet();

            for(var trade : trades) {
                TradeLevel level = trade.getKey();
                VillagerLevelTradeMetadata tradeMetadata = trade.getValue();

                TagAppender<VillagerTrade> tagAppender = tag(tradeMetadata.tradeTag());

                tradeMetadata.declaredTrades()
                    .keySet()
                    .forEach(tagAppender::add);

                tradeMetadata.includedTrades()
                    .forEach(tagAppender::add);

                tradeMetadata.includedTags()
                    .forEach(tagAppender::addTag);
            }

        }
    }

}
