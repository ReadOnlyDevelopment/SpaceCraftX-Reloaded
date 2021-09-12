package fr.militario.spacex.tiles;

import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.blocks.BlockFuelUnloader;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFuelUnloader extends TileElectric implements ISidedInventory, IFluidHandlerWrapper, ILandingPadAttachable, IMachineSides {

	@SideOnly(Side.CLIENT)
	public FluidTank fuelTank;
	private NonNullList<ItemStack> stacks;
	public IFuelable attachedFuelable;
	public EntitySpaceshipBase rocket;
	private boolean loadedFuelLastTick;
	private IMachineSides.MachineSidePack[] machineSides;

	public TileEntityFuelUnloader() {
		super("container.fuel.unloader.name");
		this.fuelTank = new FluidTank(1000);
		this.stacks = NonNullList.withSize(2, ItemStack.EMPTY);
		this.loadedFuelLastTick = false;
		this.storage.setMaxExtract(30.0F);
	}

	@Override
	public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canConnect(EnumFacing direction, NetworkType type) {
		if (direction == null)
			return false;
		if (type == NetworkType.POWER)
			return (direction == getElectricInputDirection());
		if (type == NetworkType.FLUID)
			return (direction == getPipeInputDirection());
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		if (getPipeInputDirection().equals(from))
			return ((this.fuelTank.getFluid() != null) && (this.fuelTank.getFluidAmount() > 0));
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		if ((slotID == 1) && (itemstack != null))
			return FluidUtil.isEmptyContainer(itemstack);
		return false;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (getPipeInputDirection().equals(from) && (resource != null))
			return this.fuelTank.drain(resource.amount, doDrain);
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		if (getPipeInputDirection().equals(from))
			return drain(from, new FluidStack(GCFluids.fluidFuel, maxDrain), doDrain);
		return null;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public IMachineSides.MachineSidePack[] getAllMachineSides() {
		if (this.machineSides == null) {
			initialiseSides();
		}
		return this.machineSides;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, facing));
		return super.getCapability(capability, facing);
	}

	@Override
	public IMachineSidesProperties getConfigurationType() {
		return BlockFuelUnloader.MACHINESIDES_RENDERTYPE;
	}

	protected NonNullList<ItemStack> getContainingItems() {
		return this.stacks;
	}

	@Override
	public TextComponentString getDisplayName() {
		return null;
	}

	@Override
	public EnumFacing getElectricInputDirection() {
		switch (getSide(IMachineSides.MachineSide.ELECTRIC_IN)) {
		case RIGHT:
			return getFront().rotateYCCW();
		case REAR:
			return getFront().getOpposite();
		case TOP:
			return EnumFacing.UP;
		case BOTTOM:
			return EnumFacing.DOWN;
		default:
			return getFront().rotateY();
		}
	}

	@Override
	public EnumFacing getFront() {
		IBlockState state = this.world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockFuelUnloader)
			return state.getValue(BlockFuelUnloader.FACING);
		return EnumFacing.NORTH;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public String getName() {
		return SpaceXUtils.translate("container.fuelunloader.name");
	}

	@Override
	public EnumFacing getPipeInputDirection() {
		switch (getSide(IMachineSides.MachineSide.PIPE_IN)) {
		default:
			return getFront().rotateYCCW();
		case REAR:
			return getFront().getOpposite();
		case TOP:
			return EnumFacing.UP;
		case BOTTOM:
			return EnumFacing.DOWN;
		case LEFT:
			break;
		}
		return getFront().rotateY();
	}

	public int getScaledFuelLevel(int i) {
		double fuelLevel = (this.fuelTank.getFluid() == null) ? 0.0D : (this.fuelTank.getFluid()).amount;
		getClass();
		return (int) ((fuelLevel * i) / 1000.0D);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1 };
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		if (getPipeInputDirection().equals(from))
			return new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		return (((slotID == 1) && (itemstack != null) && (itemstack.getItem() == AsteroidsItems.canisterLOX)) || ((slotID == 0) && ItemElectricBase.isElectricItem(itemstack.getItem())));
	}

	@Override
	public IMachineSides.MachineSide[] listConfigurableSides() {
		return new IMachineSides.MachineSide[] { IMachineSides.MachineSide.ELECTRIC_IN, IMachineSides.MachineSide.PIPE_IN };
	}

	@Override
	public IMachineSides.Face[] listDefaultFaces() {
		return new IMachineSides.Face[] { IMachineSides.Face.LEFT, IMachineSides.Face.RIGHT };
	}

	@Override
	public void onLoad() {
		clientOnLoad();
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.stacks = readStandardItemsFromNBT(par1NBTTagCompound);
		if (par1NBTTagCompound.hasKey("fuelTank")) {
			this.fuelTank.readFromNBT(par1NBTTagCompound.getCompoundTag("fuelTank"));
		}
		readMachineSidesFromNBT(par1NBTTagCompound);
	}

	@Override
	public void setupMachineSides(int length) {
		this.machineSides = new IMachineSides.MachineSidePack[length];
	}

	@Override
	public boolean shouldUseEnergy() {
		return ((this.fuelTank.getFluid() != null) && ((this.fuelTank.getFluid()).amount > 0) && !getDisabled(0) && this.loadedFuelLastTick);
	}

	@Override
	public void update() {
		super.update();
		if (!this.world.isRemote) {
			this.loadedFuelLastTick = false;
			FluidStack liquidContained = FluidUtil.getFluidContained(this.stacks.get(1));
			if (FluidUtil.isFuel(liquidContained)) {
				FluidUtil.loadFromContainer(this.fuelTank, GCFluids.fluidFuel, this.stacks, 1, liquidContained.amount);
			}
			if ((this.ticks % 100) == 0) {
				this.attachedFuelable = null;
				this.rocket = null;
				BlockVec3 thisVec = new BlockVec3(this);
				for (EnumFacing dir : EnumFacing.VALUES) {
					TileEntity pad = thisVec.getTileEntityOnSide(this.world, dir);
					if (pad instanceof TileEntityMulti) {
						TileEntity mainTile = ((TileEntityMulti) pad).getMainBlockTile();
						if (mainTile instanceof IFuelable) {
							this.attachedFuelable = (IFuelable) mainTile;
							if (this.attachedFuelable instanceof TileEntityLandingPad) {
								TileEntityLandingPad epad = (TileEntityLandingPad) mainTile;
								if (epad.getDockedEntity() instanceof EntitySpaceshipBase) {
									this.rocket = (EntitySpaceshipBase) epad.getDockedEntity();
								}
							}
							break;
						}
					} else if (pad instanceof IFuelable) {
						this.attachedFuelable = (IFuelable) pad;
						break;
					}
				}
			}
			if ((this.rocket != null) && (this.rocket.fuelTank != null) && (this.rocket.fuelTank.getFluid() != null) && ((this.rocket.fuelTank.getFluid()).amount > 0)) {
				FluidStack liquid = new FluidStack(GCFluids.fluidFuel, 2);
				if ((this.rocket != null) && this.hasEnoughEnergyToRun && !this.disabled) {
					int filled = this.fuelTank.fill(liquid, true);
					this.loadedFuelLastTick = (filled > 0);
					this.rocket.fuelTank.drain(filled, true);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		writeStandardItemsToNBT(par1NBTTagCompound, this.stacks);
		if (this.fuelTank.getFluid() != null) {
			par1NBTTagCompound.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}
		addMachineSidesToNBT(par1NBTTagCompound);
		return par1NBTTagCompound;
	}
}
