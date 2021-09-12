package fr.militario.spacex.containers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ContainerSpaceXRocket extends Container {

	private final IInventory lowerChestInventory;
	private final IInventory spaceshipInv;

	public ContainerSpaceXRocket(IInventory lowerChestInventory, IInventory spaceshipInv) {
		this.lowerChestInventory = lowerChestInventory;
		this.spaceshipInv = spaceshipInv;
		spaceshipInv.openInventory(FMLClientHandler.instance().getClient().player);
	}

	protected void addSlotsNoInventory() {
		int i;
		int j;
		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.lowerChestInventory, j + ((i + 1) * 9), 8 + (j * 18), (84 + (i * 18)) - 34));
			}
		}
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.lowerChestInventory, i, 8 + (i * 18), 142 - 34));
		}
	}

	protected void addSlotsWithInventory() {
		int var4;
		for (var4 = 0; var4 < 3; var4++) {
			for (int var5 = 0; var5 < 9; var5++) {
				addSlotToContainer(new Slot(this.spaceshipInv, var5 + (var4 * 9) + 1, 8 + (var5 * 18), (84 + (var4 * 18)) - 34));
			}
		}
		for (var4 = 0; var4 < 3; var4++) {
			for (int var5 = 0; var5 < 9; var5++) {
				addSlotToContainer(new Slot(this.lowerChestInventory, var5 + (var4 * 9) + 9, 8 + (var5 * 18), (146 + (var4 * 18)) - 34));
			}
		}
		for (var4 = 0; var4 < 9; var4++) {
			addSlotToContainer(new Slot(this.lowerChestInventory, var4, 8 + (var4 * 18), 170));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.spaceshipInv.isUsableByPlayer(par1EntityPlayer);
	}

	@Override
	public @Nonnull ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if ((slot != null) && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if ((index == 2) || (index == 3)) { // TODO: Does this need to be changed?
				if (!this.mergeItemStack(itemstack1, 4, 40, true))
					return ItemStack.EMPTY;

				slot.onSlotChange(itemstack1, itemstack);
			} else if ((index != 1) && (index != 0)) {
				if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty()) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
						return ItemStack.EMPTY;
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false))
						return ItemStack.EMPTY;
				} else if ((index >= 4) && (index < 31)) {
					if (!this.mergeItemStack(itemstack1, 31, 40, false))
						return ItemStack.EMPTY;
				} else if ((index >= 31) && (index < 40) && !this.mergeItemStack(itemstack1, 4, 31, false))
					return ItemStack.EMPTY;
			} else if (!this.mergeItemStack(itemstack1, 4, 40, false))
				return ItemStack.EMPTY;
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}
}
