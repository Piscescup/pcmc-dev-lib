package io.github.piscescup.fabricmc.store.villager.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Provides access to collected trade metadata by profession and career level.
 *
 * <p>The supplied registration implementation populates this holder when a
 * profession is registered. Data generators read the declarations to build
 * trade entries, level tags, and trade sets. Complete profession registration
 * before creating a data-generation model from this holder.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerLevelTradeMetadata
 */
public interface ReadableVillagerTradesHolder {

    /**
     * Finds the trade metadata declared for the specified profession
     * and career level.
     *
     * @param profession the villager profession key
     * @param level      the career level
     * @return the associated metadata, or {@code null} if none was declared
     */
    @Nullable
    VillagerLevelTradeMetadata searchByLevel(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull TradeLevel level
    );

    /**
     * Returns all configured levels for the specified profession.
     *
     * <p>The supplied implementation returns a snapshot of the level mapping.</p>
     *
     * @param profession the villager profession key
     * @return an unmodifiable level-to-metadata mapping, empty for an unknown profession
     */
    @NotNull
    Map<TradeLevel, VillagerLevelTradeMetadata> tradesOf(
        @NotNull ResourceKey<VillagerProfession> profession
    );

    /**
     * Returns all villager professions and their trade metadata.
     *
     * <p>The supplied implementation copies both map levels. Later holder
     * changes do not alter those maps; metadata values are shared.</p>
     *
     * @return an unmodifiable profession-to-level mapping
     */
    @NotNull
    Map<ResourceKey<VillagerProfession>, Map<TradeLevel, VillagerLevelTradeMetadata>> allTrades();

    /**
     * Determines whether any trade metadata has been declared for the
     * specified profession.
     *
     * @param profession the villager profession key
     * @return {@code true} if at least one level has metadata
     */
    default boolean containsProfession(
        @NotNull ResourceKey<VillagerProfession> profession
    ) {
        return !tradesOf(profession).isEmpty();
    }

    /**
     * Determines whether trade metadata exists for the specified profession
     * and career level.
     *
     * @param profession the villager profession key
     * @param level the career level to query
     * @return {@code true} if the selected level has metadata
     */
    default boolean contains(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull TradeLevel level
    ) {
        return searchByLevel(profession, level) != null;
    }
}
