package io.github.piscescup.fabricmc.store.recipe;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ReadableRecipeRegistrablesHolder {
    @NotNull Collection<RecipeRegistrable<?>> recipes();

    @NotNull Optional<RecipeRegistrable<?>> recipe(
        @NotNull ResourceKey<Recipe<?>> id
    );
}
