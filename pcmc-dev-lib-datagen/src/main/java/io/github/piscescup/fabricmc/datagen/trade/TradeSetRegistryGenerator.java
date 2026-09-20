package io.github.piscescup.fabricmc.datagen.trade;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.trading.TradeSet;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class TradeSetRegistryGenerator
    extends FabricDynamicRegistryProvider
{

    private final VillagerTradeDatagenModel model;

    public TradeSetRegistryGenerator(
        FabricPackOutput output,
        CompletableFuture<HolderLookup.Provider> registriesFuture,
        VillagerTradeDatagenModel model
    ) {
        super(output, registriesFuture);
        this.model = model;
    }

    @Override
    protected void configure(
        HolderLookup.Provider registries,
        Entries entries
    ) {
        HolderLookup.RegistryLookup<TradeSet> lookup =
            registries.lookupOrThrow(Registries.TRADE_SET);

        model.tradeSetKeys().forEach(key ->
                                         entries.add(lookup, key)
        );
    }

    @Override
    public String getName() {
        return "Villager Trade Sets";
    }
}
