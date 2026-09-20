package io.github.piscescup.fabricmc.datagen.trade;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class VillagerTradeRegistryGenerator
    extends FabricDynamicRegistryProvider
{

    private final VillagerTradeDatagenModel model;

    public VillagerTradeRegistryGenerator(
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
        HolderLookup.RegistryLookup<VillagerTrade> lookup =
            registries.lookupOrThrow(Registries.VILLAGER_TRADE);

        model.tradeKeys().forEach(key ->
                                      entries.add(lookup, key)
        );
    }

    @Override
    public @NotNull String getName() {
        return "Villager Trades";
    }
}