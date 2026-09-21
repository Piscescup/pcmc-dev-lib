package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;
import io.github.piscescup.util.validation.StateCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Builds the trade metadata for one profession career level.
 *
 * <p>New trade paths are sorted before resource keys are assigned by
 * {@link #buildBy(Identifier)}. Included trade keys and tags retain their first
 * insertion order. Selection defaults to two trades without duplicates and
 * an empty random-sequence optional.</p>
 *
 * <p>Use a fresh builder for each profession and level. Building retains the
 * resolved declared-trade keys internally, so this mutable builder should not
 * be reused with a different profession identifier.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerLevelTradeBuilder
 * @see VillagerLevelTradeMetadata
 */
public final class LevelTradesBuilder
    implements VillagerLevelTradeBuilder
{
    /** Career level used when deriving trade and trade-set identifiers. */
    private final TradeLevel level;

    /** Local trade paths in lexicographic order, resolved during building. */
    private final Map<String, VillagerTrade> tradeMap = new TreeMap<>();

    /** Resolved declarations retained across build calls. */
    private final Map<ResourceKey<VillagerTrade>, VillagerTrade> declaredTrades =
        new LinkedHashMap<>();

    /** Existing trade references in first-inclusion order. */
    private final Set<ResourceKey<VillagerTrade>> includedTrades =
        new LinkedHashSet<>();

    /** Nested trade tags in first-inclusion order. */
    private final Set<TagKey<VillagerTrade>> includedTags =
        new LinkedHashSet<>();

    /** Selection count, initially the constant 2. */
    private NumberProvider amount = ConstantValue.exactly(2.0F);
    /** Whether selection permits duplicates; initially false. */
    private boolean allowDuplicates;
    /** Explicit random-sequence override, initially empty. */
    private Optional<Identifier> randomSequence = Optional.empty();

    /**
     * Creates an empty level builder with default selection settings.
     *
     * @param level the career level; must not be {@code null}
     * @throws NullPointerException if {@code level} is {@code null}
     */
    public LevelTradesBuilder(
        @NotNull TradeLevel level
    ) {
        this.level = NullCheck.requireNonNull(
            level,
            "level"
        );
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull VillagerLevelTradeBuilder add(
        @NotNull String path,
        @NotNull VillagerTrade trade
    ) {
        NullCheck.requireNonNull(path, "path");

        VillagerTrade previous = this.tradeMap.putIfAbsent(
            path,
            trade
        );

        StateCheck.checkState(previous == null, "The trade for villager %s is repeated", trade);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull VillagerLevelTradeBuilder include(
        @NotNull ResourceKey<VillagerTrade> trade
    ) {
        NullCheck.requireNonNull(trade, "trade");

        if (!declaredTrades.containsKey(trade)) {
            includedTrades.add(trade);
        }

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull VillagerLevelTradeBuilder includeTag(
        @NotNull TagKey<VillagerTrade> tag
    ) {
        NullCheck.requireNonNull(tag, "tag");

        includedTags.add(tag);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull VillagerLevelTradeBuilder amount(
        @NotNull NumberProvider amount
    ) {
        this.amount = NullCheck.requireNonNull(
            amount,
            "amount"
        );

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull VillagerLevelTradeBuilder allowDuplicates(
        boolean allowDuplicates
    ) {
        this.allowDuplicates = allowDuplicates;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull VillagerLevelTradeBuilder randomSequence(
        @NotNull Identifier randomSequence
    ) {
        this.randomSequence = Optional.of(
            NullCheck.requireNonNull(
                randomSequence,
                "randomSequence"
            )
        );

        return this;
    }

    /**
     * Resolves resource identifiers and snapshots this level's declarations.
     *
     * <p>For profession {@code example:trader} and level 1, the tag and trade
     * set both use {@code example:trader/level_1}. A declared trade with local
     * path {@code coal_for_emerald} uses
     * {@code example:trader1/coal_for_emerald}.</p>
     *
     * <p>Returned maps and sets are unmodifiable copies; trade values and the
     * number provider are shared. The random-sequence optional remains empty
     * unless {@link #randomSequence(Identifier)} supplied an override.</p>
     *
     * @param professionId the identifier of the profession owning this level
     * @return the resolved level metadata
     * @throws NullPointerException if {@code professionId} is {@code null}
     */
    @NotNull
    public VillagerLevelTradeMetadata buildBy(Identifier professionId) {

        Identifier levelId = professionId.withSuffix("/level_" + level.level());

        TagKey<VillagerTrade> tradeTag = TagKey.create(
            Registries.VILLAGER_TRADE,
            levelId
        );

        tradeMap.forEach((path, trade) -> {
            ResourceKey<VillagerTrade> villagerTradeResourceKey = ResourceKey.create(
                Registries.VILLAGER_TRADE,
                professionId.withSuffix(Integer.toString(level.level()))
                    .withSuffix("/" + path)
            );

            declaredTrades.put(villagerTradeResourceKey, trade);
        });

        ResourceKey<TradeSet> tradeSetKey = ResourceKey.create(
            Registries.TRADE_SET,
            levelId
        );

        Optional<Identifier> effectiveRandomSequence =
            randomSequence.isPresent()
                ? randomSequence
                : Optional.of(levelId.withPrefix("trade_set/"));

        return new VillagerLevelTradeMetadata(
            level,
            tradeTag,
            tradeSetKey,
            Collections.unmodifiableMap(
                new LinkedHashMap<>(declaredTrades)
            ),
            Collections.unmodifiableSet(
                new LinkedHashSet<>(includedTrades)
            ),
            Collections.unmodifiableSet(
                new LinkedHashSet<>(includedTags)
            ),
            amount,
            allowDuplicates,
            randomSequence
        );
    }
}
