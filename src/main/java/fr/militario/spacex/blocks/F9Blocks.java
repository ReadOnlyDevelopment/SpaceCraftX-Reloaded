package fr.militario.spacex.blocks;

import java.lang.reflect.Constructor;

import com.google.common.collect.ObjectArrays;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.items.ItemBlockDesc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class F9Blocks {
	public static Block oxygenLoader;
	public static Block oxygenUnloader;
	public static Block fuelUnloader;
	public static Block lithiumOre;

	public static void initBlocks() {
		oxygenLoader = new BlockOxygenLoader("oxygen_loader");
		oxygenUnloader = new BlockOxygenUnloader("oxygen_unloader");
		fuelUnloader = new BlockFuelUnloader("fuel_unloader");
		lithiumOre = (new BlockOre()).setUnlocalizedName("ore_lithium").setCreativeTab(SpaceX.SpaceXtab).setHardness(5.0F);
		registerBlocks();
	}

	public static void oreDictRegistrations() {
		OreDictionary.registerOre("oreLithium", lithiumOre);
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass, Object... itemCtorArgs) {
		String name = block.getUnlocalizedName().substring(5);
		if (block.getRegistryName() == null)
			block.setRegistryName(name);
		SpaceXUtils.registerBlock(name, block);
		if (itemClass != null) {
			ItemBlock item = null;
			Class<?>[] ctorArgClasses = new Class[itemCtorArgs.length + 1];
			ctorArgClasses[0] = Block.class;
			for (int idx = 1; idx < ctorArgClasses.length; idx++)
				ctorArgClasses[idx] = itemCtorArgs[idx - 1].getClass();
			try {
				Constructor<? extends ItemBlock> constructor = itemClass.getConstructor(ctorArgClasses);
				item = constructor.newInstance(ObjectArrays.concat(block, itemCtorArgs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (item != null) {
				SpaceXUtils.registerItem(name, item);
				if (item.getRegistryName() == null)
					item.setRegistryName(name);
			}
		}
	}

	public static void registerBlocks() {
		registerBlock(oxygenLoader, ItemBlockDesc.class, new Object[0]);
		registerBlock(oxygenUnloader, ItemBlockDesc.class, new Object[0]);
		registerBlock(fuelUnloader, ItemBlockDesc.class, new Object[0]);
		registerBlock(lithiumOre, ItemBlock.class, new Object[0]);
	}

	public static void registerBlocks(IForgeRegistry<Block> registry) {
		for (Block block : SpaceX.blocksList)
			registry.register(block);
	}

	public static void registerRender(Block block) {
		FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("spacex:" + block.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerRender(Block block, int meta) {
		FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation("spacex:" + block.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerRenders() {
		registerRender(oxygenLoader);
		registerRender(oxygenUnloader);
		registerRender(fuelUnloader);
		registerRender(lithiumOre);
	}
}
