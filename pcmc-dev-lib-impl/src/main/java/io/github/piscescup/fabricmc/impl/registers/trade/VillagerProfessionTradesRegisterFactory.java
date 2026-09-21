package io.github.piscescup.fabricmc.impl.registers.trade;

import io.github.piscescup.fabricmc.api.trade.VillagerProfessionTradesPreRegistrable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Creates reusable declarations of profession trade callbacks.
 *
 * <p>Declarations acquire their resource identifiers when attached to a
 * profession. Each {@link #create()} call starts with no configured levels.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionTradesRegister
 */
public final class VillagerProfessionTradesRegisterFactory {
    private VillagerProfessionTradesRegisterFactory() {}

    /**
     * Starts an independent declaration of career-level trade callbacks.
     *
     * @return a new, empty trade pre-registration stage
     */
    @Contract(" -> new")
    @NotNull
    public static VillagerProfessionTradesPreRegistrable create() {
        return new VillagerProfessionTradesRegister();
    }

}
