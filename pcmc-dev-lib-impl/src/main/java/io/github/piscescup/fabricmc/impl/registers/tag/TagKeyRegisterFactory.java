package io.github.piscescup.fabricmc.impl.registers.tag;

import io.github.piscescup.fabricmc.api.registers.tag.TagKeyPostRegistrable;
import io.github.piscescup.fabricmc.api.registers.tag.TagKeyPreRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Creates {@link TagKey} declaration builders for one {@link Registry} and namespace.
 *
 * <p>Use a registry-specific convenience method or
 * {@link #ofNamespace(String, ResourceKey)} to create a factory, then select
 * a tag path through {@link #path(String)}. Each stage collects members for
 * data generation and exposes its tag key after {@code register()}.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * TagKeyRegisterFactory<Item> ITEM_TAGS =
 *     TagKeyRegisterFactory.itemsTagOfNamespace("example");
 * TagKey<Item> materials = ITEM_TAGS.path("materials")
 *     .addEntries(Items.IRON_INGOT, Items.GOLD_INGOT)
 *     .register()
 *     .get();
 * }</pre>
 *
 * <p>{@link #ofVanilla(TagKey)} starts a declaration using an existing tag's
 * registry and identifier, which is useful for adding members to vanilla tags.
 * It also accepts tags from other namespaces.</p>
 *
 * <p>Typical usage when extending an existing vanilla tag:</p>
 * <pre>{@code
 * TagKeyRegisterFactory.ofVanilla(BlockTags.NEEDS_IRON_TOOL)
 *     .addEntries(MY_BLOCK)
 *     .register();
 * }</pre>
 *
 * @param <T> the value type of the {@link Registry} targeted by this factory's tags
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see TagKeyRegister
 * @see TagKeyPreRegistrable
 * @see TagKeyPostRegistrable
 */
public final class TagKeyRegisterFactory<T>
    extends RegisterFactoryImpl<T>
{

    /**
     * Creates a factory scope for {@link TagKey} declarations.
     *
     * @param resourceKey the target {@link Registry} key; must not be {@code null}
     * @param namespace   the namespace for {@link TagKey} identifiers; must not be {@code null}
     * @throws NullPointerException if {@code resourceKey} or {@code namespace} is {@code null}
     */
    TagKeyRegisterFactory(ResourceKey<? extends Registry<T>> resourceKey, String namespace) {
        super(resourceKey, namespace);
    }

    /**
     * Creates a {@link TagKey} factory using the key of an existing {@link Registry}.
     *
     * <p>Obtains the key through {@link Registry#key()} and delegates to
     * {@link #ofNamespace(String, ResourceKey)}.</p>
     *
     * @param namespace the namespace for {@link TagKey} identifiers; must not be {@code null}
     * @param registry  the registry whose values the tags may contain; must not be {@code null}
     * @param <T>       the {@link Registry} value type
     * @return a new factory for the registry's key and selected namespace
     * @throws NullPointerException if {@code namespace} or {@code registry} is {@code null}
     * @deprecated Use {@link #ofNamespace(String, ResourceKey)} to describe
     *             the target registry without requiring a registry instance.
     */
    @Contract("_, _ -> new")
    @NotNull
    @Deprecated
    public static <T> TagKeyRegisterFactory<T> ofNamespace(
        @NotNull String namespace,
        @NotNull Registry<T> registry
    ) {
        return ofNamespace(namespace, registry.key());
    }

    /**
     * Creates a {@link TagKey} factory for a {@link Registry} key and namespace.
     *
     * <p>The registry key and namespace are retained directly. A concrete
     * {@link Identifier} is created when {@link #path(String)} is called.</p>
     *
     * @param namespace the namespace for {@link TagKey} identifiers; must not be {@code null}
     * @param registry  the target {@link Registry} key; must not be {@code null}
     * @param <T>       the {@link Registry} value type
     * @return a new factory scoped to the registry key and namespace
     * @throws NullPointerException if {@code namespace} or {@code registry} is {@code null}
     */
    @Contract("_, _ -> new")
    @NotNull
    public static <T> TagKeyRegisterFactory<T> ofNamespace(
        @NotNull String namespace,
        @NotNull ResourceKey<? extends Registry<T>> registry
    ) {
        NullCheck.requireNonNull(namespace, "namespace");
        NullCheck.requireNonNull(registry, "registry");
        return new TagKeyRegisterFactory<>(registry, namespace);
    }

    /**
     * Starts a declaration using an existing {@link TagKey}'s registry and identifier.
     *
     * <p>The tag need not use the vanilla namespace. Added members are collected
     * under its existing identity for subsequent tag data generation.</p>
     *
     * @param tagKey the existing {@link TagKey} receiving additional members; must not be {@code null}
     * @param <T>    the tag's {@link Registry} value type
     * @return a new pre-registration stage targeting the existing tag
     * @throws NullPointerException if {@code tagKey} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static <T> TagKeyPreRegistrable<T> ofVanilla(TagKey<T> tagKey) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        return new TagKeyRegister<>(tagKey.registry(), tagKey.location());
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#ITEM}.
     *
     * @param namespace the namespace for {@link Item} tags; must not be {@code null}
     * @return a new factory for {@link Item} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Item> itemsTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.ITEM);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#BLOCK}.
     *
     * @param namespace the namespace for {@link Block} tags; must not be {@code null}
     * @return a new factory for {@link Block} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Block> blocksTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.BLOCK);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#ENTITY_TYPE}.
     *
     * @param namespace the namespace for {@link EntityType} tags; must not be {@code null}
     * @return a new factory for {@link EntityType} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<EntityType<?>> entitiesTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.ENTITY_TYPE);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#POINT_OF_INTEREST_TYPE}.
     *
     * @param namespace the namespace for {@link PoiType} tags; must not be {@code null}
     * @return a new factory for {@link PoiType} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<PoiType> poiTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.POINT_OF_INTEREST_TYPE);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#DAMAGE_TYPE}.
     *
     * @param namespace the namespace for {@link DamageType} tags; must not be {@code null}
     * @return a new factory for {@link DamageType} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<DamageType> damageTypesTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.DAMAGE_TYPE);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#DIALOG}.
     *
     * @param namespace the namespace for {@link Dialog} tags; must not be {@code null}
     * @return a new factory for {@link Dialog} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Dialog> dialogTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.DIALOG);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#ENCHANTMENT}.
     *
     * @param namespace the namespace for {@link Enchantment} tags; must not be {@code null}
     * @return a new factory for {@link Enchantment} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Enchantment> enchantmentTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.ENCHANTMENT);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#FLUID}.
     *
     * @param namespace the namespace for {@link Fluid} tags; must not be {@code null}
     * @return a new factory for {@link Fluid} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Fluid> fluidTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.FLUID);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#GAME_EVENT}.
     *
     * @param namespace the namespace for {@link GameEvent} tags; must not be {@code null}
     * @return a new factory for {@link GameEvent} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<GameEvent> gameEventTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.GAME_EVENT);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#INSTRUMENT}.
     *
     * @param namespace the namespace for {@link Instrument} tags; must not be {@code null}
     * @return a new factory for {@link Instrument} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Instrument> instrumentTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.INSTRUMENT);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#STRUCTURE}.
     *
     * @param namespace the namespace for {@link Structure} tags; must not be {@code null}
     * @return a new factory for {@link Structure} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Structure> structureTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.STRUCTURE);
    }

    /**
     * Creates a {@link TagKey} factory targeting {@link Registries#VILLAGER_TRADE}.
     *
     * @param namespace the namespace for {@link VillagerTrade} tags; must not be {@code null}
     * @return a new factory for {@link VillagerTrade} tags
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<VillagerTrade> villagerTradeTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.VILLAGER_TRADE);
    }

    /**
     * Starts a {@link TagKey} declaration using this factory's registry and namespace.
     *
     * <p>The path is combined with this factory's namespace to construct the
     * {@link Identifier} supplied to a new {@link TagKeyRegister}. Each call
     * creates a new declaration stage, even when the same path is reused.</p>
     *
     * @param path the tag path without a namespace prefix; must not be {@code null}
     * @return a new pre-registration stage for collecting tag members
     * @throws NullPointerException if {@code path} is {@code null}
     */
    @Contract("_ -> new")
    @NotNull
    public TagKeyPreRegistrable<T> path(String path) {
        NullCheck.requireNonNull(path, "path");

        Identifier identifier = Identifier.fromNamespaceAndPath(namespace, path);
        return new TagKeyRegister<>(this.resourceKey, identifier);
    }

}
