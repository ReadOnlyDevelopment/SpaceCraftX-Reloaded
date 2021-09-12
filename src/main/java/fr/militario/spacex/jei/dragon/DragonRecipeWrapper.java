package fr.militario.spacex.jei.dragon;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;

public class DragonRecipeWrapper extends BlankRecipeWrapper implements IRecipeWrapper {
	@Nonnull
	private final INasaWorkbenchRecipe recipe;

	public DragonRecipeWrapper(@Nonnull INasaWorkbenchRecipe recipe) {
		this.recipe = recipe;
	}

	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Lists.newArrayList(this.recipe.getRecipeInput().values()));
		ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
	}
}
