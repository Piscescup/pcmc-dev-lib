package io.github.piscescup.fabricmc.testvillager;

import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPostRegistrable;
import io.github.piscescup.fabricmc.impl.registers.trade.VillagerProfessionTradesRegisterFactory;
import net.minecraft.world.item.Items;

import static io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder.trade;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TestTrades {
    public static final VillagerProfessionTradesPostRegistrable TEST_TRADES = VillagerProfessionTradesRegisterFactory.create()
        .novice(level -> level
            .add(
                "coal_for_emerald", trade(Items.COAL, 16, Items.EMERALD, 1, 16, 2)
            )
            .add(
                "emerald_for_apples", trade(Items.EMERALD, 1, Items.APPLE, 4, 12, 1)
            )
            .amount(2)
            .allowDuplicates(false)
        )
        .apprentice(level -> level
            .add(
                "emerald_for_iron_pickaxe", trade(Items.EMERALD, 3, Items.IRON_PICKAXE, 1, 8, 10)
            )
            .amount(1)
        )
        .master(level -> level
            .add(
                "emerald_for_diamond", trade(Items.EMERALD, 20, Items.DIAMOND, 1, 4, 30)
            )
            .amount(1)
        )
        .register();
}
