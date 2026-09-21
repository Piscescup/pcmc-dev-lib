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
 * Generates the trade tags referenced by profession-level trade sets.
 *
 * <p>For each level, the provider adds declared trade keys, included trade
 * keys, and nested tags as required members. The holder is retained and queried
 * when tags are generated. Complete registration before creating the matching
 * registry model so both providers use the same declarations.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerLevelTradeMetadata
 */
public class VillagerTradeTagGenerator
    extends FabricTagsProvider<VillagerTrade>
{
    /** Metadata source queried when Fabric invokes tag generation. */
    private final ReadableVillagerTradesHolder holder;

    /**
     * Creates a tag provider targeting {@link Registries#VILLAGER_TRADE}.
     *
     * @param output the Fabric pack output receiving generated tag files
     * @param registryLookupFuture the future supplying the trade registry lookup
     * @param holder the source of profession-level tag declarations
     */
    public VillagerTradeTagGenerator(
        FabricPackOutput output,
        CompletableFuture<HolderLookup.Provider> registryLookupFuture,
        ReadableVillagerTradesHolder holder
    ) {
        super(
            output, Registries.VILLAGER_TRADE, registryLookupFuture
        );
        this.holder = holder;
    }

    /**
     * Populates each declared level tag from its trade keys and nested tags.
     *
     * @param registries the registry lookup for this generation run
     */
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
