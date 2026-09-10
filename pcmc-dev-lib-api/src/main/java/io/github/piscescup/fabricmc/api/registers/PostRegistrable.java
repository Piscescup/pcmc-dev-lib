package io.github.piscescup.fabricmc.api.registers;

import io.github.piscescup.fabricmc.constants.MCLanguage;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface PostRegistrable<V, T extends V, POST extends PostRegistrable<V, T, POST>> {

    @NotNull
    POST translate(@NotNull MCLanguage lang, @NotNull String translation);

    @NotNull
    T get();

    @NotNull
    Identifier identifier();

    @NotNull
    ResourceKey<V> resourceKey();
}
