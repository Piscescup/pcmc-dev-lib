package io.github.piscescup.fabricmc.impl.registers.villager;

import io.github.piscescup.fabricmc.api.villager.VillagerProfessionPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since
 */
public final class VillagerProfessionRegisterFactory
    extends RegisterFactoryImpl<VillagerProfession>
{

    private VillagerProfessionRegisterFactory(
        String namespace
    ) {
        super(Registries.VILLAGER_PROFESSION, namespace);
    }

    @Contract("_ -> new")
    @NotNull
    public static VillagerProfessionRegisterFactory ofNamespace(@NotNull String namespace) {
        return new VillagerProfessionRegisterFactory(namespace);
    }

    @Contract("_ -> new")
    @NotNull
    public VillagerProfessionPreRegistrable.HeldJobSite path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        Identifier id = Identifier.fromNamespaceAndPath(this.namespace, path);
        return new VillagerProfessionRegister(id);
    }
}
