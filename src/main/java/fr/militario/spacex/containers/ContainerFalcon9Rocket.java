package fr.militario.spacex.containers;

import net.minecraft.inventory.IInventory;

public class ContainerFalcon9Rocket extends ContainerSpaceXRocket {

	public ContainerFalcon9Rocket(IInventory inventoryPlayer, IInventory tileInventory) {
		super(inventoryPlayer, tileInventory);
		addSlotsNoInventory();
	}
}
