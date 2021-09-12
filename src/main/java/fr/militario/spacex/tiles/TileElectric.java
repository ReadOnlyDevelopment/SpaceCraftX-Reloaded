package fr.militario.spacex.tiles;

import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public abstract class TileElectric extends TileBaseElectricBlockWithInventory {

	public TileElectric(String tileName) {
		super(tileName);
	}

	public NonNullList<ItemStack> readStandardItemsFromNBT(NBTTagCompound nbt) {
		NonNullList<ItemStack> stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, stacks);
		return stacks;
	}

	public void writeStandardItemsToNBT(NBTTagCompound nbt, NonNullList<ItemStack> stacks) {
		ItemStackHelper.saveAllItems(nbt, stacks);
	}
}
