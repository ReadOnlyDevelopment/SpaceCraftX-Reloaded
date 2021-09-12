package fr.militario.spacex.jei;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.jei.dragon.DragonRecipeCategory;
import fr.militario.spacex.jei.dragon.DragonRecipeMaker;
import fr.militario.spacex.jei.falcon9.Falcon9RecipeCategory;
import fr.militario.spacex.jei.falcon9.Falcon9RecipeMaker;
import fr.militario.spacex.jei.secondstage.F9SecondStageRecipeCategory;
import fr.militario.spacex.jei.secondstage.F9SecondStageRecipeMaker;
import fr.militario.spacex.jei.trunk.DragonTrunkRecipeCategory;
import fr.militario.spacex.jei.trunk.DragonTrunkRecipeMaker;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class SpaceXJEI extends BlankModPlugin {

	private static IModRegistry registryCached = null;
	private static boolean JEIversion450plus = false;
	public static final String FALCON9_ID = "spacex.falcon9";
	public static final String DRAGON_ID = "spacex.dragon";
	public static final String SECONDSTAGE_ID = "spacex.secondstage";
	public static final String TRUNK_ID = "spacex.trunk";

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registryCached = registry;
		Method[] methods = registry.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().equals("addRecipeCatalyst")) {
				JEIversion450plus = true;
				break;
			}
		}
		if (!JEIversion450plus) {
			IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
			registry.addRecipeCategories(new IRecipeCategory[] { new Falcon9RecipeCategory(guiHelper), new F9SecondStageRecipeCategory(guiHelper), new DragonRecipeCategory(guiHelper), new DragonTrunkRecipeCategory(guiHelper) });
		}
		registry.handleRecipes(INasaWorkbenchRecipe.class, fr.militario.spacex.jei.falcon9.Falcon9RecipeWrapper::new, "spacex.falcon9");
		registry.handleRecipes(INasaWorkbenchRecipe.class, fr.militario.spacex.jei.secondstage.F9SecondStageRecipeWrapper::new, "spacex.secondstage");
		registry.handleRecipes(INasaWorkbenchRecipe.class, fr.militario.spacex.jei.dragon.DragonRecipeWrapper::new, "spacex.dragon");
		registry.handleRecipes(INasaWorkbenchRecipe.class, fr.militario.spacex.jei.trunk.DragonTrunkRecipeWrapper::new, "spacex.trunk");
		registry.addRecipes(Falcon9RecipeMaker.getRecipesList(), "spacex.falcon9");
		registry.addRecipes(F9SecondStageRecipeMaker.getRecipesList(), "spacex.secondstage");
		registry.addRecipes(DragonRecipeMaker.getRecipesList(), "spacex.dragon");
		registry.addRecipes(DragonTrunkRecipeMaker.getRecipesList(), "spacex.trunk");
		if (JEIversion450plus) {
			registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), new String[] { "spacex.falcon9", "spacex.secondstage", "spacex.dragon", "spacex.trunk" });
		} else {
			ItemStack nasaWorkbench = new ItemStack(GCBlocks.nasaWorkbench);
			registry.addRecipeCategoryCraftingItem(nasaWorkbench, new String[] { "spacex.falcon9", "spacex.secondstage", "spacex.dragon", "spacex.trunk" });
		}
		SpaceX.logger.info("spacex JEI Plugin loaded");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new IRecipeCategory[] { new Falcon9RecipeCategory(guiHelper), new F9SecondStageRecipeCategory(guiHelper), new DragonRecipeCategory(guiHelper), new DragonTrunkRecipeCategory(guiHelper) });
	}
}
