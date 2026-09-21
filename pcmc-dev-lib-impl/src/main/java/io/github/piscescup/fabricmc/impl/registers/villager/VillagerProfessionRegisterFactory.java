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
 * Creates {@link VillagerProfession} registration builders within one namespace.
 *
 * <p>Each {@link #path(String)} call creates an independent builder starting
 * with the required held-job-site stage. Profession registration occurs only
 * when the configured builder's {@code register()} method is called.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * VillagerProfessionRegisterFactory PROFESSIONS =
 *     VillagerProfessionRegisterFactory.ofNamespace("example");
 * VillagerProfession trader = PROFESSIONS.path("trader")
 *     .heldJobSite(TRADER_POI)
 *     .tradeSetsByLevel(TRADER_TRADES)
 *     .register()
 *     .translate(MCLanguage.EN_US, "Trader")
 *     .get();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see VillagerProfessionRegister
 * @see VillagerProfessionPreRegistrable
 */
public final class VillagerProfessionRegisterFactory
    extends RegisterFactoryImpl<VillagerProfession>
{

    /**
     * Creates a factory targeting {@link Registries#VILLAGER_PROFESSION}.
     *
     * @param namespace the namespace for profession identifiers
     */
    private VillagerProfessionRegisterFactory(
        String namespace
    ) {
        super(Registries.VILLAGER_PROFESSION, namespace);
    }

    /**
     * Creates a new profession factory for the given namespace.
     *
     * @param namespace the namespace, usually a mod ID; must not be {@code null}
     * @return a new factory scoped to the profession registry and namespace
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static VillagerProfessionRegisterFactory ofNamespace(@NotNull String namespace) {
        return new VillagerProfessionRegisterFactory(namespace);
    }

    /**
     * Starts a profession registration with the required job-site selection.
     *
     * @param path the profession path without a namespace prefix; must not be {@code null}
     * @return a fresh held-job-site stage using this namespace and path
     * @throws NullPointerException if {@code path} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public VillagerProfessionPreRegistrable.HeldJobSite path(@NotNull String path) {
        NullCheck.requireNonNull(path, "path");
        Identifier id = Identifier.fromNamespaceAndPath(this.namespace, path);
        return new VillagerProfessionRegister(id);
    }
}
