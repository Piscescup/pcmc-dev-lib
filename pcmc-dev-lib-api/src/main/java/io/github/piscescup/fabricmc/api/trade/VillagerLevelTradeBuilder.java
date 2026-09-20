package io.github.piscescup.fabricmc.api.trade;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerLevelTradeBuilder {
    VillagerLevelTradeBuilder add(
        String path,
        VillagerTrade trade
    );

    VillagerLevelTradeBuilder include(
        ResourceKey<VillagerTrade> trade
    );

    VillagerLevelTradeBuilder includeTag(
        TagKey<VillagerTrade> tag
    );

    default VillagerLevelTradeBuilder amount(int amount) {
        return amount(ConstantValue.exactly(amount));
    }

    VillagerLevelTradeBuilder amount(
        NumberProvider amount
    );

    VillagerLevelTradeBuilder allowDuplicates(
        boolean allowDuplicates
    );

    VillagerLevelTradeBuilder randomSequence(
        Identifier randomSequence
    );

    @Contract("_, _, _, _, _, _ -> new")
    @NotNull
    static VillagerTrade trade(@NotNull ItemLike wantedItem, int wantedCount, @NotNull ItemLike givenItem, int givenCount, int maxUses, int villagerXp) {
        return new VillagerTrade(
            new TradeCost(wantedItem, wantedCount),
            new ItemStackTemplate(givenItem.asItem(), givenCount),
            maxUses,
            villagerXp,
            0.05F,
            Optional.empty(),
            List.of()
        );
    }

    static VillagerTrade trade(
        @NotNull ItemLike wantedItem, int wantedCount,
        @NotNull ItemLike additionWantItem, int additionWantCount,
        @NotNull ItemLike givenItem, int givenCount,
        int maxUses, int villagerXp
    ) {
        return new VillagerTrade(
            new TradeCost(wantedItem, wantedCount),
            Optional.of(new TradeCost(additionWantItem, additionWantCount)),
            new ItemStackTemplate(givenItem.asItem(), givenCount),
            maxUses,
            villagerXp,
            0.05F,
            Optional.empty(),
            List.of()
        );
    }

}
