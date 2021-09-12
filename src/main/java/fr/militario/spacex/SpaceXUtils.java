package fr.militario.spacex;

import java.util.HashMap;
import java.util.List;

import fr.militario.spacex.entity.EntityDragonCapsule;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@SuppressWarnings("deprecation")
public class SpaceXUtils {
	public static int nextID = 0;

	public static void addRocketBenchRecipe(List<INasaWorkbenchRecipe> recipe, ItemStack result, HashMap<Integer, ItemStack> input) {
		recipe.add(new NasaWorkbenchRecipe(result, input));
	}

	public static float DegToRad(float deg) {
		return (float) ((deg / 180.0F) * Math.PI);
	}

	public static int fillWithOxygen(FluidTank tank, FluidStack liquid, boolean doFill) {
		if ((liquid != null) && testFuel(FluidRegistry.getFluidName(liquid))) {
			FluidStack liquidInTank = tank.getFluid();
			if (liquidInTank == null)
				return tank.fill(new FluidStack(GCFluids.fluidOxygenGas, liquid.amount), doFill);
			if (liquidInTank.amount < tank.getCapacity())
				return tank.fill(new FluidStack(liquidInTank, liquid.amount), doFill);
		}
		return 0;
	}

	public static List<INasaWorkbenchRecipe> getDragonRecipe() {
		return SpaceX.dragonBenchRecipe;
	}

	public static List<INasaWorkbenchRecipe> getDragonTrunkRecipe() {
		return SpaceX.dragontrunkBenchRecipe;
	}

	public static List<INasaWorkbenchRecipe> getFalcon9RocketRecipe() {
		return SpaceX.falcon9rocketBenchRecipe;
	}

	public static IRocketType.EnumRocketType getRocketTypeInt(int value) {
		for (IRocketType.EnumRocketType phase : IRocketType.EnumRocketType.values()) {
			if (value == phase.ordinal())
				return phase;
		}
		return IRocketType.EnumRocketType.DEFAULT;
	}

	public static List<INasaWorkbenchRecipe> getSecondStageRecipe() {
		return SpaceX.secondstageBenchRecipe;
	}

	public static int nextInternalID() {
		nextID++;
		return nextID - 1;
	}

	public static void registerBlock(String key, Block block) {
		SpaceX.blocksList.add(block);
	}

	public static void registerItem(String key, Item item) {
		registerItem(key, new ItemStack(item));
	}

	public static void registerItem(String key, Item item, int metadata) {
		registerItem(key, new ItemStack(item, 1, metadata));
	}

	public static void registerItem(String key, ItemStack stack) {
		SpaceX.itemList.add(stack);
	}

	public static void registerSpaceXRocketEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel) {
		ResourceLocation registryName = new ResourceLocation(F9Constants.MODID, var1);
		EntityRegistry.registerModEntity(registryName, var0, var1, nextInternalID(), SpaceX.instance, trackingDistance, updateFreq, sendVel);
	}

	public static void spawnLander(World newWorld, EntityPlayerMP player, GCPlayerStats stats) {
		EntityDragonCapsule lander = new EntityDragonCapsule(newWorld, player.posX, player.posY, player.posZ, getRocketTypeInt(stats.getRocketType()), 0.0F);
		lander.setPosition(player.posX, player.posY, player.posZ);
		lander.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, stats.getFuelLevel()));
		lander.oxygenTank.setFluid(new FluidStack(GCFluids.fluidFuel, stats.getFuelLevel()));
		if (!newWorld.isRemote) {
			CompatibilityManager.forceLoadChunks((WorldServer) newWorld);
			lander.forceSpawn = true;
			newWorld.spawnEntity(lander);
			player.startRiding(lander);
			CompatibilityManager.forceLoadChunks((WorldServer) newWorld);
		}
		lander.setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.LANDING);
		lander.separateStages(true);
		lander.updateInventory(player);
		stats.setTeleportCooldown(10);
	}

	public static boolean testFuel(String name) {
		if (name.startsWith("oxygen"))
			return true;
		return false;
	}

	public static String translate(String key) {
		String result = I18n.translateToLocal(key);
		int comment = result.indexOf('#');
		String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
		for (int i = 0; i < key.length(); i++) {
			Character c = Character.valueOf(key.charAt(i));
			if (Character.isUpperCase(c.charValue())) {
				System.err.println(ret);
			}
		}
		return ret;
	}

	public static boolean isDevelopmentEnvironment() {
		return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}
}
