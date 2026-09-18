package io.github.piscescup.fabricmc.api.trade;

import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author REN YuanTong
 * @since
 */
public record VillagerLevelTradeMetadata(
    TradeLevel level,
    TagKey<VillagerTrade> tradeTag,
    ResourceKey<TradeSet> tradeSetKey,
    Map<ResourceKey<VillagerTrade>, VillagerTrade> declaredTrades,
    Set<ResourceKey<VillagerTrade>> includedTrades,
    Set<TagKey<VillagerTrade>> includedTags,
    NumberProvider amount,
    boolean allowDuplicates,
    Optional<Identifier> randomSequence
) {
}