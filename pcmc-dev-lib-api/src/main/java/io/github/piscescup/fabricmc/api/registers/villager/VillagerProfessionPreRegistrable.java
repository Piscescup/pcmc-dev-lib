package io.github.piscescup.fabricmc.api.registers.villager;

import io.github.piscescup.fabricmc.api.registers.PreRegistrable;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

import java.util.function.Predicate;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerProfessionPreRegistrable
    extends PreRegistrable<VillagerProfession, VillagerProfession, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable>
{
    interface JobSite {
        VillagerProfessionPreRegistrable jobSite(Predicate<Holder<PoiType>> heldJobSite);

        default VillagerProfessionPreRegistrable jobSite(ResourceKey<PoiType> jobSite) {


            return jobSite(poiType -> poiType.is(jobSite));
        }
    }
    //
    // VillagerProfessionPreRegistrable acquirableJobSite(Predicate<Holder<PoiType>> acquirableJobSite);
    //
    // VillagerProfessionPreRegistrable requestedItems(ImmutableSet<Item> requiredItems);
    //
    // VillagerProfessionPreRegistrable secondaryPoi(ImmutableSet<Block> secondaryPoi);
    //
    // VillagerProfessionPreRegistrable workSound(@Nullable SoundEvent sound);
    //
    // VillagerProfessionPreRegistrable novice();

}
