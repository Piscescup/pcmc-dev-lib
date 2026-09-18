package io.github.piscescup.fabricmc.api.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.fabricmc.api.PreRegistrable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerProfessionPreRegistrable
    extends PreRegistrable<VillagerProfession, VillagerProfession, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable>
{
    interface HeldJobSite {
        VillagerProfessionPreRegistrable heldJobSite(Predicate<Holder<PoiType>> heldJobSite);

        default VillagerProfessionPreRegistrable heldJobSite(ResourceKey<PoiType> jobSite) {
            return heldJobSite(poiType -> poiType.is(jobSite));
        }
    }

    VillagerProfessionPreRegistrable acquirableJobSite(Predicate<Holder<PoiType>> acquirableJobSite);

    VillagerProfessionPreRegistrable requestedItems(ImmutableSet<Item> requestedItems);

    VillagerProfessionPreRegistrable secondaryPoi(ImmutableSet<Block> secondaryPoi);

    VillagerProfessionPreRegistrable workSound(@Nullable SoundEvent workSound);

    VillagerProfessionPreRegistrable tradeSetsByLevel(Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel);

}
