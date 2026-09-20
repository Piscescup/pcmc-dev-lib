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
 *
 * @author REN YuanTong
 * @since
 */
public final class LevelTradesBuilder
    implements VillagerLevelTradeBuilder
{
    private final TradeLevel level;

    private final Map<String, VillagerTrade> tradeMap = new TreeMap<>();

    private final Map<ResourceKey<VillagerTrade>, VillagerTrade> declaredTrades =
        new LinkedHashMap<>();

    private final Set<ResourceKey<VillagerTrade>> includedTrades =
        new LinkedHashSet<>();

    private final Set<TagKey<VillagerTrade>> includedTags =
        new LinkedHashSet<>();

    private NumberProvider amount = ConstantValue.exactly(2.0F);
    private boolean allowDuplicates;
    private Optional<Identifier> randomSequence = Optional.empty();

    public LevelTradesBuilder(
        @NotNull TradeLevel level
    ) {
        this.level = NullCheck.requireNonNull(
            level,
            "level"
        );
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder add(
        @NotNull String path,
        @NotNull VillagerTrade trade
    ) {
        NullCheck.requireNonNull(path, "path");

        VillagerTrade previous = tradeMap.put(path, trade);

        StateCheck.checkState(previous != null, "The trade for villager %s is repeated", trade);
        return this;
    }

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

    @Override
    public @NotNull VillagerLevelTradeBuilder includeTag(
        @NotNull TagKey<VillagerTrade> tag
    ) {
        NullCheck.requireNonNull(tag, "tag");

        includedTags.add(tag);
        return this;
    }

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

    @Override
    public @NotNull VillagerLevelTradeBuilder allowDuplicates(
        boolean allowDuplicates
    ) {
        this.allowDuplicates = allowDuplicates;
        return this;
    }

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
