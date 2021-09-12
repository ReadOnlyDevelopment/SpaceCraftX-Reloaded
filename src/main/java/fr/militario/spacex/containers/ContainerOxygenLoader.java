package fr.militario.spacex.containers;

import fr.militario.spacex.tiles.TileEntityOxygenLoader;
import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerOxygenLoader extends ContainerBase {

	public ContainerOxygenLoader(InventoryPlayer playerInventory, TileEntityOxygenLoader oxygenLoader) {
		super(playerInventory, oxygenLoader);
		addSlotToContainer(new SlotSpecific(oxygenLoader, 0, 51, 55, new Class[] { IItemElectric.class }));
		addSlotToContainer(new Slot(oxygenLoader, 1, 7, 12));
		addPlayerInventorySlots(playerInventory);
	}
}
