package io.github.piscescup.fabricmc.impl.registers.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.fabricmc.api.registers.PostRegistrable;
import io.github.piscescup.fabricmc.api.registers.villager.VillagerProfessionPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.villager.VillagerProfessionPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class VillagerProfessionRegister
    extends Register<VillagerProfession, VillagerProfession, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable>
    implements VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable,
                VillagerProfessionPreRegistrable.JobSite
{
    private Component name;
    private Predicate<Holder<PoiType>> heldJobSite;
    private Predicate<Holder<PoiType>> acquirableJobSite;
    private ImmutableSet<Item> requestedItems;
    private ImmutableSet<Block> secondaryPoi;
    private @Nullable SoundEvent workSound;
    private Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel;

    VillagerProfessionRegister(Identifier id) {
        super(BuiltInRegistries.VILLAGER_PROFESSION, id);
    }

    /**
     * Derives the translation key used for localized display text.
     *
     * <p>Implementations may obtain the key from the registered value, so this
     * method is used after registration has completed successfully.</p>
     *
     * @return the non-null, non-blank translation key
     */
    @Override
    protected String translateKey() {
        return "entity." + this.id.getNamespace() + ".villager." + this.id.getPath();
    }

    /**
     * Completes the pre-registration configuration, registers the object into the
     * target registry, and returns the matching
     * {@link PostRegistrable post-registration stage}.
     *
     * <p>This method is the terminal operation of the pre-registration stage.
     * In typical implementations, the returned post-registration stage may be the
     * same object as this pre-registration stage, but it should be used through
     * the {@code POST} interface for subsequent customization.</p>
     *
     * @return the matching post-registration stage
     * @see PostRegistrable
     */
    @Override
    public @NotNull VillagerProfessionPostRegistrable register() {
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable acquirableJobSite(Predicate<Holder<PoiType>> acquirableJobSite) {
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable requestedItems(ImmutableSet<Item> requiredItems) {
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable secondaryPoi(ImmutableSet<Block> secondaryPoi) {
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable workSound(@Nullable SoundEvent sound) {
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable jobSite(Predicate<Holder<PoiType>> heldJobSite) {
        return this;
    }
}
