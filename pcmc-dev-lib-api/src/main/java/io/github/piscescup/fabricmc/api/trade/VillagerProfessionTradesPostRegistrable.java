package io.github.piscescup.fabricmc.api.trade;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerProfessionTradesPostRegistrable {

    Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel();
}
