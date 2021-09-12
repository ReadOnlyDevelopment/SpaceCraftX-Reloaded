package fr.militario.spacex.inventory;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class InventorySecondStageBench implements IInventoryDefaults {

	public NonNullList<ItemStack> stacks;
	private final int inventoryWidth;
	private final Container eventHandler;

	public InventorySecondStageBench(Container par1Container) {
		this.stacks = NonNullList.withSize(15, ItemStack.EMPTY);
		this.eventHandler = par1Container;
		this.inventoryWidth = 5;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);
		if (!itemstack.isEmpty()) {
			markDirty();
			this.eventHandler.onCraftMatrixChanged(this);
		}
		return itemstack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getName() {
		return "container.crafting";
	}

	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	public ItemStack getStackInRowAndColumn(int par1, int par2) {
		if ((par1 >= 0) && (par1 < this.inventoryWidth)) {
			int var3 = par1 + (par2 * this.inventoryWidth);
			if (var3 >= this.stacks.size())
				return ItemStack.EMPTY;
			return getStackInSlot(var3);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.stacks.get(index);
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.stacks)
			if (!itemstack.isEmpty())
				return false;
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer) {
		return true;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack oldstack = ItemStackHelper.getAndRemove(this.stacks, index);
		if (!oldstack.isEmpty()) {
			markDirty();
			this.eventHandler.onCraftMatrixChanged(this);
		}
		return oldstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		this.stacks.set(index, stack);
		markDirty();
		this.eventHandler.onCraftMatrixChanged(this);
	}
}
