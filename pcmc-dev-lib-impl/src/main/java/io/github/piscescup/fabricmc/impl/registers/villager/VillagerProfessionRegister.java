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
 *
 * @author REN YuanTong
 * @since
 */
public final class VillagerProfessionRegister
    extends Register<VillagerProfession, VillagerProfession, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable>
    implements VillagerProfessionPreRegistrable.HeldJobSite, VillagerProfessionPreRegistrable, VillagerProfessionPostRegistrable
{
    private Predicate<Holder<PoiType>> heldJobSite;
    private Predicate<Holder<PoiType>> acquirableJobSite;
    private ImmutableSet<Item> requestedItems = ImmutableSet.of();
    private ImmutableSet<Block> secondaryPoi = ImmutableSet.of();
    private @Nullable SoundEvent workSound;
    private Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel = new Int2ObjectOpenHashMap<>();
    private EnumMap<TradeLevel, VillagerLevelTradeMetadata> levelTrades = new EnumMap<>(TradeLevel.class);

    VillagerProfessionRegister(Identifier id) {
        super(Registries.VILLAGER_PROFESSION, id);
    }

    @Override
    protected String translateKey() {
        return "entity." + id.getNamespace() + ".villager." + id.getPath();
    }

    @Override
    public VillagerProfessionPreRegistrable heldJobSite(Predicate<Holder<PoiType>> heldJobSite) {
        NullCheck.requireNonNull(heldJobSite, "heldJobSite");

        this.heldJobSite = heldJobSite;
        this.acquirableJobSite = heldJobSite;
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable acquirableJobSite(Predicate<Holder<PoiType>> acquirableJobSite) {
        NullCheck.requireNonNull(acquirableJobSite, "acquirableJobSite");
        this.acquirableJobSite = acquirableJobSite;
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable requestedItems(ImmutableSet<Item> requestedItems) {
        NullCheck.requireAllNonNull(requestedItems);
        this.requestedItems = requestedItems;
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable secondaryPoi(ImmutableSet<Block> secondaryPoi) {
        NullCheck.requireAllNonNull(secondaryPoi);
        this.secondaryPoi = secondaryPoi;
        return this;
    }

    @Override
    public VillagerProfessionPreRegistrable workSound(@Nullable SoundEvent workSound) {
        this.workSound = workSound;
        return this;
    }

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
