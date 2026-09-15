package io.github.piscescup.fabricmc.impl.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.registers.recipe.crafting.ShapelessCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.impl.registers.RegisterFactoryImpl;
import io.github.piscescup.util.validation.NullCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class CraftingRecipeRegisterFactory
    extends RegisterFactoryImpl<Recipe<?>>
{

    /**
     * Creates a factory scope for a registry and namespace.
     *
     * <p>Both arguments are retained as-is. Identifier construction and namespace
     * syntax validation are performed when specialized factories create paths.</p>
     *
     * @param resourceKey the target {@link Registry} key; must not be {@code null}
     * @param namespace   the namespace for identifiers; must not be {@code null}
     * @throws NullPointerException if {@code resourceKey} or {@code namespace} is {@code null}
     */
    private CraftingRecipeRegisterFactory(String namespace) {
        super(Registries.RECIPE, namespace);
    }

    @Contract("_ -> new")
    public static @NotNull CraftingRecipeRegisterFactory ofNamespace(String namespace) {
        NullCheck.requireNonNull(namespace, "namespace");
        return new CraftingRecipeRegisterFactory(namespace);
    }

    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        @NotNull RecipeCategory category,
        @NotNull ItemStackTemplate result
    ) {
        ResourceKey<Recipe<?>> key = ResourceKey.create(
            Registries.RECIPE,
            Identifier.fromNamespaceAndPath(this.namespace, path)
        );
        return new ShapelessCraftingRecipeRegister(key, category, result);
    }

    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemLike result, int count
    ) {
        ItemStackTemplate target = new ItemStackTemplate(result.asItem(), count);

        return shapeless(path, category, target);
    }

    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemLike result
    ) {
        return shapeless(path, category, result, 1);
    }

    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemStack result, int count
    ) {
        ItemStackTemplate target = new ItemStackTemplate(result.getItem(), count);

        return shapeless(path, category, target);
    }

    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemStack result
    ) {
        return shapeless(path, category, result, 1);
    }

}
