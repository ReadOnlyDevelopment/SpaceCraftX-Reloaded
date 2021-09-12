package fr.militario.spacex.jei.dragon;

import javax.annotation.Nonnull;

import fr.militario.spacex.F9Constants;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.ResourceLocation;

public class DragonRecipeCategory extends BlankRecipeCategory {
	private static final ResourceLocation rocketGuiTexture = new ResourceLocation(F9Constants.MODID, "textures/gui/dragonbench.png");

	@Nonnull
	private final IDrawable background;

	@Nonnull
	private final String localizedName;

	public DragonRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(rocketGuiTexture, 3, 4, 168, 130);
		this.localizedName = GCCoreUtil.translate("tile.rocket_workbench.name");
	}

	@Override
	@Nonnull
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public String getModName() {
		return "SpaceCraftX";
	}

	@Override
	@Nonnull
	public String getTitle() {
		return this.localizedName;
	}

	@Override
	@Nonnull
	public String getUid() {
		return "spacex.dragon";
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();
		int x = 4;
		int y = 5;
		itemstacks.init(0, true, 39 - x, 109 - y);
		itemstacks.init(1, true, 57 - x, 109 - y);
		itemstacks.init(2, true, 75 - x, 109 - y);
		itemstacks.init(3, true, 21 - x, 73 - y);
		itemstacks.init(4, true, 93 - x, 73 - y);
		itemstacks.init(5, true, 39 - x, 55 - y);
		itemstacks.init(6, true, 75 - x, 55 - y);
		itemstacks.init(7, true, 57 - x, 37 - y);
		itemstacks.init(8, true, 21 - x, 91 - y);
		itemstacks.init(9, true, 93 - x, 91 - y);
		itemstacks.init(10, true, 39 - x, 73 - y);
		itemstacks.init(11, true, 75 - x, 73 - y);
		itemstacks.init(12, true, 75 - x, 91 - y);
		itemstacks.init(13, true, 39 - x, 91 - y);
		itemstacks.init(14, true, 57 - x, 91 - y);
		itemstacks.init(15, true, 57 - x, 73 - y);
		itemstacks.init(16, true, 57 - x, 55 - y);
		itemstacks.init(17, false, 142 - x, 96 - y);
		itemstacks.set(ingredients);
	}
}
