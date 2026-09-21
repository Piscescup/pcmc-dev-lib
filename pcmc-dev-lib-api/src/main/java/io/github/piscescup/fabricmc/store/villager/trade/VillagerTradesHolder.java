package io.github.piscescup.fabricmc.store.villager.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.Collection;
import java.util.Optional;

/**
 * Describes a lookup of {@link VillagerTrades} groups by villager trade key.
 *
 * <p>Implementations determine group membership and collection mutability.
 * For metadata indexed by profession and level, use
 * {@link ReadableVillagerTradesHolder}.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerTradesHolder {
    /**
     * Finds the trade group associated with a trade key.
     *
     * @param villagerTrade the trade registry key to look up
     * @return the associated group, or an empty optional when absent
     */
    Optional<VillagerTrades> searchBy(
        ResourceKey<VillagerTrade> villagerTrade
    );

    /**
     * Returns all trade groups known to this holder.
     *
     * @return the collection of groups, possibly empty
     */
    Collection<VillagerTrades> all();

    /**
     * Determines whether a trade key is associated with a group.
     *
     * @param villagerTrade the trade registry key to look up
     * @return {@code true} if the key is present
     */
    boolean contains(ResourceKey<VillagerTrade> villagerTrade);
}
