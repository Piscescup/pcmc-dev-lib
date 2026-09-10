package io.github.piscescup.fabricmc.api.registers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface RegisterFactory<R> {
    @NotNull
    ResourceKey<? extends Registry<R>> registryKey();

    @NotNull
    String namespace();

}
