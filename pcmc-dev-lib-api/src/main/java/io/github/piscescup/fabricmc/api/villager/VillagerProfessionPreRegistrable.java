package io.github.piscescup.fabricmc.api.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.fabricmc.api.PreRegistrable;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPostRegistrable;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Configures a {@link VillagerProfession} before registration.
 *
 * <p>The factory selects the profession identifier and first exposes
 * {@link HeldJobSite} to select its required job-site predicate. That predicate
 * also becomes the default acquirable-job-site predicate. Requested items and
 * secondary POI blocks initially form empty sets, the work sound is initially
 * {@code null}, and no trade sets are configured.</p>
 *
 * <p>Typical usage, with a previously registered POI key and trade declaration:</p>
 * <pre>{@code
 * VillagerProfession profession = PROFESSIONS.path("trader")
 *     .heldJobSite(TRADER_POI)
 *     .workSound(SoundEvents.ANVIL_USE)
 *     .tradeSetsByLevel(TRADER_TRADES)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Trader")
 *     .get();
 * }</pre>
 *
 * <p>Finish configuration before calling {@link #register()} once. The supplied
 * implementation registers the profession and collects its trade metadata for
 * data generation; trade and trade-set resources must also be generated.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionPostRegistrable
 * @see VillagerProfessionTradesPostRegistrable
 */
public interface VillagerProfessionPreRegistrable
    extends PreRegistrable<VillagerProfession, VillagerProfession, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable>
{
    /**
     * Selects the job sites a villager may hold before optional configuration.
     */
    interface HeldJobSite {
        /**
         * Sets the predicate for job sites held by this profession.
         *
         * <p>The supplied implementation assigns the same predicate to
         * acquirable job sites. Call
         * {@link VillagerProfessionPreRegistrable#acquirableJobSite(Predicate)} on the
         * returned stage to override that default.</p>
         *
         * @param heldJobSite the predicate accepting held job sites; must not be {@code null}
         * @return the profession pre-registration stage
         * @throws NullPointerException if {@code heldJobSite} is {@code null}
         */
        VillagerProfessionPreRegistrable heldJobSite(Predicate<Holder<PoiType>> heldJobSite);

        /**
         * Selects a single POI type as both the held and default acquirable job site.
         *
         * @param jobSite the resource key matched against each POI holder
         * @return the profession pre-registration stage
         * @see #heldJobSite(Predicate)
         */
        default VillagerProfessionPreRegistrable heldJobSite(ResourceKey<PoiType> jobSite) {
            return heldJobSite(poiType -> poiType.is(jobSite));
        }
    }

    /**
     * Replaces the predicate for job sites this profession may acquire.
     *
     * @param acquirableJobSite the acquisition predicate; must not be {@code null}
     * @return this pre-registration stage
     * @throws NullPointerException if {@code acquirableJobSite} is {@code null}
     */
    VillagerProfessionPreRegistrable acquirableJobSite(Predicate<Holder<PoiType>> acquirableJobSite);

    /**
     * Replaces the items requested by villagers of this profession.
     *
     * @param requestedItems the immutable item set; initially empty
     * @return this pre-registration stage
     * @throws NullPointerException if {@code requestedItems} is {@code null}
     */
    VillagerProfessionPreRegistrable requestedItems(ImmutableSet<Item> requestedItems);

    /**
     * Replaces the blocks used as secondary points of interest.
     *
     * @param secondaryPoi the immutable secondary POI block set; initially empty
     * @return this pre-registration stage
     * @throws NullPointerException if {@code secondaryPoi} is {@code null}
     */
    VillagerProfessionPreRegistrable secondaryPoi(ImmutableSet<Block> secondaryPoi);

    /**
     * Sets the sound used while a villager of this profession works.
     *
     * @param workSound the work sound, or {@code null} to leave it unset
     * @return this pre-registration stage
     */
    VillagerProfessionPreRegistrable workSound(@Nullable SoundEvent workSound);

    /**
     * Builds and associates the declared trade sets with this profession.
     *
     * <p>The supplied implementation immediately invokes each level's callback
     * with a fresh builder and derives resource keys from this profession's
     * identifier. Calling this method again replaces matching levels and retains
     * levels absent from the new declaration. The metadata is collected when
     * {@link #register()} registers the profession.</p>
     *
     * @param tradesPostRegistrable the completed declaration of level callbacks
     * @return this pre-registration stage
     * @throws NullPointerException if {@code tradesPostRegistrable} is {@code null}
     * @see VillagerProfessionTradesPostRegistrable#trades()
     */
    VillagerProfessionPreRegistrable tradeSetsByLevel(VillagerProfessionTradesPostRegistrable tradesPostRegistrable);

}
