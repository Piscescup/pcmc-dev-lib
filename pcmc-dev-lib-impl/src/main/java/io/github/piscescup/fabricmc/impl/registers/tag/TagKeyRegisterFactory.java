package io.github.piscescup.fabricmc.impl.registers.tag;

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
 *
 * @author REN YuanTong
 * @since
 */
public final class TagKeyRegisterFactory<T>
    extends RegisterFactoryImpl<T>
{

    TagKeyRegisterFactory(ResourceKey<? extends Registry<T>> resourceKey, String namespace) {
        super(resourceKey, namespace);
    }

    @Contract("_, _ -> new")
    @NotNull
    @Deprecated
    public static <T> TagKeyRegisterFactory<T> ofNamespace(
        @NotNull String namespace,
        @NotNull Registry<T> registry
    ) {
        return ofNamespace(namespace, registry.key());
    }

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

    @Contract("_ -> new")
    @NotNull
    public static <T> TagKeyPreRegistrable<T> ofVanilla(TagKey<T> tagKey) {
        NullCheck.requireNonNull(tagKey, "tagKey");
        return new TagKeyRegister<>(tagKey.registry(), tagKey.location());
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Item> itemsTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.ITEM);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Block> blocksTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.BLOCK);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<EntityType<?>> entitiesTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.ENTITY_TYPE);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<PoiType> poiTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.POINT_OF_INTEREST_TYPE);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<DamageType> damageTypesTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.DAMAGE_TYPE);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Dialog> dialogTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.DIALOG);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Enchantment> enchantmentTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.ENCHANTMENT);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Fluid> fluidTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.FLUID);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<GameEvent> gameEventTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.GAME_EVENT);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Instrument> instrumentTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.INSTRUMENT);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<Structure> structureTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.STRUCTURE);
    }

    @Contract("_ -> new")
    @NotNull
    public static TagKeyRegisterFactory<VillagerTrade> villagerTradeTagOfNamespace(@NotNull String namespace) {
        return ofNamespace(namespace, Registries.VILLAGER_TRADE);
    }

    @Contract("_ -> new")
    @NotNull
    public TagKeyPreRegistrable<T> path(String path) {
        NullCheck.requireNonNull(path, "path");

        Identifier identifier = Identifier.fromNamespaceAndPath(namespace, path);
        return new TagKeyRegister<>(this.resourceKey, identifier);
    }

}
