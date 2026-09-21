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
 * Describes the trade resources and selection settings for one career level.
 *
 * <p>Declared trades supply new registry entries. Included trade keys and
 * nested tags reference existing resources. Data generation combines these
 * members in {@code tradeTag} and creates {@code tradeSetKey} from that tag and
 * the selection settings.</p>
 *
 * <p>This record retains its arguments without validation or defensive copies.
 * The supplied level builder provides unmodifiable snapshots of its maps and
 * sets; callers constructing this record directly control their mutability.</p>
 *
 * @param level the career level described by this metadata
 * @param tradeTag the tag gathering all trades available at this level
 * @param tradeSetKey the registry key of the generated trade set
 * @param declaredTrades new trade definitions indexed by their registry keys
 * @param includedTrades existing trade keys added to the generated tag
 * @param includedTags existing trade tags nested in the generated tag
 * @param amount the provider determining how many trades to select
 * @param allowDuplicates whether selection may include duplicate trades
 * @param randomSequence the optional random-sequence identifier for selection
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerLevelTradeBuilder
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