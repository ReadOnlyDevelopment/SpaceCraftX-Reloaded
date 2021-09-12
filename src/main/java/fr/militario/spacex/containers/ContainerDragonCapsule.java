package fr.militario.spacex.containers;

import fr.militario.spacex.inventory.slots.SlotDragonPads;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerDragonCapsule extends ContainerSpaceXRocket {

	private final IInventory playerInventory;

	public ContainerDragonCapsule(IInventory inventoryPlayer, IInventory tileInventory) {
		super(inventoryPlayer, tileInventory);
		addSlotToContainer(new SlotDragonPads(tileInventory, 0, 80, 0));
		addSlotsWithInventory();
		this.playerInventory = inventoryPlayer;
	}

	public IInventory getLowerChestInventory() {
		return this.playerInventory;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		this.playerInventory.closeInventory(par1EntityPlayer);
	}
}
