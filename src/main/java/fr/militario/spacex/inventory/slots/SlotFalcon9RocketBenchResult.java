package fr.militario.spacex.inventory.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFalcon9RocketBenchResult extends Slot {

	private final IInventory craftMatrix;
	private final EntityPlayer thePlayer;

	public SlotFalcon9RocketBenchResult(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
		super(par3IInventory, par4, par5, par6);
		this.thePlayer = par1EntityPlayer;
		this.craftMatrix = par2IInventory;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer par1EntityPlayer, ItemStack par1ItemStack) {
		for (int var2 = 0; var2 < this.craftMatrix.getSizeInventory(); var2++) {
			ItemStack var3 = this.craftMatrix.getStackInSlot(var2);
			if (var3 != null) {
				this.craftMatrix.decrStackSize(var2, var3.getCount());
				if (var3.getItem().hasContainerItem(var3)) {
					ItemStack var4 = new ItemStack(var3.getItem().getContainerItem());
					if (!this.thePlayer.inventory.addItemStackToInventory(var4))
						if (this.craftMatrix.getStackInSlot(var2) == null)
							this.craftMatrix.setInventorySlotContents(var2, var4);
						else
							this.thePlayer.entityDropItem(var4, 0.0F);
				}
			}
		}
		return par1ItemStack;
	}
}
