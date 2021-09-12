package fr.militario.spacex.jei.dragon;

import java.util.ArrayList;
import java.util.List;

import fr.militario.spacex.SpaceXUtils;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

public class DragonRecipeMaker {
	public static List<DragonRecipeWrapper> getRecipesList() {
		List<DragonRecipeWrapper> recipes = new ArrayList<>();
		for (INasaWorkbenchRecipe recipe : SpaceXUtils.getDragonRecipe()) {
			DragonRecipeWrapper wrapper = new DragonRecipeWrapper(recipe);
			recipes.add(wrapper);
		}
		return recipes;
	}
}
