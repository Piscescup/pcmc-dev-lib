package io.github.piscescup.fabricmc.api.villager;

import io.github.piscescup.fabricmc.api.PostRegistrable;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

/**
 * Provides post-registration operations for a {@link VillagerProfession}.
 *
 * <p>Returned by {@link VillagerProfessionPreRegistrable#register()}. Inherited
 * methods expose the registered profession, its identifier and resource key,
 * record translations, and collect the profession into a caller's collection.</p>
 *
 * <p>The supplied implementation uses the translation key
 * {@code entity.<namespace>.villager.<path>}. For example, profession
 * {@code example:trader} uses {@code entity.example.villager.trader}.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionPreRegistrable
 * @see PostRegistrable
 */
public interface VillagerProfessionPostRegistrable
    extends PostRegistrable<VillagerProfession, VillagerProfession, VillagerProfessionPostRegistrable>
{
}
