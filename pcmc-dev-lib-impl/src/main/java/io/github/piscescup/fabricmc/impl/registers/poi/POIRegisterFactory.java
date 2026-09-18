package io.github.piscescup.fabricmc.impl.registers.poi;

import io.github.piscescup.fabricmc.api.poi.POIPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class POIRegisterFactory
    extends RegisterFactoryImpl<PoiType>
{
    private POIRegisterFactory(String namespace) {
        super(Registries.POINT_OF_INTEREST_TYPE, namespace);
    }

    @Contract("_ -> new")
    @NotNull
    public static POIRegisterFactory ofNamespace(@NotNull String namespace) {
        return new POIRegisterFactory(namespace);
    }

    public POIPreRegistrable.MatchingStates path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        return new POIRegister(Identifier.fromNamespaceAndPath(this.namespace, path));
    }
}
