package fr.militario.spacex.containers;

import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {

	@Deprecated
	public static void onTakeFromSlot(Slot slot, EntityPlayer player, ItemStack stack) {
		slot.onTake(player, stack);
	}

	protected final IInventory tileInventory;
	protected final IInventory playerInventory;

	public ContainerBase(InventoryPlayer playerInventory, IInventory tileInventory) {
		this.tileInventory = tileInventory;
		this.playerInventory = playerInventory;
		addTileInventorySlots(tileInventory);
		addPlayerInventorySlots(playerInventory);
	}

	protected void addPlayerInventorySlots(InventoryPlayer inv) {
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inv, i, 8 + (i * 18), 142));
		}
	}

	protected void addTileInventorySlots(IInventory inv) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileInventory.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if ((slot != null) && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < 2) {
				if (!mergeItemStack(itemstack1, 2, 38, true))
					return ItemStack.EMPTY;
			} else if (EnergyUtil.isElectricItem(itemstack1.getItem())) {
				if (!mergeItemStack(itemstack1, 0, 1, false))
					return ItemStack.EMPTY;
			} else if (FluidUtil.isFuelContainerAny(itemstack1)) {
				if (!mergeItemStack(itemstack1, 1, 2, false))
					return ItemStack.EMPTY;
			} else if (index < 29) {
				if (!mergeItemStack(itemstack1, 29, 38, false))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(itemstack1, 2, 29, false))
				return ItemStack.EMPTY;
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(entityPlayer, itemstack1);
		}
		return itemstack;
	}
}
