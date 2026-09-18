package io.github.piscescup.fabricmc.testrecipes;

import io.github.piscescup.fabricmc.api.recipe.RecipeRegistrable;
import io.github.piscescup.fabricmc.api.recipe.crafting.ShapelessCraftingRecipeRegistrable;
import io.github.piscescup.fabricmc.impl.registers.recipe.crafting.CraftingRecipeRegisterFactory;
import io.github.piscescup.fabricmc.testitems.TestItems;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collection;

import static io.github.piscescup.fabricmc.Refs.MOD_ID;

/**
 *
 * @author REN YuanTong
 * @since
 */
public final class TestCraftingRecipes {
    private static final Collection<RecipeRegistrable<?>> RECIPES =
        new ArrayList<>();

    private static final CraftingRecipeRegisterFactory FACTORY =
        CraftingRecipeRegisterFactory.ofNamespace(MOD_ID);

    public static final ShapelessCraftingRecipeRegistrable TEST_ITEM_1_RECIPE = FACTORY
        .shapeless("test_item1", RecipeCategory.MISC, TestItems.ITEM1)
        .requires(Items.DIAMOND)
        .requires(ItemTags.WOOL)
        .unlockedBy("test_item1", Items.DIAMOND)
        .collectsTo(RECIPES);

    public static void registerRecipes() {
        RECIPES.forEach(RecipeRegistrable::register);
    }
}
