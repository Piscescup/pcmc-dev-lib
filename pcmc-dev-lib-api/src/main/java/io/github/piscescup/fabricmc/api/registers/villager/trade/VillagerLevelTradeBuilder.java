package io.github.piscescup.fabricmc.api.registers.villager.trade;

import net.minecraft.world.item.trading.VillagerTrade;

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

}
