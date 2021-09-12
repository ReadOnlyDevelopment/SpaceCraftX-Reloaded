package fr.militario.spacex.containers.schematics;

import javax.annotation.Nonnull;

import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.inventory.InventoryDragonTrunkBench;
import fr.militario.spacex.inventory.slots.SlotDragonTrunkBench;
import fr.militario.spacex.inventory.slots.SlotFalcon9RocketBenchResult;
import fr.militario.spacex.items.F9Items;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerSchematicDragonTrunk extends Container {
	@Nonnull
	public static ItemStack findMatchingSpaceshipRecipe(InventoryDragonTrunkBench inventoryRocketBench) {
		for (INasaWorkbenchRecipe recipe : SpaceXUtils.getDragonTrunkRecipe()) {
			if (recipe.matches(inventoryRocketBench))
				return recipe.getRecipeOutput();
		}
		return ItemStack.EMPTY;
	}

	public InventoryDragonTrunkBench craftMatrix = new InventoryDragonTrunkBench(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;
	private final int slot_limit;

	public ContainerSchematicDragonTrunk(InventoryPlayer par1InventoryPlayer, BlockPos pos) {
		this.slot_limit = 12;
		this.worldObj = par1InventoryPlayer.player.world;
		addSlotToContainer(new SlotFalcon9RocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 96));
		int var6;
		for (var6 = 0; var6 < 3; var6++) {
			addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 1 + var6, 39, -6 + (var6 * 18) + 16 + 27, pos, par1InventoryPlayer.player));
		}
		for (var6 = 0; var6 < 3; var6++) {
			addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 4 + var6, 75, -6 + (var6 * 18) + 16 + 27, pos, par1InventoryPlayer.player));
		}
		addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 7, 57, 37, pos, par1InventoryPlayer.player));
		addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 8, 57, 73, pos, par1InventoryPlayer.player));
		addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 9, 21, 55, pos, par1InventoryPlayer.player));
		addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 10, 93, 55, pos, par1InventoryPlayer.player));
		addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 11, 57, 19, pos, par1InventoryPlayer.player));
		addSlotToContainer(new SlotDragonTrunkBench(this.craftMatrix, 12, 57, 91, pos, par1InventoryPlayer.player));
		for (var6 = 0; var6 < 3; var6++) {
			for (int var7 = 0; var7 < 9; var7++) {
				addSlotToContainer(new Slot(par1InventoryPlayer, var7 + (var6 * 9) + 9, 8 + (var7 * 18), 111 + (var6 * 18) + 27));
			}
		}
		for (var6 = 0; var6 < 9; var6++) {
			addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + (var6 * 18), 196));
		}
		onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return true;
	}

	protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
		boolean flag1 = false;
		if (par1ItemStack.getCount() > 0) {
			for (int k = par2; k < par3; k++) {
				Slot slot = this.inventorySlots.get(k);
				ItemStack slotStack = slot.getStack();
				if (slotStack.isEmpty()) {
					ItemStack stackOneItem = par1ItemStack.copy();
					stackOneItem.setCount(1);
					par1ItemStack.shrink(1);
					slot.putStack(stackOneItem);
					slot.onSlotChanged();
					flag1 = true;
					break;
				}
			}
		}
		return flag1;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		if (!this.worldObj.isRemote) {
			for (int var2 = 1; var2 < (this.slot_limit + 1); var2++) {
				ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);
				if (!var3.isEmpty()) {
					par1EntityPlayer.entityDropItem(var3, 0.0F);
				}
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		this.craftResult.setInventorySlotContents(0, findMatchingSpaceshipRecipe(this.craftMatrix));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1) {
		ItemStack var2 = ItemStack.EMPTY;
		Slot var3 = this.inventorySlots.get(par1);
		if ((var3 != null) && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			if (par1 <= this.slot_limit) {
				if (!mergeItemStack(var4, this.slot_limit + 1, this.slot_limit + 36, false))
					return ItemStack.EMPTY;
				if (par1 == 0) {
					var3.onSlotChange(var4, var2);
				}
			} else if (var2.getItem() == (new ItemStack(GCItems.basicItem, 1, 8)).getItem()) {
				if (!mergeOneItem(var4, 1, 9, false))
					return ItemStack.EMPTY;
			} else if (var2.getItem() == F9Items.trunk_fin) {
				if (!mergeOneItem(var4, 9, 13, false))
					return ItemStack.EMPTY;
			} else if ((par1 >= (this.slot_limit + 1)) && (par1 < (this.slot_limit + 27))) {
				if (!mergeItemStack(var4, (this.slot_limit + 36) - 8, this.slot_limit + 36, false))
					return ItemStack.EMPTY;
			} else if ((par1 >= ((this.slot_limit + 36) - 8)) && (par1 < (this.slot_limit + 36 + 1))) {
				if (!mergeItemStack(var4, this.slot_limit, (this.slot_limit + 36) - 8, false))
					return ItemStack.EMPTY;
			}
			if (var4.getCount() == 0) {
				if (par1 == 0) {
					var3.onTake(par1EntityPlayer, var4);
				}
				var3.putStack(ItemStack.EMPTY);
				return var2;
			}
			if (var4.getCount() == var2.getCount())
				return ItemStack.EMPTY;
			var3.onTake(par1EntityPlayer, var4);
			if (par1 == 0) {
				var3.onSlotChanged();
			}
		}
		return var2;
	}
}
