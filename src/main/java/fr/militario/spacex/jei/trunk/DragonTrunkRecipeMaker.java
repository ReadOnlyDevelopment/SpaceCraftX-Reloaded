package fr.militario.spacex.jei.trunk;

import java.util.ArrayList;
import java.util.List;

import fr.militario.spacex.SpaceXUtils;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

public class DragonTrunkRecipeMaker {
	public static List<DragonTrunkRecipeWrapper> getRecipesList() {
		List<DragonTrunkRecipeWrapper> recipes = new ArrayList<>();
		for (INasaWorkbenchRecipe recipe : SpaceXUtils.getDragonTrunkRecipe()) {
			DragonTrunkRecipeWrapper wrapper = new DragonTrunkRecipeWrapper(recipe);
			recipes.add(wrapper);
		}
		return recipes;
	}
}
