package io.github.piscescup.fabricmc.datagen.trade;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Emits the {@link VillagerTrade} entries declared by a trade data-generation model.
 *
 * <p>The registry lookup must include the model's bootstrapped trades. Only
 * keys from {@link VillagerTradeDatagenModel#tradeKeys()} are emitted; external
 * trades referenced by a level are supplied by their existing resources.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerTradeRegistryGenerator
    extends FabricDynamicRegistryProvider
{

    /** Model selecting which bootstrapped trade entries are written. */
    private final VillagerTradeDatagenModel model;

    /**
     * Creates a provider for the model's declared trades.
     *
     * @param output the Fabric pack output receiving generated files
     * @param registriesFuture the future supplying the bootstrapped registry lookup
     * @param model the model whose trade keys should be emitted
     */
    public VillagerTradeRegistryGenerator(
        FabricPackOutput output,
        CompletableFuture<HolderLookup.Provider> registriesFuture,
        VillagerTradeDatagenModel model
    ) {
        super(output, registriesFuture);
        this.model = model;
    }

    /**
     * Selects the model's trade entries from the generated registry lookup.
     *
     * @param registries the registry lookup containing the bootstrapped trades
     * @param entries the output collection receiving the selected entries
     */
    @Override
    protected void configure(
        HolderLookup.Provider registries,
        Entries entries
    ) {
        HolderLookup.RegistryLookup<VillagerTrade> lookup =
            registries.lookupOrThrow(Registries.VILLAGER_TRADE);

        model.tradeKeys().forEach(key ->
                                      entries.add(lookup, key)
        );
    }

    /**
     * Returns the provider name used in data-generator diagnostics.
     *
     * @return {@code Villager Trades}
     */
    @Override
    public @NotNull String getName() {
        return "Villager Trades";
    }
}