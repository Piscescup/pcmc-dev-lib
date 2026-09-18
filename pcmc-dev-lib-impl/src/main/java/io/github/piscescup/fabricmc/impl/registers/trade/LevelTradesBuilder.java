package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
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
final class LevelTradesBuilder
    implements VillagerLevelTradeBuilder
{
    private final String namespace;
    private final Identifier professionId;
    private final TradeLevel level;

    private final TagKey<VillagerTrade> tradeTag;
    private final ResourceKey<TradeSet> tradeSetKey;

    private final Map<ResourceKey<VillagerTrade>, VillagerTrade>
        declaredTrades = new LinkedHashMap<>();

    private final Set<ResourceKey<VillagerTrade>>
        includedTrades = new LinkedHashSet<>();

    private final Set<TagKey<VillagerTrade>>
        includedTags = new LinkedHashSet<>();

    private NumberProvider amount = ConstantValue.exactly(2.0F);
    private boolean allowDuplicates;
    private Optional<Identifier> randomSequence;

    LevelTradesBuilder(
        @NotNull String namespace,
        @NotNull Identifier professionId,
        @NotNull TradeLevel level
    ) {
        this.namespace = Objects.requireNonNull(
            namespace,
            "namespace"
        );
        this.professionId = Objects.requireNonNull(
            professionId,
            "professionId"
        );
        this.level = Objects.requireNonNull(
            level,
            "level"
        );

        Identifier levelId = Identifier.fromNamespaceAndPath(
            professionId.getNamespace(),
            professionId.getPath() + "/level_" + level.level()
        );

        this.tradeTag = TagKey.create(
            Registries.VILLAGER_TRADE,
            levelId
        );

        this.tradeSetKey = ResourceKey.create(
            Registries.TRADE_SET,
            levelId
        );

        this.randomSequence = Optional.of(
            Identifier.fromNamespaceAndPath(
                levelId.getNamespace(),
                "trade_set/" + levelId.getPath()
            )
        );
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder add(
        @NotNull String path,
        @NotNull VillagerTrade trade
    ) {
        Objects.requireNonNull(path, "path");

        Identifier id = Identifier.fromNamespaceAndPath(
            namespace,
            professionId.getPath()
            + "/"
            + level.level()
            + "/"
            + path
        );

        return add(id, trade);
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder add(
        @NotNull Identifier id,
        @NotNull VillagerTrade trade
    ) {
        Objects.requireNonNull(id, "id");

        ResourceKey<VillagerTrade> key = ResourceKey.create(
            Registries.VILLAGER_TRADE,
            id
        );

        return add(key, trade);
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder add(
        @NotNull ResourceKey<VillagerTrade> key,
        @NotNull VillagerTrade trade
    ) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(trade, "trade");

        VillagerTrade previous = declaredTrades.putIfAbsent(
            key,
            trade
        );

        if (previous != null && previous != trade) {
            throw new IllegalStateException(
                "Duplicate villager trade id: "
                + key.identifier()
            );
        }

        /*
         * If the same key was previously included as an external trade,
         * the locally declared value takes precedence.
         */
        includedTrades.remove(key);

        return this;
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder include(
        @NotNull ResourceKey<VillagerTrade> trade
    ) {
        Objects.requireNonNull(trade, "trade");

        if (!declaredTrades.containsKey(trade)) {
            includedTrades.add(trade);
        }

        return this;
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder includeTag(
        @NotNull TagKey<VillagerTrade> tag
    ) {
        Objects.requireNonNull(tag, "tag");

        if (tradeTag.equals(tag)) {
            throw new IllegalArgumentException(
                "A villager trade tag cannot include itself: "
                + tag.location()
            );
        }

        includedTags.add(tag);
        return this;
    }

    @Override
    public @NotNull VillagerLevelTradeBuilder amount(
        @NotNull NumberProvider amount
    ) {
        this.amount = Objects.requireNonNull(
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
            Objects.requireNonNull(
                randomSequence,
                "randomSequence"
            )
        );

        return this;
    }

    @NotNull
    VillagerLevelTradeMetadata build() {
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
