package io.github.piscescup.fabricmc.api.registers.recipe.crafting;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;

/**
 *
 * @author REN YuanTong
 * @since
 */
public interface CraftingRecipeRegistrable<SUB_C extends CraftingRecipeRegistrable<SUB_C>>
    extends RecipeRegistrable<SUB_C>
{

}
