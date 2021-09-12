package fr.militario.spacex.inventory.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDragonPads extends Slot {
  public SlotDragonPads(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  
  public boolean isItemValid(ItemStack par1ItemStack) {
    return false;
  }
}
