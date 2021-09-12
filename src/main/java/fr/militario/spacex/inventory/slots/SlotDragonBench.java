package fr.militario.spacex.inventory.slots;

import java.util.List;

import fr.militario.spacex.SpaceXUtils;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class SlotDragonBench extends Slot {
	private final int index;

	private final BlockPos pos;

	private final EntityPlayer player;

	public SlotDragonBench(IInventory par2IInventory, int par3, int par4, int par5, BlockPos pos, EntityPlayer player) {
		super(par2IInventory, par3, par4, par5);
		this.index = par3;
		this.pos = pos;
		this.player = player;
	}

	@Override
	public int getSlotStackLimit() {
		switch (this.index) {
		case 9:
			return 2;
		case 10:
			return 2;
		case 11:
			return 3;
		case 12:
			return 3;
		}
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack == null)
			return false;
		List<INasaWorkbenchRecipe> recipes = SpaceXUtils.getDragonRecipe();
		for (INasaWorkbenchRecipe recipe : recipes) {
			if (ItemStack.areItemsEqual(par1ItemStack, recipe.getRecipeInput().get(Integer.valueOf(this.index))))
				return true;
		}
		return false;
	}

	@Override
	public void onSlotChanged() {
		if (this.player instanceof net.minecraft.entity.player.EntityPlayerMP) {
			int dimID = GCCoreUtil.getDimensionID(this.player.world);
			GCCoreUtil.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_SPAWN_SPARK_PARTICLES, dimID, new Object[] { this.pos }), this.player.world, dimID, this.pos, 20.0D);
		}
	}
}
