package fr.militario.spacex;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.militario.spacex.advancement.CustomTrigger;
import fr.militario.spacex.advancement.Triggers;
import fr.militario.spacex.asm.ASM;
import fr.militario.spacex.blocks.F9Blocks;
import fr.militario.spacex.entity.EntityDragonCapsule;
import fr.militario.spacex.entity.EntityDragonTrunk;
import fr.militario.spacex.entity.EntityF9SecondStage;
import fr.militario.spacex.entity.EntityFalcon9Rocket;
import fr.militario.spacex.items.F9Items;
import fr.militario.spacex.schematics.SchematicDragon;
import fr.militario.spacex.schematics.SchematicDragonTrunk;
import fr.militario.spacex.schematics.SchematicFalcon9Rocket;
import fr.militario.spacex.schematics.SchematicSecondStage;
import fr.militario.spacex.sides.CommonProxy;
import fr.militario.spacex.sides.RecipeHandler;
import fr.militario.spacex.teleports.OverridingTeleport;
import fr.militario.spacex.tiles.TileEntityFuelUnloader;
import fr.militario.spacex.tiles.TileEntityOxygenLoader;
import fr.militario.spacex.tiles.TileEntityOxygenUnloader;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.world.gen.OverworldGenerator;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

//@formatter:off
@Mod(
		modid = F9Constants.MODID,
		name = "SpaceCraftX",
		version = F9Constants.COMBINEDVERSION,
		acceptedMinecraftVersions = "[1.12.2]",
		dependencies = F9Constants.DEPENDENCIES,
		certificateFingerprint = F9Constants.SHA1
)
//@formatter:on
public class SpaceX {

	@EventBusSubscriber(modid = "spacex")
	public static class RegistrationHandler {
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			F9Blocks.registerBlocks(event.getRegistry());
		}

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerItems(RegistryEvent.Register<Item> event) {
			F9Items.registerItems(event.getRegistry());
			F9Items.oreDictRegistrations();
			F9Blocks.oreDictRegistrations();
		}

		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			SpaceX.proxy.registerVariants();
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			RecipeHandler.registerFurnaceRecipes();
			RecipeHandler.registerSchematicRecipes();
			RecipeHandler.registerCompressorRecipes();
		}
	}

	@Instance("spacex")
	public static SpaceX instance;

	@SidedProxy(clientSide = "fr.militario.spacex.sides.SpaceXClient", serverSide = "fr.militario.spacex.sides.SpaceXServer")
	public static CommonProxy proxy;
	public static Log logger;
	public static LinkedList<ItemStack> itemList = new LinkedList<>();
	public static LinkedList<Block> blocksList = new LinkedList<>();
	public static SpaceXCreativeTab SpaceXtab = new SpaceXCreativeTab("spacex");
	public static List<INasaWorkbenchRecipe> falcon9rocketBenchRecipe = new ArrayList<>();
	public static List<INasaWorkbenchRecipe> secondstageBenchRecipe = new ArrayList<>();
	public static List<INasaWorkbenchRecipe> dragonBenchRecipe = new ArrayList<>();
	public static List<INasaWorkbenchRecipe> dragontrunkBenchRecipe = new ArrayList<>();

	public SpaceX() {
		logger = new Log(this, SpaceXUtils.isDevelopmentEnvironment());
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		if (SpaceXUtils.isDevelopmentEnvironment()) {
			logger.info("Ignoring fingerprint signing since we are in a Development Environment");
			return;
		} else
			throw new InvalidFingerprintException(event);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		F9ConfigManager.initialize(new File(event.getModConfigurationDirectory(), F9Constants.CONFIG_FILE));
		F9Items.initItems();
		F9Blocks.initBlocks();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerGuiHandler();
		proxy.init(event);
		try {
			Method method = ASM.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
			method.setAccessible(true);
			for (CustomTrigger element : Triggers.TRIGGER_ARRAY) {
				method.invoke(null, element);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e) {
			e.printStackTrace();
		}
		MinecraftForge.EVENT_BUS.register(new SpaceXEventHandler());
		SchematicRegistry.registerSchematicRecipe(new SchematicFalcon9Rocket());
		SchematicRegistry.registerSchematicRecipe(new SchematicSecondStage());
		SchematicRegistry.registerSchematicRecipe(new SchematicDragon());
		SchematicRegistry.registerSchematicRecipe(new SchematicDragonTrunk());
		SpaceXUtils.registerSpaceXRocketEntity(EntityFalcon9Rocket.class, "FalconCraft 9", 150, 1, false);
		SpaceXUtils.registerSpaceXRocketEntity(EntityDragonCapsule.class, "DragonCraft V2", 150, 1, false);
		SpaceXUtils.registerSpaceXRocketEntity(EntityF9SecondStage.class, "FC9 SecondStage", 150, 1, false);
		SpaceXUtils.registerSpaceXRocketEntity(EntityDragonTrunk.class, "DragonCraft Trunk", 150, 1, false);
		GameRegistry.registerTileEntity(TileEntityOxygenLoader.class, new ResourceLocation(F9Constants.MODID, "SpaceCraftX Oxygen Loader"));
		GameRegistry.registerTileEntity(TileEntityOxygenUnloader.class, new ResourceLocation(F9Constants.MODID, "SpaceCraftX Oxygen Unloader"));
		GameRegistry.registerTileEntity(TileEntityFuelUnloader.class, new ResourceLocation(F9Constants.MODID, "SpaceCraftX Fuel Unloader"));
		if (F9ConfigManager.enableLithiumOreGen) {
			GameRegistry.registerWorldGenerator(new OverworldGenerator(F9Blocks.lithiumOre, 0, 8, 10, 60, 7), 4);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Map<Class<? extends WorldProvider>, ITeleportType> map = ASM.getStaticObject(GalacticraftRegistry.class, "teleportTypeMap");

		List<CelestialBody> bodies = new ArrayList<>();
		bodies.addAll(GalaxyRegistry.getRegisteredPlanets().values().stream().filter(CelestialBody::getReachable).sorted().collect(Collectors.toList()));
		bodies.addAll(GalaxyRegistry.getRegisteredMoons().values().stream().filter(CelestialBody::getReachable).sorted().collect(Collectors.toList()));
		for (CelestialBody body : bodies) {
			ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(body.getWorldProvider());
			map.replace(body.getWorldProvider(), type, new OverridingTeleport(type));
		}
		ASM.setPrivateValue(GalacticraftRegistry.class, new GalacticraftRegistry(), map, "teleportTypeMap");
		proxy.postInit(event);
	}
}
