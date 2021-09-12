package fr.militario.spacex.jei.falcon9;

import java.util.ArrayList;
import java.util.List;

import fr.militario.spacex.SpaceXUtils;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

public class Falcon9RecipeMaker {
	public static List<Falcon9RecipeWrapper> getRecipesList() {
		List<Falcon9RecipeWrapper> recipes = new ArrayList<>();
		for (INasaWorkbenchRecipe recipe : SpaceXUtils.getFalcon9RocketRecipe()) {
			Falcon9RecipeWrapper wrapper = new Falcon9RecipeWrapper(recipe);
			recipes.add(wrapper);
		}
		return recipes;
	}
}
