package io.github.piscescup.fabricmc.impl.store.village.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.store.villager.trade.ReadableVillagerTradesHolder;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum MutableVillagerTradeHolder
    implements ReadableVillagerTradesHolder
{
    INSTANCE;

    private final Map<ResourceKey<VillagerProfession>, EnumMap<TradeLevel, VillagerLevelTradeMetadata>> professions =
        new LinkedHashMap<>();

    /**
     * Adds one profession-level metadata entry.
     *
     * @throws IllegalStateException if different metadata has already been
     *                               declared for the same profession and level
     */
    public void add(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull VillagerLevelTradeMetadata metadata
    ) {
        NullCheck.requireNonNull(profession, "profession");
        NullCheck.requireNonNull(metadata, "metadata");

        EnumMap<TradeLevel, VillagerLevelTradeMetadata> levels =
            professions.computeIfAbsent(
                profession,
                ignored -> new EnumMap<>(TradeLevel.class)
            );

        VillagerLevelTradeMetadata previous =
            levels.putIfAbsent(
                metadata.level(),
                metadata
            );

        if (previous != null && !previous.equals(metadata)) {
            throw new IllegalStateException(
                "Duplicate villager profession trade metadata: "
                + profession.identifier()
                + ", level="
                + metadata.level()
            );
        }
    }

    public void addAll(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull EnumMap<TradeLevel, VillagerLevelTradeMetadata> levelTrades
    ) {
        Objects.requireNonNull(profession, "profession");
        Objects.requireNonNull(levelTrades, "metadata");

        this.professions.put(profession, levelTrades);
    }

    @Override
    public @Nullable VillagerLevelTradeMetadata searchByLevel(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull TradeLevel level
    ) {
        Objects.requireNonNull(profession, "profession");
        Objects.requireNonNull(level, "level");

        Map<TradeLevel, VillagerLevelTradeMetadata> levels =
            professions.get(profession);

        return levels == null
            ? null
            : levels.get(level);
    }

    @Override
    public @NotNull Map<TradeLevel, VillagerLevelTradeMetadata> tradesOf(
        @NotNull ResourceKey<VillagerProfession> profession
    ) {
        Objects.requireNonNull(profession, "profession");

        EnumMap<TradeLevel, VillagerLevelTradeMetadata> levels =
            professions.get(profession);

        if (levels == null) {
            return Map.of();
        }

        return Collections.unmodifiableMap(
            new EnumMap<>(levels)
        );
    }

    @Override
    public @NotNull Map<ResourceKey<VillagerProfession>, Map<TradeLevel, VillagerLevelTradeMetadata>> allTrades() {
        Map<ResourceKey<VillagerProfession>, Map<TradeLevel, VillagerLevelTradeMetadata>> result = new LinkedHashMap<>();

        professions.forEach(
            (profession, levels) ->
                result.put(
                    profession,
                    Collections.unmodifiableMap(
                        new EnumMap<>(levels)
                    )
                )
        );

        return Collections.unmodifiableMap(result);
    }
}
