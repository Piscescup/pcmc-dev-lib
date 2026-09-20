package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPreRegistrable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerProfessionTradesRegisterFactory {
    private VillagerProfessionTradesRegisterFactory() {}

    @Contract(" -> new")
    @NotNull
    public static VillagerProfessionTradesPreRegistrable create() {
        return new VillagerProfessionTradesRegister();
    }

}
