package fr.militario.spacex.sides;

import java.util.HashMap;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.blocks.F9Blocks;
import fr.militario.spacex.items.F9Items;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class RecipeHandler {

	public static void registerCompressorRecipes() {
		CompressorRecipes.addShapelessRecipe(new ItemStack(F9Items.compressed_AlLi), new Object[] { new ItemStack(GCItems.basicItem, 1, 5), F9Items.ingot_lithium });
		CompressorRecipes.addRecipe(new ItemStack(F9Items.compressed_carbonfiber), new Object[] { "XXX", "XXX", Character.valueOf('X'), Items.COAL });
	}

	public static void registerFurnaceRecipes() {
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(F9Blocks.lithiumOre), new ItemStack(F9Items.ingot_lithium), 0.5F);
	}

	public static void registerSchematicRecipes() {
		HashMap<Integer, ItemStack> input = new HashMap<>();
		input.put(Integer.valueOf(1), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(2), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(3), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(4), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(5), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(6), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(7), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(8), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(9), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(10), new ItemStack(GCItems.basicItem, 1, 8));
		input.put(Integer.valueOf(11), new ItemStack(F9Items.Merlin1D, 9, 0));
		input.put(Integer.valueOf(12), new ItemStack(F9Items.GridFin));
		input.put(Integer.valueOf(13), new ItemStack(F9Items.GridFin));
		input.put(Integer.valueOf(14), new ItemStack(F9Items.GridFin));
		input.put(Integer.valueOf(15), new ItemStack(F9Items.GridFin));
		input.put(Integer.valueOf(16), new ItemStack(F9Items.Leg));
		input.put(Integer.valueOf(17), new ItemStack(F9Items.Leg));
		input.put(Integer.valueOf(18), new ItemStack(F9Items.Leg));
		input.put(Integer.valueOf(19), new ItemStack(F9Items.Leg));
		input.put(Integer.valueOf(20), new ItemStack(GCItems.basicItem, 1, 19));
		input.put(Integer.valueOf(21), new ItemStack(F9Items.oxygenTank));
		input.put(Integer.valueOf(22), new ItemStack(F9Items.RP1Tank));
		SpaceXUtils.addRocketBenchRecipe(SpaceX.falcon9rocketBenchRecipe, new ItemStack(F9Items.Falcon9RocketItem, 1), input);
		HashMap<Integer, ItemStack> input1 = new HashMap<>();
		input1.put(Integer.valueOf(1), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(2), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(3), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(4), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(5), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(6), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(7), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(8), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(9), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(10), new ItemStack(GCItems.basicItem, 1, 8));
		input1.put(Integer.valueOf(11), new ItemStack(F9Items.Merlin1D));
		input1.put(Integer.valueOf(12), new ItemStack(GCItems.basicItem, 1, 19));
		input1.put(Integer.valueOf(13), new ItemStack(F9Items.oxygenTank));
		input1.put(Integer.valueOf(14), new ItemStack(F9Items.RP1Tank));
		SpaceXUtils.addRocketBenchRecipe(SpaceX.secondstageBenchRecipe, new ItemStack(F9Items.F9SecondStageRocketItem, 1), input1);
		HashMap<Integer, ItemStack> input2 = new HashMap<>();
		input2.put(Integer.valueOf(1), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(2), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(3), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(4), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(5), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(6), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(7), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(8), new ItemStack(GCItems.basicItem, 1, 8));
		input2.put(Integer.valueOf(9), new ItemStack(F9Items.DracoEngine));
		input2.put(Integer.valueOf(10), new ItemStack(F9Items.DracoEngine));
		input2.put(Integer.valueOf(11), new ItemStack(Blocks.GLASS));
		input2.put(Integer.valueOf(12), new ItemStack(Blocks.GLASS));
		input2.put(Integer.valueOf(13), new ItemStack(GCItems.basicItem, 1, 19));
		input2.put(Integer.valueOf(14), new ItemStack(F9Items.RP1Tank));
		input2.put(Integer.valueOf(15), new ItemStack(F9Items.oxygenTank));
		input2.put(Integer.valueOf(16), new ItemStack(F9Items.seat));
		input2.put(Integer.valueOf(17), new ItemStack(GCBlocks.screen));
		SpaceXUtils.addRocketBenchRecipe(SpaceX.dragonBenchRecipe, new ItemStack(F9Items.DragonCapsuleItem, 1), input2);
		HashMap<Integer, ItemStack> input3 = new HashMap<>();
		input3.put(Integer.valueOf(1), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(2), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(3), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(4), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(5), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(6), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(7), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(8), new ItemStack(GCItems.basicItem, 1, 8));
		input3.put(Integer.valueOf(9), new ItemStack(F9Items.trunk_fin));
		input3.put(Integer.valueOf(10), new ItemStack(F9Items.trunk_fin));
		input3.put(Integer.valueOf(11), new ItemStack(F9Items.trunk_fin));
		input3.put(Integer.valueOf(12), new ItemStack(F9Items.trunk_fin));
		SpaceXUtils.addRocketBenchRecipe(SpaceX.dragontrunkBenchRecipe, new ItemStack(F9Items.DragonTrunkItem, 1), input3);
	}
}
