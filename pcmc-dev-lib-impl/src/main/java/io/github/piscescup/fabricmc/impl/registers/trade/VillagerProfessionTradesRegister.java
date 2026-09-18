package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder;
import io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeMetadata;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPostRegistrable;
import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPreRegistrable;
import io.github.piscescup.fabricmc.impl.store.tag.MutableTagKeysHolder;
import io.github.piscescup.fabricmc.impl.store.village.trade.MutableVillagerTradeHolder;
import io.github.piscescup.fabricmc.store.villager.trade.TradeLevel;
import io.github.piscescup.util.validation.NullCheck;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerProfessionTradesRegister
    implements VillagerProfessionTradesPreRegistrable, VillagerProfessionTradesPostRegistrable
{
    private Identifier profession;
    private ResourceKey<VillagerProfession> profession;

    private final EnumMap<TradeLevel, LevelTradesBuilder> trades = new EnumMap<>(TradeLevel.class);

    @Override
    public Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel() {
        Int2ObjectAVLTreeMap<ResourceKey<TradeSet>> result = new Int2ObjectAVLTreeMap<>();

        for (TradeLevel level : trades.keySet()) {
            result.put(
                level.level(),
                ResourceKey.create(
                    Registries.TRADE_SET,
                    levelIdentifier(level)
                )
            );
        }

        return result;
    }

    @Override
    public VillagerProfessionTradesPreRegistrable level(TradeLevel level, Consumer<VillagerLevelTradeBuilder> action) {
        NullCheck.requireNonNull(action, "action");

        LevelTradesBuilder builder = new LevelTradesBuilder(profession.getNamespace(), profession, level);

        action.accept(builder);
        this.trades.put(level, builder);

        return this;
    }

    @Override
    public VillagerProfessionTradesPostRegistrable register() {

        return this;
    }


    private @NotNull Identifier levelIdentifier(
        TradeLevel level
    ) {
        return Identifier.fromNamespaceAndPath(
            profession.getNamespace(),
            profession.getPath() + "/level_" + level.level()
        );
    }
}
