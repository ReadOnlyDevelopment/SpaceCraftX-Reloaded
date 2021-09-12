package fr.militario.spacex.tiles;

import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.blocks.BlockOxygenUnloader;
import fr.militario.spacex.entity.EntityDragonCapsule;
import fr.militario.spacex.entity.EntityF9SecondStage;
import fr.militario.spacex.entity.EntityFalcon9Rocket;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
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
import net.minecraft.inventory.ItemStackHelper;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityOxygenUnloader extends TileElectric implements ISidedInventory, IFluidHandlerWrapper, ILandingPadAttachable, IMachineSides {
	public static boolean isOxygen(FluidStack fluid) {
		return ((fluid != null) && SpaceXUtils.testFuel(FluidRegistry.getFluidName(fluid)));
	}

	@SideOnly(Side.CLIENT)
	public FluidTank oxygenTank;
	private NonNullList<ItemStack> stacks;
	public IFuelable attachedOxygenable;
	public EntitySpaceshipBase rocket;
	private boolean loadedFuelLastTick;
	private IMachineSides.MachineSidePack[] machineSides;

	public TileEntityOxygenUnloader() {
		super("container.oxygen.unloader.name");
		this.oxygenTank = new FluidTank(1000);
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
		if (getPipeInputDirection().equals(from) && ((fluid == null) || fluid.getName().equals("oxygen")))
			return ((this.oxygenTank.getFluid() != null) && (this.oxygenTank.getFluidAmount() > 0));
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
			return this.oxygenTank.drain(resource.amount, doDrain);
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		if (getPipeInputDirection().equals(from))
			return drain(from, new FluidStack(GCFluids.fluidOxygenGas, maxDrain), doDrain);
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
		return BlockOxygenUnloader.MACHINESIDES_RENDERTYPE;
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
			break;
		}
		return getFront().rotateY();
	}

	@Override
	public EnumFacing getFront() {
		IBlockState state = this.world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockOxygenUnloader)
			return state.getValue(BlockOxygenUnloader.FACING);
		return EnumFacing.NORTH;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public String getName() {
		return SpaceXUtils.translate("container.oxygenunloader.name");
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
		double fuelLevel = (this.oxygenTank.getFluid() == null) ? 0.0D : (this.oxygenTank.getFluid()).amount;
		return (int) ((fuelLevel * i) / 1000.0D);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1 };
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		if (getPipeInputDirection().equals(from))
			return new FluidTankInfo[] { new FluidTankInfo(this.oxygenTank) };
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

	public boolean produceOxygen(EnumFacing outputDirection) {
		int provide = getPipeInputDirection().getIndex();
		if (provide > 0) {
			TileEntity outputTile = (new BlockVec3(this)).getTileEntityOnSide(this.world, outputDirection);
			FluidNetwork outputNetwork = NetworkHelper.getFluidNetworkFromTile(outputTile, outputDirection);
			if (outputNetwork != null) {
				int gasRequested = outputNetwork.getRequest();
				if (gasRequested > 0) {
					outputNetwork.emitToBuffer(new FluidStack(GCFluids.fluidOxygenGas, Math.min(gasRequested, provide)), true);
					return true;
				}
			} else if (outputTile instanceof IOxygenReceiver) {
				float requestedOxygen = ((IOxygenReceiver) outputTile).getOxygenRequest(outputDirection.getOpposite());
				if (requestedOxygen > 0.0F) {
					((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), provide, true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NonNullList<ItemStack> stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(par1NBTTagCompound, stacks);
		this.stacks = readStandardItemsFromNBT(par1NBTTagCompound);
		if (par1NBTTagCompound.hasKey("oxygenTank")) {
			this.oxygenTank.readFromNBT(par1NBTTagCompound.getCompoundTag("oxygenTank"));
		}
		readMachineSidesFromNBT(par1NBTTagCompound);
	}

	@Override
	public void setupMachineSides(int length) {
		this.machineSides = new IMachineSides.MachineSidePack[length];
	}

	@Override
	public boolean shouldUseEnergy() {
		return ((this.oxygenTank.getFluid() != null) && ((this.oxygenTank.getFluid()).amount > 0) && !getDisabled(0) && this.loadedFuelLastTick);
	}

	@Override
	public void update() {
		super.update();
		if (!this.world.isRemote) {
			this.loadedFuelLastTick = false;
			FluidStack liquidContained = FluidUtil.getFluidContained(this.stacks.get(1));
			if (isOxygen(liquidContained)) {
				FluidUtil.loadFromContainer(this.oxygenTank, GCFluids.fluidOxygenGas, this.stacks, 1, liquidContained.amount);
			}
			if ((this.ticks % 100) == 0) {
				this.attachedOxygenable = null;
				BlockVec3 thisVec = new BlockVec3(this);
				for (EnumFacing dir : EnumFacing.VALUES) {
					TileEntity pad = thisVec.getTileEntityOnSide(this.world, dir);
					if (pad instanceof TileEntityMulti) {
						TileEntity mainTile = ((TileEntityMulti) pad).getMainBlockTile();
						if (mainTile instanceof IFuelable) {
							this.attachedOxygenable = (IFuelable) mainTile;
							if (this.attachedOxygenable instanceof TileEntityLandingPad) {
								TileEntityLandingPad epad = (TileEntityLandingPad) mainTile;
								if (epad.getDockedEntity() instanceof EntitySpaceshipBase) {
									this.rocket = (EntitySpaceshipBase) epad.getDockedEntity();
								}
							}
							break;
						}
					} else if (pad instanceof IFuelable) {
						this.attachedOxygenable = (IFuelable) pad;
						break;
					}
				}
			}
			FluidStack liquid = new FluidStack(GCFluids.fluidOxygenGas, 2);
			if ((this.rocket != null) && this.hasEnoughEnergyToRun && !this.disabled)
				if (this.rocket instanceof EntityFalcon9Rocket) {
					if ((((EntityFalcon9Rocket) this.rocket).oxygenTank != null) && (((EntityFalcon9Rocket) this.rocket).oxygenTank.getFluid() != null) && ((((EntityFalcon9Rocket) this.rocket).oxygenTank.getFluid()).amount > 0)) {
						int filled = SpaceXUtils.fillWithOxygen(this.oxygenTank, liquid, true);
						this.loadedFuelLastTick = (filled > 0);
						((EntityFalcon9Rocket) this.rocket).oxygenTank.drain(filled, true);
					}
				} else if (this.rocket instanceof EntityF9SecondStage) {
					if ((((EntityF9SecondStage) this.rocket).oxygenTank != null) && (((EntityF9SecondStage) this.rocket).oxygenTank.getFluid() != null) && ((((EntityF9SecondStage) this.rocket).oxygenTank.getFluid()).amount > 0)) {
						int filled = SpaceXUtils.fillWithOxygen(this.oxygenTank, liquid, true);
						this.loadedFuelLastTick = (filled > 0);
						((EntityF9SecondStage) this.rocket).oxygenTank.drain(filled, true);
					}
				} else if ((this.rocket instanceof EntityDragonCapsule) && (((EntityDragonCapsule) this.rocket).oxygenTank != null) && (((EntityDragonCapsule) this.rocket).oxygenTank.getFluid() != null)
						&& ((((EntityDragonCapsule) this.rocket).oxygenTank.getFluid()).amount > 0)) {
					int filled = SpaceXUtils.fillWithOxygen(this.oxygenTank, liquid, true);
					this.loadedFuelLastTick = (filled > 0);
					((EntityDragonCapsule) this.rocket).oxygenTank.drain(filled, true);
				}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		ItemStackHelper.saveAllItems(par1NBTTagCompound, this.stacks);
		if (this.oxygenTank.getFluid() != null) {
			par1NBTTagCompound.setTag("oxygenTank", this.oxygenTank.writeToNBT(new NBTTagCompound()));
		}
		addMachineSidesToNBT(par1NBTTagCompound);
		return par1NBTTagCompound;
	}
}
