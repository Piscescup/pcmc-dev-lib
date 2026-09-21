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
 * Stores shared profession trade metadata for data generation.
 *
 * <p>{@link #add(ResourceKey, VillagerLevelTradeMetadata)} merges a single
 * level and rejects conflicting metadata. {@link #addAll(ResourceKey, EnumMap)}
 * replaces a profession's entire level map and retains the supplied map directly.
 * Read operations return shared metadata values inside unmodifiable map snapshots.</p>
 *
 * <p>This singleton is unsynchronized. Finish registration and mutation before
 * data-generation consumers traverse its declarations.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum MutableVillagerTradeHolder
    implements ReadableVillagerTradesHolder
{
    /** The shared holder populated by profession registration. */
    INSTANCE;

    /** Professions in insertion order, each mapped to its career-level metadata. */
    private final Map<ResourceKey<VillagerProfession>, EnumMap<TradeLevel, VillagerLevelTradeMetadata>> professions =
        new LinkedHashMap<>();

    /**
     * Adds one profession-level metadata entry.
     *
     * <p>Equal metadata already stored at this level is accepted without
     * replacing the existing value.</p>
     *
     * @param profession the owning profession key; must not be {@code null}
     * @param metadata the level metadata to add; must not be {@code null}
     * @throws NullPointerException if {@code profession}, {@code metadata}, or its level is {@code null}
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

    /**
     * Replaces all metadata for a profession with the supplied level map.
     *
     * <p>The map is retained directly without copying or checking individual
     * entries. Subsequent mutations of that map are visible to this holder.</p>
     *
     * @param profession the owning profession key; must not be {@code null}
     * @param levelTrades the replacement level map; must not be {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public void addAll(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull EnumMap<TradeLevel, VillagerLevelTradeMetadata> levelTrades
    ) {
        Objects.requireNonNull(profession, "profession");
        Objects.requireNonNull(levelTrades, "metadata");

        this.professions.put(profession, levelTrades);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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
