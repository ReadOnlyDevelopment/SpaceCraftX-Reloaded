package fr.militario.spacex.items;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class F9Items {

	public static Item Falcon9RocketItem;
	public static Item Falcon9RocketItem_used;
	public static Item DragonCapsuleItem;
	public static Item DragonCapsuleItem_used;
	public static Item DragonTrunkItem;
	public static Item DragonTrunkItem_used;
	public static Item F9SecondStageRocketItem;
	public static Item F9SecondStageRocketItem_used;
	public static Item Merlin1D;
	public static Item DracoEngine;
	public static Item GridFin;
	public static Item Leg;
	public static Item oxygenTank;
	public static Item RP1Tank;
	public static Item nozzle;
	public static Item combustionChamber;
	public static Item turboPump;
	public static Item ingot_lithium;
	public static Item compressed_AlLi;
	public static Item compressed_carbonfiber;
	public static Item pressureValve;
	public static Item trunk_fin;
	public static Item seat;
	public static Item schematic;

	public static void initItems() {
		Falcon9RocketItem = new Falcon9RocketItem("falcon9");
		Falcon9RocketItem_used = new Falcon9RocketSeparatedItem("falcon9_used");
		DragonCapsuleItem = new DragonCapsuleItem("dragon");
		DragonCapsuleItem_used = new DragonCapsuleSeparatedItem("dragon_used");
		DragonTrunkItem = new DragonTrunkItem("trunk");
		DragonTrunkItem_used = new DragonTrunkSeparatedItem("trunk_used");
		F9SecondStageRocketItem = new F9SecondStageRocketItem("secondstage");
		F9SecondStageRocketItem_used = new F9SecondStageRocketSeparatedItem("secondstage_used");
		Merlin1D = (new HeavyItem(true)).setUnlocalizedName("merlin1d").setMaxStackSize(9);
		DracoEngine = (new HeavyItem(false)).setUnlocalizedName("draco").setMaxStackSize(4);
		GridFin = (new Item()).setUnlocalizedName("gridfin").setCreativeTab(SpaceX.SpaceXtab);
		Leg = (new HeavyItem(false)).setUnlocalizedName("leg");
		seat = (new HeavyItem(true)).setUnlocalizedName("seat");
		oxygenTank = (new HeavyItem(true)).setUnlocalizedName("oxygen_tank");
		RP1Tank = (new HeavyItem(true)).setUnlocalizedName("rp1_tank");
		nozzle = (new HeavyItem(false)).setUnlocalizedName("nozzle");
		combustionChamber = (new HeavyItem(false)).setUnlocalizedName("combustion_chamber");
		turboPump = (new HeavyItem(false)).setUnlocalizedName("turbopump");
		ingot_lithium = (new Item()).setUnlocalizedName("ingot_lithium").setCreativeTab(SpaceX.SpaceXtab);
		compressed_AlLi = (new Item()).setUnlocalizedName("compressed_alli").setCreativeTab(SpaceX.SpaceXtab);
		compressed_carbonfiber = (new Item()).setUnlocalizedName("compressed_carbonfiber").setCreativeTab(SpaceX.SpaceXtab);
		pressureValve = (new Item()).setUnlocalizedName("pressure_valve").setCreativeTab(SpaceX.SpaceXtab);
		trunk_fin = (new Item()).setUnlocalizedName("trunk_fin").setCreativeTab(SpaceX.SpaceXtab);
		schematic = new ItemSchematic("schematic");
		registerItems();
	}

	public static void oreDictRegistrations() {
		OreDictionary.registerOre(ingot_lithium.getUnlocalizedName().substring(5), ingot_lithium);
	}

	public static void registerItem(Item item) {
		String name = item.getUnlocalizedName().substring(5);
		if (item.getRegistryName() == null)
			item.setRegistryName(name);
		SpaceXUtils.registerItem(name, item);
	}

	public static void registerItems() {
		registerItem(Falcon9RocketItem);
		registerItem(Falcon9RocketItem_used);
		registerItem(DragonCapsuleItem);
		registerItem(DragonCapsuleItem_used);
		registerItem(DragonTrunkItem);
		registerItem(DragonTrunkItem_used);
		registerItem(F9SecondStageRocketItem);
		registerItem(F9SecondStageRocketItem_used);
		registerItem(Merlin1D);
		registerItem(DracoEngine);
		registerItem(GridFin);
		registerItem(Leg);
		registerItem(seat);
		registerItem(oxygenTank);
		registerItem(RP1Tank);
		registerItem(nozzle);
		registerItem(combustionChamber);
		registerItem(turboPump);
		registerItem(ingot_lithium);
		registerItem(compressed_AlLi);
		registerItem(compressed_carbonfiber);
		registerItem(pressureValve);
		registerItem(trunk_fin);
		registerItem(schematic);
	}

	public static void registerItems(IForgeRegistry<Item> registry) {
		for (ItemStack item : SpaceX.itemList)
			registry.register(item.getItem());
	}

	public static void registerRender(Item item) {
		FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("spacex:" + item.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerRender(Item item, String location, int meta) {
		FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation("spacex:" + location, "inventory"));
	}

	public static void registerRenders() {
		registerRender(ingot_lithium);
		registerRender(compressed_AlLi);
		registerRender(compressed_carbonfiber);
		registerRender(pressureValve);
		registerRender(trunk_fin);
		registerRender(schematic);
		registerRender(schematic, schematic.getUnlocalizedName().substring(5) + "_dragon", 1);
		registerRender(schematic, schematic.getUnlocalizedName().substring(5) + "_secondstage", 2);
		registerRender(schematic, schematic.getUnlocalizedName().substring(5) + "_dragon_trunk", 3);
	}
}
