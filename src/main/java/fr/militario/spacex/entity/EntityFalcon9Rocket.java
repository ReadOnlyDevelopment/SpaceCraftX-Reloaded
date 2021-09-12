package fr.militario.spacex.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.militario.spacex.F9ConfigManager;
import fr.militario.spacex.F9Constants;
import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.advancement.Triggers;
import fr.militario.spacex.items.F9Items;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.FMLClientHandler;

public class EntityFalcon9Rocket extends EntityTieredRocket implements IOxygenable {

	public enum EnumF9LaunchPhase {
		DISABLED, ATMOSPHERICENTRY, ENTRYBURN, FALLING, LANDING, LANDED;
	}

	public static final String NBT_LAUNCHPHASE = "LaunchPhase";
	public static final String NBT_STAGES_SEPARATED = "Separated";
	public static final String NBT_YPAD = "YPad";
	public static final String NBT_LEGS_DEPLOYED = "LegsDeployed";
	public static final String NBT_GRIDSFINS_DEPLOYED = "GridFinsDeployed";
	public static final String NBT_LEGS_ANGLE = "LegsAngle";
	public static final String NBT_GRIDSFINS_ANGLE = "GridFinsAngle";
	public FluidTank oxygenTank = new FluidTank(getOxygenTankCapacity());
	public boolean gridsfins_deployed;
	public boolean legs_deployed;
	public boolean separated;
	public double YPad;
	public int F9launchPhase;
	private boolean fuse_sound_played;
	private int random_fuse = 200;
	Random rand = new Random();
	public float legs_angle;
	public float gridsfins_angle;
	double YM_atmentry = -0.75D;
	double YM_entryburn = -0.6D;
	double YM_landing = -0.4D;
	double YM_padland = -0.1D;

	public EntityFalcon9Rocket(World par1World) {
		super(par1World);
		setSize(2.8F, 10.9F);
	}

	public EntityFalcon9Rocket(World par1World, double par2, double par4, double par6, IRocketType.EnumRocketType rocketType, double YPad) {
		super(par1World, par2, par4, par6);
		this.rocketType = rocketType;
		this.stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		setSize(2.8F, 10.5F);
		this.F9launchPhase = EnumF9LaunchPhase.DISABLED.ordinal();
		setYPad(YPad);
	}

	@Override
	public int addOxygen(FluidStack liquid, boolean doFill) {
		return SpaceXUtils.fillWithOxygen(this.oxygenTank, liquid, doFill);
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (hand != EnumHand.MAIN_HAND)
			return EnumActionResult.PASS;
		if ((player.getHeldItemMainhand().getItem() == Items.FLINT_AND_STEEL) && hasValidFuel()) {
			Triggers.IGNITED.trigger(player);
			ignite();
		}
		if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
			if (getPassengers().isEmpty())
				if (player.getHeldItemMainhand().getItem() == F9Items.F9SecondStageRocketItem) {
					mountCapsule(player);
				} else if (player.getHeldItemMainhand().getItem() == GCItems.wrench) {
					this.rotationYaw += 45.0F;
				} else if ((player.getHeldItemMainhand().getItem() == F9Items.seat) && player.capabilities.isCreativeMode) {
					player.startRiding(this);
				}
		} else {
			player.openGui(SpaceX.instance, 0, player.world, getEntityId(), (int) player.posY, (int) player.posZ);
		}
		(FMLClientHandler.instance().getClient()).player.swingArm(EnumHand.MAIN_HAND);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean canBeRidden() {
		return true;
	}

	@Override
	public void decodePacketdata(ByteBuf buffer) {
		super.decodePacketdata(buffer);
		this.oxygenTank.setFluid(new FluidStack(GCFluids.fluidOxygenGas, buffer.readInt()));
		setF9LaunchPhase(getF9LaunchPhaseInt(buffer.readInt()));
		setYPad(buffer.readDouble());
		separateStages(buffer.readBoolean());
		deployLegs(buffer.readBoolean());
		deployGridsFins(buffer.readBoolean());
		setLegsAngle(buffer.readFloat());
		setGridsFinsAngle(buffer.readFloat());
	}

	public void decreaseSpeed(double speed, int factor) {
		if (hasValidFuel())
			if (this.motionY < speed) {
				this.motionY += 0.001D * factor;
			} else {
				this.motionY = speed;
			}
	}

	@Override
	public boolean defaultThirdPerson() {
		return true;
	}

	public void deployGridsFins(boolean value) {
		this.gridsfins_deployed = value;
	}

	public void deployLegs(boolean value) {
		this.legs_deployed = value;
	}

	public void dropCapsule() {
		if (isBeingRidden() && !getLaunched() && (getPassengers().get(0) instanceof EntitySpaceshipBase)) {
			((EntitySpaceshipBase) getPassengers().get(0)).dropShipAsItem();
			getPassengers().get(0).setDead();
		}
	}

	@Override
	protected void failRocket() {
		if (!getPassengers().isEmpty()) {
			getPassengers().get(0).attackEntityFrom(DamageSourceGC.spaceshipCrash, 81.0F);
		}
		if (!ConfigManagerCore.disableSpaceshipGrief) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 30.0F, true);
		}
		setDead();
	}

	@Override
	public double getMountedYOffset() {
		return 10.93D;
	}

	@Override
	public float getCameraZoom() {
		return 12.0F;
	}

	public EnumF9LaunchPhase getF9LaunchPhaseInt(int value) {
		for (EnumF9LaunchPhase phase : EnumF9LaunchPhase.values())
			if (value == phase.ordinal())
				return phase;
		return EnumF9LaunchPhase.DISABLED;
	}

	@Override
	public int getFuelTankCapacity() {
		return F9ConfigManager.rocketFuelCapacityFalcon9;
	}

	public float getGridFinsAngle() {
		return this.gridsfins_angle;
	}

	public boolean getGridFinsDeployed() {
		return this.gridsfins_deployed;
	}

	public boolean getGroundValid() {
		BlockPos ourPostion = new BlockPos(this.posX, this.posY, this.posZ);
		Material groundMaterial = this.world.getBlockState(ourPostion.down()).getMaterial();
		if ((getPosition().getY() <= (ourPostion.down().getY() + 1.4D)) && !groundMaterial.isLiquid() && (groundMaterial != Material.AIR))
			return true;
		else
			return false;
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems) {
		ItemStack rocket;
		super.getItemsDropped(droppedItems);
		if (getStagesSeparated()) {
			rocket = new ItemStack(F9Items.Falcon9RocketItem_used, 1, 0);
		} else {
			rocket = new ItemStack(F9Items.Falcon9RocketItem, 1, this.rocketType.getIndex());
		}
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
		rocket.getTagCompound().setInteger("RocketOxygen", this.oxygenTank.getFluidAmount());

		rocket.getTagCompound().setBoolean("Separated", getStagesSeparated());
		droppedItems.add(rocket);
		return droppedItems;
	}

	private void getLandingSteps() {
		int entryburn_alt = 600;
		int land_fall = 350;
		int landing_alt = 130;
		int padland_alt = 15;
		if (this.F9launchPhase == EnumF9LaunchPhase.ATMOSPHERICENTRY.ordinal()) {
			this.motionY = this.YM_atmentry;
		}
		if (this.F9launchPhase == EnumF9LaunchPhase.ENTRYBURN.ordinal()) {
			deployGridsFins(true);
			decreaseSpeed(this.YM_entryburn, 10);
		}
		if (this.F9launchPhase == EnumF9LaunchPhase.FALLING.ordinal()) {
			;
		}
		if (this.F9launchPhase == EnumF9LaunchPhase.LANDING.ordinal()) {
			decreaseSpeed(this.YM_landing, 10);
			deployLegs(true);
		}
		if (getStagesSeparated()) {
			if (getPosition().getY() >= (getYPad() + entryburn_alt)) {
				setF9LaunchPhase(EnumF9LaunchPhase.ATMOSPHERICENTRY);
			} else if ((getPosition().getY() >= (getYPad() + land_fall)) && (getPosition().getY() <= (getYPad() + entryburn_alt))) {
				setF9LaunchPhase(EnumF9LaunchPhase.ENTRYBURN);
			} else if ((getPosition().getY() >= (getYPad() + landing_alt)) && (getPosition().getY() <= (getYPad() + land_fall))) {
				setF9LaunchPhase(EnumF9LaunchPhase.FALLING);
			} else if ((getPosition().getY() >= (getYPad() + padland_alt)) && (getPosition().getY() <= (getYPad() + landing_alt))) {
				setF9LaunchPhase(EnumF9LaunchPhase.LANDING);
			} else if ((getPosition().getY() >= (getYPad() + 1.0D)) && (getPosition().getY() <= (getYPad() + padland_alt))) {
				decreaseSpeed(this.YM_padland, 15);
			} else if (getGroundValid() && (this.F9launchPhase != EnumF9LaunchPhase.LANDED.ordinal())) {
				setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.UNIGNITED);
				setF9LaunchPhase(EnumF9LaunchPhase.LANDED);
				if (this.world.isRemote && (getSoundUpdater() != null)) {
					stopRocketSound();
				}
				if (hasValidFuel()) {
					removeFuel(this.fuelTank.getCapacity() / 2);
					removeOxygen(this.oxygenTank.getCapacity() / 2);
				}
			}
		} else {
			this.motionY = this.YM_atmentry;
		}
	}

	public float getLegsAngle() {
		return this.legs_angle;
	}

	public boolean getLegsDeployed() {
		return this.legs_deployed;
	}

	@Override
	public Vec3d getLookVec() {
		return getLook(1.0F);
	}

	public int getMaxOxygen() {
		return this.oxygenTank.getCapacity();
	}

	@Override
	public void getNetworkedData(ArrayList<Object> list) {
		if (this.world.isRemote)
			return;
		super.getNetworkedData(list);
		list.add(Integer.valueOf(this.oxygenTank.getFluidAmount()));
		list.add(Integer.valueOf(this.F9launchPhase));
		list.add(Double.valueOf(getYPad()));
		list.add(Boolean.valueOf(getStagesSeparated()));
		list.add(Boolean.valueOf(getLegsDeployed()));
		list.add(Boolean.valueOf(getGridFinsDeployed()));
		list.add(Float.valueOf(getLegsAngle()));
		list.add(Float.valueOf(getGridFinsAngle()));
	}

	public int getOxygenTankCapacity() {
		return F9ConfigManager.rocketOxygenCapacityFalcon9;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		ItemStack rocket;
		if (getStagesSeparated()) {
			rocket = new ItemStack(F9Items.Falcon9RocketItem_used, 1, 0);
		} else {
			rocket = new ItemStack(F9Items.Falcon9RocketItem, 1, this.rocketType.getIndex());
		}
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
		rocket.getTagCompound().setInteger("RocketOxygen", this.oxygenTank.getFluidAmount());

		rocket.getTagCompound().setBoolean("Separated", getStagesSeparated());
		return rocket;
	}

	@Override
	public int getPreLaunchWait() {
		return 200;
	}

	@Override
	public int getRocketTier() {
		return F9ConfigManager.rocketTierFalcon9;
	}

	@Override
	public float getRotateOffset() {
		return -1.5F;
	}

//	@Override
//	public float getRenderOffsetY() {
//		return -0.15F;
//	}

	public int getScaledOxygenLevel(int scale) {
		if (getFuelTankCapacity() <= 0)
			return 0;
		return (this.oxygenTank.getFluidAmount() * scale) / getOxygenTankCapacity() / F9ConfigManager.rocketOxygenFactor;
	}

	public boolean getStagesSeparated() {
		return this.separated;
	}

	public double getYPad() {
		return this.YPad;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public boolean hasValidFuel() {
		return ((this.fuelTank.getFluidAmount() > 0) && (this.oxygenTank.getFluidAmount() > 0));
	}

	public boolean hasValidOxygen() {
		return (this.oxygenTank.getFluidAmount() > 0);
	}

	@Override
	public boolean hitByEntity(Entity entityIn) {
		if (entityIn instanceof EntityPlayerMP) {
			EntityPlayerMP playerIn = (EntityPlayerMP) entityIn;
			if (playerIn.capabilities.isCreativeMode) {
				dropCapsule();
			}
		}
		return super.hitByEntity(entityIn);
	}

	@Override
	public boolean isDockValid(IFuelDock dock) {
		return dock instanceof micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	public void landRocket() {
		if (!getStagesSeparated()) {
			separateStages(true);
			setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.LANDING);
			setF9LaunchPhase(EnumF9LaunchPhase.ATMOSPHERICENTRY);
		}
	}

	public void mountCapsule(EntityPlayer player) {
		ItemStack stack = player.getHeldItemMainhand();
		EntityF9SecondStage spaceship = new EntityF9SecondStage(getEntityWorld(), this.posX, this.posY, this.posZ, IRocketType.EnumRocketType.values()[stack.getItemDamage()]);
		if (!this.world.isRemote) {
			spaceship.setPosition(spaceship.posX, spaceship.posY + spaceship.getOnPadYOffset() + 11.0D, spaceship.posZ);
			getEntityWorld().spawnEntity(spaceship);
			spaceship.startRiding(this);
		}
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("RocketFuel")) {
			spaceship.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, stack.getTagCompound().getInteger("RocketFuel")), true);
		}
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("RocketOxygen")) {
			spaceship.oxygenTank.fill(new FluidStack(GCFluids.fluidOxygenGas, stack.getTagCompound().getInteger("RocketOxygen")), true);
		}
		if (spaceship.rocketType.getPreFueled()) {
			spaceship.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, spaceship.getMaxFuel()), true);
			spaceship.oxygenTank.fill(new FluidStack(GCFluids.fluidOxygenGas, spaceship.getMaxOxygen()), true);
		}
		if (!player.capabilities.isCreativeMode) {
			player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
		}
		this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.AMBIENT, 3.0F, 0.8F, true);
	}

	@Override
	public void onLaunch() {
		if (!getPassengers().isEmpty() && (getPassengers().get(0) instanceof EntityF9SecondStage) && !getPassengers().get(0).getPassengers().isEmpty() && (getPassengers().get(0).getPassengers().get(0) instanceof EntityDragonTrunk)
				&& !getPassengers().get(0).getPassengers().get(0).getPassengers().isEmpty() && (getPassengers().get(0).getPassengers().get(0).getPassengers().get(0) instanceof EntityDragonCapsule)) {
			EntityDragonCapsule dragon = (EntityDragonCapsule) getPassengers().get(0).getPassengers().get(0).getPassengers().get(0);
			dragon.setLandingPadsItem();
		}
		super.onLaunch();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		int i = 0;
		this.rumble = 0.0F;
		if (getLaunched() && getStagesSeparated() && (this.F9launchPhase == EnumF9LaunchPhase.LANDED.ordinal())) {
			setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.UNIGNITED);
			this.timeUntilLaunch = 0;
		}
		if (this.posY > ((this.world.provider instanceof IExitHeight) ? ((IExitHeight) this.world.provider).getYCoordinateToTeleport() : 1000.0D)) {
			landRocket();
		}
		getLandingSteps();
		if (this.timeUntilLaunch >= 100) {
			i = Math.abs(this.timeUntilLaunch / 100);
		} else {
			i = 1;
		}
		if (getGridFinsDeployed()) {
			if (getGridFinsAngle() < 90.0F) {
				setGridsFinsAngle(getGridFinsAngle() + 2.5F);
			}
		} else if (getGridFinsAngle() != 0.0F) {
			setGridsFinsAngle(0.0F);
		}
		if (getLegsDeployed()) {
			if (getLegsAngle() < 117.0F) {
				setLegsAngle(getLegsAngle() + 1.5F);
			}
		} else if (getLegsAngle() != 0.0F) {
			setLegsAngle(0.0F);
		}
		if (((this.F9launchPhase == EnumF9LaunchPhase.LANDING.ordinal()) || (this.F9launchPhase == EnumF9LaunchPhase.ENTRYBURN.ordinal()) || ((this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.IGNITED.ordinal()) && (this.rand.nextInt(i) == 0))
				|| (getLaunched() && !getStagesSeparated())) && !ConfigManagerCore.disableSpaceshipParticles && hasValidFuel() && this.world.isRemote)
			if ((this.timeUntilLaunch >= (getPreLaunchWait() - 10)) && (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.IGNITED.ordinal())) {
				spawnBoreSmokes();
			} else {
				spawnParticles(getLaunched());
			}
		if (!ConfigManagerCore.disableSpaceshipParticles && (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.UNIGNITED.ordinal()) && (this.F9launchPhase == EnumF9LaunchPhase.DISABLED.ordinal())
				&& (this.oxygenTank.getFluidAmount() >= (this.oxygenTank.getCapacity() - 200)))
			if (this.random_fuse <= 0) {
				if (!this.fuse_sound_played) {
					this.world.playSound(this.posX, this.posY + 8.0D, this.posZ, new SoundEvent(new ResourceLocation(F9Constants.MODID, "gaz_fuse")), SoundCategory.AMBIENT, 2.0F, 1.0F, true);
					this.fuse_sound_played = true;
				}
				if (this.world.isRemote) {
					spawnFuseParticles();
				}
				this.random_fuse--;
				if (this.random_fuse <= -40) {
					this.random_fuse = this.rand.nextInt(251) + 50;
					this.fuse_sound_played = false;
				}
			} else {
				this.random_fuse--;
			}
		if (this.F9launchPhase == EnumF9LaunchPhase.DISABLED.ordinal())
			if ((this.launchPhase >= EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal()) && hasValidFuel()) {
				if (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal()) {
					double d = (this.timeSinceLaunch / 150.0F);
					d = Math.min(d, 1.0D);
					if (d != 0.0D) {
						this.motionY = (-d * Math.cos((this.rotationPitch - 180.0F) / 57.29577951308232D));
					}
				}
				double multiplier = 1.0D;
				if (this.world.provider instanceof IGalacticraftWorldProvider) {
					multiplier = ((IGalacticraftWorldProvider) this.world.provider).getFuelUsageMultiplier();
					if (multiplier <= 0.0D) {
						multiplier = 1.0D;
					}
				}
				if ((this.timeSinceLaunch % MathHelper.floor((3.0D * 1.0D) / multiplier)) == 0.0F) {
					removeFuel(1);
					removeOxygen(1);
					if (!hasValidFuel()) {
						stopRocketSound();
					}
				}
			} else if (!hasValidFuel() && getLaunched() && !this.world.isRemote && ((Math.abs(Math.sin((this.timeSinceLaunch / 1000.0F))) / 10.0D) != 0.0D)) {
				this.motionY -= Math.abs(Math.sin((this.timeSinceLaunch / 1000.0F))) / 20.0D;
			}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("oxygenTank")) {
			this.oxygenTank.readFromNBT(nbt.getCompoundTag("oxygenTank"));
		}
		setF9LaunchPhase(getF9LaunchPhaseInt(nbt.getInteger("LaunchPhase")));
		separateStages(nbt.getBoolean("Separated"));
		setYPad(nbt.getDouble("YPad"));
		deployLegs(nbt.getBoolean("LegsDeployed"));
		deployGridsFins(nbt.getBoolean("GridFinsDeployed"));
		setLegsAngle(nbt.getFloat("LegsAngle"));
		setGridsFinsAngle(nbt.getFloat("GridFinsAngle"));
	}

	@Override
	public FluidStack removeOxygen(int amount) {
		return this.oxygenTank.drain(amount * F9ConfigManager.rocketOxygenFactor, true);
	}

	@Override
	protected void removePassenger(Entity passenger) {
		if (!passenger.isDead) {
			dropCapsule();
		}
		super.removePassenger(passenger);
	}

	public void separateStages(boolean value) {
		this.separated = value;
	}

	public void setF9LaunchPhase(EnumF9LaunchPhase step) {
		this.F9launchPhase = step.ordinal();
	}

	public void setGridsFinsAngle(float angle) {
		this.gridsfins_angle = angle;
	}

	public void setLegsAngle(float angle) {
		this.legs_angle = angle;
	}

	public void setYPad(double pos) {
		this.YPad = pos;
	}

	@Override
	protected boolean shouldCancelExplosion() {
		if (this.F9launchPhase > EnumF9LaunchPhase.LANDING.ordinal())
			return true;
		return super.shouldCancelExplosion();
	}

	public void spawnBoreSmokes() {
		if (!this.isDead) {
			double sinPitch = Math.sin(this.rotationPitch / 57.29577951308232D);
			double x1 = 2.0D * Math.cos(this.rotationYaw / 57.29577951308232D) * sinPitch;
			double z1 = 2.0D * Math.sin(this.rotationYaw / 57.29577951308232D) * sinPitch;
			double y1 = 2.0D * Math.cos((this.rotationPitch - 180.0F) / 57.29577951308232D);
			double y = ((((this.prevPosY + this.posY) - this.prevPosY) + y1) - this.motionY) + 1.2D;
			double x2 = (this.posX + x1) - this.motionX;
			double z2 = (this.posZ + z1) - this.motionZ;
			EntityLivingBase riddenByEntity = (!getPassengers().isEmpty() && (getPassengers().get(0) instanceof EntityLivingBase)) ? (EntityLivingBase) getPassengers().get(0) : null;
			if (!getLaunched()) {
				SpaceX.proxy.spawnParticle("greenSmokeIdle", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), new Vector3(x1 + 0.2D, y1 - 0.1D, z1 + 0.2D), new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("greenSmokeIdle", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), new Vector3(x1 - 0.2D, y1 - 0.1D, z1 + 0.2D), new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("greenSmokeIdle", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), new Vector3(x1 - 0.2D, y1 - 0.1D, z1 - 0.2D), new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("greenSmokeIdle", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), new Vector3(x1 + 0.2D, y1 - 0.1D, z1 - 0.2D), new Object[] { riddenByEntity });
			}
		}
	}

	protected void spawnFuseParticles() {
		if (!this.isDead) {
			Vec3d vecs = getLookVec().rotateYaw(170.0F);
			double x1 = this.posX + vecs.x;
			double y1 = this.posY + vecs.y + 8.7D;
			double z1 = this.posZ + vecs.z;
			double x2 = (vecs.scale(0.2D)).x;
			double z2 = (vecs.scale(0.2D)).z;
			Vector3 motionVec = new Vector3(x2, 0.01D, z2);
			SpaceX.proxy.spawnParticle("condensation", new Vector3(x1, y1, z1), motionVec, new Object[0]);
		}
	}

	protected void spawnParticles(boolean launched) {
		if (!this.isDead) {
			int yv = 2;
			if (this.F9launchPhase == EnumF9LaunchPhase.ENTRYBURN.ordinal()) {
				yv = 1;
			}
			double sinPitch = Math.sin(this.rotationPitch / 57.29577951308232D);
			double x1 = 2.0D * Math.cos(this.rotationYaw / 57.29577951308232D) * sinPitch;
			double z1 = 2.0D * Math.sin(this.rotationYaw / 57.29577951308232D) * sinPitch;
			double y1 = yv * Math.cos((this.rotationPitch - 180.0F) / 57.29577951308232D);
			double y = ((((this.prevPosY + this.posY) - this.prevPosY) + y1) - this.motionY) + 1.2D;
			double x2 = (this.posX + x1) - this.motionX;
			double z2 = (this.posZ + z1) - this.motionZ;
			EntityLivingBase riddenByEntity = (!getPassengers().isEmpty() && (getPassengers().get(0) instanceof EntityLivingBase)) ? (EntityLivingBase) getPassengers().get(0) : null;
			Vector3 motionVec = new Vector3(x1, y1, z1);
			if (getLaunched()) {
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("rocketFlame", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y - 0.8D, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
			} else {
				GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), new Vector3(x1 + 0.7D, y1 - 1.0D, z1 + 0.7D), new Object[] { riddenByEntity });
				GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), new Vector3(x1 - 0.7D, y1 - 1.0D, z1 + 0.7D), new Object[] { riddenByEntity });
				GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), new Vector3(x1 - 0.7D, y1 - 1.0D, z1 - 0.7D), new Object[] { riddenByEntity });
				GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), new Vector3(x1 + 0.7D, y1 - 1.0D, z1 - 0.7D), new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchSmoke", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		if (this.oxygenTank.getFluid() != null) {
			nbt.setTag("oxygenTank", this.oxygenTank.writeToNBT(new NBTTagCompound()));
		}
		nbt.setInteger("LaunchPhase", this.F9launchPhase);
		nbt.setBoolean("Separated", getStagesSeparated());
		nbt.setDouble("YPad", getYPad());
		nbt.setBoolean("LegsDeployed", getLegsDeployed());
		nbt.setBoolean("GridFinsDeployed", getGridFinsDeployed());
		nbt.setFloat("LegsAngle", getLegsAngle());
		nbt.setFloat("GridFinsAngle", getGridFinsAngle());
	}
}
