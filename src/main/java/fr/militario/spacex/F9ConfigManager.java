package fr.militario.spacex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class F9ConfigManager {
	static Configuration config;
	public static int rocketTierFalcon9;
	public static int rocketTierDragon;
	public static int rocketFuelCapacityFalcon9;
	public static int rocketOxygenCapacityFalcon9;
	public static int rocketFuelCapacitySecondStage;
	public static int rocketOxygenCapacitySecondStage;
	public static int rocketFuelCapacityDragon;
	public static int rocketOxygenCapacityDragon;
	public static int idSchematicFalcon9;
	public static int idSchematicSecondStage;
	public static int idSchematicDragon;
	public static int idSchematicDragonTrunk;
	public static int rocketOxygenFactor;
	public static boolean enableLithiumOreGen;
	public static ArrayList<Object> clientSave = null;

	public static void forceSave() {
		config.save();
	}

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();
		list.addAll((new ConfigElement(config.getCategory("schematic"))).getChildElements());
		list.addAll((new ConfigElement(config.getCategory("general"))).getChildElements());
		return list;
	}

	public static List<Object> getServerConfigOverride() {
		List<Object> returnList = new ArrayList<>();
		returnList.add(Integer.valueOf(rocketOxygenFactor));
		return returnList;
	}

	public static void initialize(File file) {
		config = new Configuration(file);
		syncConfig(true);
	}

	public static void restoreClientConfigOverrideable() {
		if (clientSave != null) {
			setConfigOverride(clientSave);
		}
	}

	public static void saveClientConfigOverrideable() {
		if (clientSave == null) {
			clientSave = (ArrayList<Object>) getServerConfigOverride();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void setConfigOverride(List<Object> configs) {
		int dataCount = 0;
		rocketOxygenFactor = ((Integer) configs.get(dataCount++)).intValue();
	}

	public static void syncConfig(boolean load) {
		List<String> propOrder = new ArrayList<>();
		try {
			if (!config.isChild)
				if (load) {
					config.load();
				}
			Property prop = config.get("general", "rocketTierFalconCraft9", 1);
			prop.setComment("Tier level for FalconCraft 9 booster.");
			prop.setLanguageKey("f9.configgui.rocket_tier-falcon9");
			rocketTierFalcon9 = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketTierDragonCraft", 4);
			prop.setComment("Tier level for DragonCraft capsule.");
			prop.setLanguageKey("f9.configgui.rocket_tier-dragon");
			rocketTierDragon = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketFuelCapacityFalconCraft9", 1000);
			prop.setComment("Fuel tank capacity for FalconCraft 9 booster.");
			prop.setLanguageKey("f9.configgui.rocket_fuel_capacity-falcon9");
			rocketFuelCapacityFalcon9 = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketOxygenCapacityFalconCraft9", 1000);
			prop.setComment("Oxygen tank capacity for FalconCraft 9 booster.");
			prop.setLanguageKey("f9.configgui.rocket_oxygen_capacity-falcon9");
			rocketOxygenCapacityFalcon9 = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketFuelCapacitySecondStage", 500);
			prop.setComment("Fuel tank capacity for FalconCraft 9 second stage.");
			prop.setLanguageKey("f9.configgui.rocket_fuel_capacity-secondstage");
			rocketFuelCapacitySecondStage = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketOxygenCapacitySecondStage", 500);
			prop.setComment("Oxygen tank capacity for FalconCraft 9 second stage.");
			prop.setLanguageKey("f9.configgui.rocket_oxygen_capacity-secondstage");
			rocketOxygenCapacitySecondStage = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketFuelCapacityDragonCraft", 1000);
			prop.setComment("Fuel tank capacity for DragonCraft capsule.");
			prop.setLanguageKey("f9.configgui.rocket_fuel_capacity-dragon");
			rocketFuelCapacityDragon = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "rocketOxygenCapacityDragonCraft", 1000);
			prop.setComment("Oxygen tank capacity for DragonCraft capsule.");
			prop.setLanguageKey("f9.configgui.rocket_oxygen_capacity-dragon");
			rocketOxygenCapacityDragon = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("schematic", "idSchematicFalconCraft9", 6);
			prop.setComment("Schematic ID for FalconCraft 9 rocket, must be unique.");
			prop.setLanguageKey("f9.configgui.id_schematic-falcon9");
			idSchematicFalcon9 = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("schematic", "idSchematicSecondStage", 7);
			prop.setComment("Schematic ID for FalconCraft 9 second stage, must be unique.");
			prop.setLanguageKey("f9.configgui.id_schematic-secondstage");
			idSchematicSecondStage = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("schematic", "idSchematicDragonCraft", 8);
			prop.setComment("Schematic ID for DragonCraft capsule, must be unique.");
			prop.setLanguageKey("f9.configgui.id_schematic-dragon");
			idSchematicDragon = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("schematic", "idSchematicDragonTrunk", 9);
			prop.setComment("Schematic ID for DragonCraft trunk, must be unique.");
			prop.setLanguageKey("f9.configgui.id_schematic-dragontrunk");
			idSchematicDragonTrunk = prop.getInt();
			propOrder.add(prop.getName());
			prop = config.get("general", "Enable Lithium Ore Gen", true);
			prop.setComment("If this is enabled, lithium ore will generate on the overworld.");
			prop.setLanguageKey("f9.configgui.enable_lithium_ore_gen").setRequiresMcRestart(true);
			enableLithiumOreGen = prop.getBoolean(true);
			propOrder.add(prop.getName());
			prop = config.get("general", "Rocket oxygen factor", 1);
			prop.setComment("The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oxygen (e.g. Mekanism) are installed to increase Falcon rocket oxygen requirement.");
			prop.setLanguageKey("f9.configgui.rocket_oxygen_factor");
			rocketOxygenFactor = prop.getInt();
			propOrder.add(prop.getName());
			config.setCategoryPropertyOrder("general", propOrder);
			if (config.hasChanged()) {
				config.save();
			}
		} catch (Exception e) {
			System.out.println("Problem loading core config (\"core.conf\")");
			e.printStackTrace();
		}
	}
}
