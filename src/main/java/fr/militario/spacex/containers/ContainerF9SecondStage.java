package fr.militario.spacex.containers;

import net.minecraft.inventory.IInventory;

public class ContainerF9SecondStage extends ContainerSpaceXRocket {

	public ContainerF9SecondStage(IInventory inventoryPlayer, IInventory tileInventory) {
		super(inventoryPlayer, tileInventory);
		addSlotsNoInventory();
	}
}
