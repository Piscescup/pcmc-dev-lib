package io.github.piscescup.fabricmc.impl.registers.recipe;

import io.github.piscescup.fabricmc.api.registers.recipe.RecipeRegistrable;
import io.github.piscescup.fabricmc.impl.store.MutableReciperegistrablesHolder;
import org.jspecify.annotations.Nullable;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public abstract class RecipeRegister<SUB_RR extends RecipeRegistrable<SUB_RR>>
    implements RecipeRegistrable<SUB_RR>
{
    private @Nullable String group;
    @Override
    public void register() {
        MutableReciperegistrablesHolder.INSTANCE.add(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SUB_RR group(@Nullable String group) {
        this.group = group;
        return (SUB_RR) this;
    }
}
