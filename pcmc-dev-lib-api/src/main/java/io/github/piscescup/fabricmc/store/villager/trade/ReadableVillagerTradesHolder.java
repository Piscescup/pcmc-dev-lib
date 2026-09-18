package io.github.piscescup.fabricmc.store.villager.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author REN YuanTong
 * @since 1.0.0
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
     * @param profession the villager profession key
     * @return an unmodifiable level-to-metadata mapping
     */
    @NotNull
    Map<TradeLevel, VillagerLevelTradeMetadata> tradesOf(
        @NotNull ResourceKey<VillagerProfession> profession
    );

    /**
     * Returns all villager professions and their trade metadata.
     *
     * @return an unmodifiable profession-to-level mapping
     */
    @NotNull
    Map<ResourceKey<VillagerProfession>, Map<TradeLevel, VillagerLevelTradeMetadata>> allTrades();

    /**
     * Determines whether any trade metadata has been declared for the
     * specified profession.
     */
    default boolean containsProfession(
        @NotNull ResourceKey<VillagerProfession> profession
    ) {
        return !tradesOf(profession).isEmpty();
    }

    /**
     * Determines whether trade metadata exists for the specified profession
     * and career level.
     */
    default boolean contains(
        @NotNull ResourceKey<VillagerProfession> profession,
        @NotNull TradeLevel level
    ) {
        return searchByLevel(profession, level) != null;
    }
}
