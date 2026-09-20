package io.github.piscescup.fabricmc.impl.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.recipe.crafting.ShapedCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.api.recipe.crafting.ShapelessCraftingRecipeRegistrable;
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
 * Creates shaped and shapeless crafting-recipe definitions in one namespace.
 *
 * <p>Each factory method combines the configured namespace with a recipe path
 * and returns a new mutable definition. Configure its ingredients, patterns,
 * unlock criteria, and group before calling {@code register()} or adding it to
 * a caller-managed collection.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * CraftingRecipeRegisterFactory recipes =
 *     CraftingRecipeRegisterFactory.ofNamespace("example");
 *
 * recipes.shapeless("mixed_wool", RecipeCategory.MISC, result)
 *     .requires(Items.STRING)
 *     .requires(ItemTags.WOOL)
 *     .unlockedBy("has_string", Items.STRING)
 *     .register();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see ShapedCraftingRecipeRegistrable
 * @see ShapelessCraftingRecipeRegistrable
 */
public final class CraftingRecipeRegisterFactory
    extends RegisterFactoryImpl<Recipe<?>>
{

    /**
     * Creates a factory targeting {@link Registries#RECIPE} in one namespace.
     *
     * @param namespace the namespace used for recipe identifiers
     */
    private CraftingRecipeRegisterFactory(String namespace) {
        super(Registries.RECIPE, namespace);
    }

    /**
     * Creates a new crafting-recipe factory for a namespace.
     *
     * @param namespace the namespace, usually a mod ID; must not be {@code null}
     * @return a new recipe factory scoped to the namespace
     * @throws NullPointerException if {@code namespace} is {@code null}
     */
    @Contract("_ -> new")
    public static @NotNull CraftingRecipeRegisterFactory ofNamespace(String namespace) {
        NullCheck.requireNonNull(namespace, "namespace");
        return new CraftingRecipeRegisterFactory(namespace);
    }

    /**
     * Starts a shapeless recipe with an explicit result template.
     *
     * <p>The template is retained so its item, count, and data components can be
     * forwarded to Minecraft's shapeless recipe builder during generation.</p>
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the generated result template
     * @return a new shapeless-recipe definition
     * @throws NullPointerException if {@code path}, {@code category}, or {@code result} is {@code null}
     */
    @NotNull
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

    /**
     * Starts a shapeless recipe producing a count of an item-like result.
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the result item or block
     * @param count    the generated result count
     * @return a new shapeless-recipe definition
     * @see #shapeless(String, RecipeCategory, ItemStackTemplate)
     */
    @NotNull
    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemLike result, int count
    ) {
        ItemStackTemplate target = new ItemStackTemplate(result.asItem(), count);

        return shapeless(path, category, target);
    }

    /**
     * Starts a shapeless recipe producing one of an item-like result.
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the result item or block
     * @return a new shapeless-recipe definition
     * @see #shapeless(String, RecipeCategory, ItemLike, int)
     */
    @NotNull
    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemLike result
    ) {
        return shapeless(path, category, result, 1);
    }

    /**
     * Starts a shapeless recipe using an {@link ItemStack}'s item as the result.
     *
     * <p>The stack's existing count and data components are not copied; the
     * supplied {@code count} is used to create a new result template.</p>
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the stack whose item becomes the result item
     * @param count    the generated result count
     * @return a new shapeless-recipe definition
     * @see #shapeless(String, RecipeCategory, ItemStackTemplate)
     */
    @NotNull
    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemStack result, int count
    ) {
        ItemStackTemplate target = new ItemStackTemplate(result.getItem(), count);

        return shapeless(path, category, target);
    }

    /**
     * Starts a shapeless recipe producing one item from the supplied stack.
     *
     * <p>The stack's count and data components are not copied.</p>
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the stack whose item becomes the result item
     * @return a new shapeless-recipe definition
     * @see #shapeless(String, RecipeCategory, ItemStack, int)
     */
    @NotNull
    public ShapelessCraftingRecipeRegistrable shapeless(
        @NotNull String path,
        RecipeCategory category,
        @NotNull ItemStack result
    ) {
        return shapeless(path, category, result, 1);
    }

    /**
     * Starts a shaped recipe producing a count of an item-like result.
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the result item or block
     * @param count    the positive generated result count
     * @return a new shaped-recipe definition
     * @throws IllegalArgumentException if {@code count} is not positive
     */
    @NotNull
    public ShapedCraftingRecipeRegistrable shaped(
        @NotNull String path,
        @NotNull RecipeCategory category,
        @NotNull ItemLike result, int count
    ) {
        ResourceKey<Recipe<?>> key = ResourceKey.create(
            Registries.RECIPE,
            Identifier.fromNamespaceAndPath(this.namespace, path)
        );
        return new ShapedCraftingRecipeRegister(key, category, result, count);
    }

    /**
     * Starts a shaped recipe producing one of an item-like result.
     *
     * @param path     the recipe path without a namespace prefix
     * @param category the recipe-book category
     * @param result   the result item or block
     * @return a new shaped-recipe definition
     * @see #shaped(String, RecipeCategory, ItemLike, int)
     */
    @NotNull
    public ShapedCraftingRecipeRegistrable shaped(
        @NotNull String path,
        @NotNull RecipeCategory category,
        @NotNull ItemLike result
    ) {
        ResourceKey<Recipe<?>> key = ResourceKey.create(
            Registries.RECIPE,
            Identifier.fromNamespaceAndPath(this.namespace, path)
        );
        return shaped(path, category, result, 1);
    }

}
