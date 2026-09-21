package io.github.piscescup.fabricmc.impl.registers.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPostRegistrable;
import io.github.piscescup.fabricmc.api.villager.VillagerProfessionPostRegistrable;
import io.github.piscescup.fabricmc.api.villager.VillagerProfessionPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.Register;
import io.github.piscescup.fabricmc.impl.registers.trade.LevelTradesBuilder;
import io.github.piscescup.fabricmc.impl.store.village.trade.MutableVillagerTradeHolder;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implements the configuration and registration stages for a villager profession.
 *
 * <p>Created through {@link VillagerProfessionRegisterFactory}. Job-site
 * predicates, item and block sets, work sound, and trade-set keys are passed to
 * the profession constructor during {@link #register()}. Trade callbacks are
 * evaluated earlier, by {@link #tradeSetsByLevel(VillagerProfessionTradesPostRegistrable)}.</p>
 *
 * <p>Registration also places the level metadata in
 * {@link MutableVillagerTradeHolder#INSTANCE} for data generation. Complete
 * configuration before registering this stateful builder once.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionPreRegistrable
 * @see VillagerProfessionPostRegistrable
 */
public final class VillagerProfessionRegister
    extends Register<VillagerProfession, VillagerProfession, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable>
    implements VillagerProfessionPreRegistrable.HeldJobSite, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable
{
    /** Required predicate selected through the factory's first stage. */
    private Predicate<Holder<PoiType>> heldJobSite;
    /** Acquisition predicate, initially assigned together with the held-site predicate. */
    private Predicate<Holder<PoiType>> acquirableJobSite;
    /** Requested items; defaults to an empty immutable set. */
    private ImmutableSet<Item> requestedItems = ImmutableSet.of();
    /** Secondary POI blocks; defaults to an empty immutable set. */
    private ImmutableSet<Block> secondaryPoi = ImmutableSet.of();
    /** Optional work sound; initially unset. */
    private @Nullable SoundEvent workSound;
    /** Generated trade-set keys indexed by numeric career level. */
    private Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel = new Int2ObjectOpenHashMap<>();
    /** Metadata built for configured levels and shared with the holder at registration. */
    private EnumMap<TradeLevel, VillagerLevelTradeMetadata> levelTrades = new EnumMap<>(TradeLevel.class);

    /**
     * Creates a profession builder with empty trade and item configuration.
     *
     * @param id the profession identifier
     */
    VillagerProfessionRegister(Identifier id) {
        super(Registries.VILLAGER_PROFESSION, id);
    }

    /**
     * Returns the translation key derived from this profession's identifier.
     *
     * @return {@code entity.<namespace>.villager.<path>}
     */
    @Override
    protected String translateKey() {
        return "entity." + id.getNamespace() + ".villager." + id.getPath();
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionPreRegistrable heldJobSite(Predicate<Holder<PoiType>> heldJobSite) {
        NullCheck.requireNonNull(heldJobSite, "heldJobSite");

        this.heldJobSite = heldJobSite;
        this.acquirableJobSite = heldJobSite;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionPreRegistrable acquirableJobSite(Predicate<Holder<PoiType>> acquirableJobSite) {
        NullCheck.requireNonNull(acquirableJobSite, "acquirableJobSite");
        this.acquirableJobSite = acquirableJobSite;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionPreRegistrable requestedItems(ImmutableSet<Item> requestedItems) {
        NullCheck.requireAllNonNull(requestedItems);
        this.requestedItems = requestedItems;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionPreRegistrable secondaryPoi(ImmutableSet<Block> secondaryPoi) {
        NullCheck.requireAllNonNull(secondaryPoi);
        this.secondaryPoi = secondaryPoi;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionPreRegistrable workSound(@Nullable SoundEvent workSound) {
        this.workSound = workSound;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public VillagerProfessionPreRegistrable tradeSetsByLevel(VillagerProfessionTradesPostRegistrable tradesPostRegistrable) {
        EnumMap<TradeLevel, Consumer<VillagerLevelTradeBuilder>> trades = tradesPostRegistrable.trades();

        trades.forEach((tradeLevel, builderConsumer) -> {
            LevelTradesBuilder builder = new LevelTradesBuilder(tradeLevel);
            builderConsumer.accept(builder);

            VillagerLevelTradeMetadata metadata =
                builder.buildBy(this.id);

            levelTrades.put(tradeLevel, metadata);
            tradeSetsByLevel.put(
                tradeLevel.level(),
                metadata.tradeSetKey()
            );
        });

        return this;
    }

    /**
     * Constructs the profession, registers it, and collects its trade metadata.
     *
     * <p>The profession is added to {@link BuiltInRegistries#VILLAGER_PROFESSION}
     * before the level metadata map is passed to
     * {@link MutableVillagerTradeHolder#addAll(ResourceKey, EnumMap)}. Trade
     * entries, tags, and trade sets are emitted separately by data generation.
     * Repeated registration calls are not guarded.</p>
     *
     * @return this instance as the profession post-registration stage
     */
    @Override
    public @NonNull VillagerProfessionPostRegistrable register() {
        this.thingToBeRegistered = Registry.register(
            BuiltInRegistries.VILLAGER_PROFESSION,
            this.id,
            new VillagerProfession(
                Component.translatable(translateKey()),
                heldJobSite,
                acquirableJobSite,
                requestedItems,
                secondaryPoi,
                workSound,
                tradeSetsByLevel
            )
        );

        MutableVillagerTradeHolder.INSTANCE.addAll(this.resourceKey, this.levelTrades);

        return this;
    }

}
