package io.github.piscescup.fabricmc.store;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArrowItem;

/**
 * Provides metadata describing a registered entry and its associated
 * registration information.
 *
 * <p>The registry type {@code R} represents the base type accepted by the
 * target registry, while {@code T} represents the concrete type of the
 * registered entry. For example, when registering a {@code CustomItem},
 * {@code R} is {@code Item} and {@code T} is {@code CustomItem}.</p>
 *
 * @param <R> the base type accepted by the target registry, such as {@code ResourceKey<Registry<Item>> ITEM}
 * @param <T> the concrete type of the registered entry, such as {@link ArrowItem}
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface RegistrationMetadata<R, T extends R> {

    /**
     * Returns the identifier assigned to the registered entry.
     *
     * @return the identifier of the entry
     */
    Identifier identifier();

    /**
     * Returns the registered entry.
     *
     * @return the registered entry
     */
    T entry();

    /**
     * Returns the resource key assigned to the entry in the target registry.
     *
     * <p>The key uses the registry's base entry type rather than the concrete
     * runtime type of the registered entry.</p>
     *
     * @return the resource key of the entry
     */
    ResourceKey<R> resourceKey();

}