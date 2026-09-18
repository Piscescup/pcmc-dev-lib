package io.github.piscescup.fabricmc.api.trade;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

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

    VillagerLevelTradeBuilder add(
        Identifier id,
        VillagerTrade trade
    );

    VillagerLevelTradeBuilder add(
        ResourceKey<VillagerTrade> key,
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

}
