package fr.militario.spacex.jei.secondstage;

import java.util.ArrayList;
import java.util.List;

import fr.militario.spacex.SpaceXUtils;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

public class F9SecondStageRecipeMaker {
	public static List<F9SecondStageRecipeWrapper> getRecipesList() {
		List<F9SecondStageRecipeWrapper> recipes = new ArrayList<>();
		for (INasaWorkbenchRecipe recipe : SpaceXUtils.getSecondStageRecipe()) {
			F9SecondStageRecipeWrapper wrapper = new F9SecondStageRecipeWrapper(recipe);
			recipes.add(wrapper);
		}
		return recipes;
	}
}
