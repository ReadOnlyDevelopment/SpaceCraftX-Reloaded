package fr.militario.spacex.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.items.F9Items;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;

public class EntityDragonTrunk extends EntityTieredRocket {
	public static final String NBT_STAGES_SEPARATED = "Separated";
	public static final String NBT_GROUND_ANGLE = "GroundAngle";
	public boolean separated;
	public int ground_angle;

	public EntityDragonTrunk(World par1World) {
		super(par1World);
		setSize(2.5F, 2.0F);
	}

	public EntityDragonTrunk(World par1World, double par2, double par4, double par6, float yaw) {
		super(par1World, par2, par4, par6);
		this.rocketType = IRocketType.EnumRocketType.DEFAULT;
		this.stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		setSize(2.8F, 10.5F);
		this.rotationYaw = yaw;
		setGroundAngle(0);
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (hand != EnumHand.MAIN_HAND)
			return EnumActionResult.PASS;
		if (getPassengers().isEmpty() && (player.getHeldItemMainhand() != ItemStack.EMPTY))
			if (player.getHeldItemMainhand().getItem() == F9Items.DragonCapsuleItem) {
				mountCapsule(player);
				(FMLClientHandler.instance().getClient()).player.swingArm(EnumHand.MAIN_HAND);
			} else if (player.getHeldItemMainhand().getItem() == GCItems.wrench) {
				this.rotationYaw += 22.5F;
				(FMLClientHandler.instance().getClient()).player.swingArm(EnumHand.MAIN_HAND);
			}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean canBeRidden() {
		return true;
	}

	@Override
	public void decodePacketdata(ByteBuf buffer) {
		super.decodePacketdata(buffer);
		separateStages(buffer.readBoolean());
		setGroundAngle(buffer.readInt());
	}

	@Override
	public boolean defaultThirdPerson() {
		return false;
	}

	public void dropCapsule() {
		if (isBeingRidden() && !getLaunched()) {
			((EntitySpaceshipBase) getPassengers().get(0)).dropShipAsItem();
			getPassengers().get(0).setDead();
		}
	}

	@Override
	protected void failRocket() {
		if (!getPassengers().isEmpty()) {
			getPassengers().get(0).attackEntityFrom(DamageSourceGC.spaceshipCrash, 81.0F);
		}
		if (this.world.isRemote && (getSoundUpdater() != null)) {
			stopRocketSound();
		}
		if (!ConfigManagerCore.disableSpaceshipGrief && !getStagesSeparated()) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5.0F, true);
		} else if (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LANDING.ordinal()) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0.0F, true);
		}
		if (!getStagesSeparated()) {
			setDead();
		} else {
			setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.UNIGNITED);
		}
	}

	@Override
	public float getCameraZoom() {
		return 15.0F;
	}

	@Override
	public int getFuelTankCapacity() {
		return 0;
	}

	public int getGroundAngle() {
		return this.ground_angle;
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems) {
		ItemStack rocket;
		super.getItemsDropped(droppedItems);
		if (getStagesSeparated()) {
			rocket = new ItemStack(F9Items.DragonTrunkItem_used);
		} else {
			rocket = new ItemStack(F9Items.DragonTrunkItem);
		}
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setBoolean("Separated", getStagesSeparated());
		droppedItems.add(rocket);
		return droppedItems;
	}

	@Override
	public double getMountedYOffset() {
		return 1.95D;
	}

	@Override
	public void getNetworkedData(ArrayList<Object> list) {
		if (this.world.isRemote)
			return;
		super.getNetworkedData(list);
		list.add(Boolean.valueOf(getStagesSeparated()));
		list.add(Integer.valueOf(getGroundAngle()));
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		ItemStack rocket;
		if (getStagesSeparated()) {
			rocket = new ItemStack(F9Items.DragonTrunkItem_used);
		} else {
			rocket = new ItemStack(F9Items.DragonTrunkItem);
		}
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setBoolean("Separated", getStagesSeparated());
		return rocket;
	}

	@Override
	public int getPreLaunchWait() {
		return 0;
	}

	@Override
	public int getRocketTier() {
		return 1;
	}

	@Override
	public float getRotateOffset() {
		return -1.5F;
	}

	public boolean getStagesSeparated() {
		return this.separated;
	}

	@Override
	public boolean hasCustomName() {
		return false;
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

	public void mountCapsule(EntityPlayer player) {
		ItemStack stack = player.getHeldItemMainhand();
		EntityDragonCapsule spaceship = new EntityDragonCapsule(getEntityWorld(), this.posX, this.posY, this.posZ, IRocketType.EnumRocketType.values()[stack.getItemDamage()], this.rotationYaw);
		if (!this.world.isRemote) {
			spaceship.setPosition(spaceship.posX, spaceship.posY, spaceship.posZ);
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
	public void onReachAtmosphere() {
		if (getRidingEntity() != null) {
			dismountRidingEntity();
		}
		separateStages(true);
		setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.LANDING);
		Random rand = new Random();
		setGroundAngle(rand.nextInt(31) + 10);
		int range = 15;
		setPositionAndUpdate(((getPosition().getX() + rand.nextInt((range - -range) + 1)) - range), (getPosition().getY() - 10), ((getPosition().getZ() + rand.nextInt((range - -range) + 1)) - range));
	}

	public synchronized void synchronizedUpdate() {
		if (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal()) {
			double riding = this.getRidingEntity().motionY;
			double d = (this.timeSinceLaunch / 150.0F);

			d = Math.min(d, 1.0D);
			if (d != 0.0D) {
				this.motionY = (-d * Math.cos((this.rotationPitch - 180.0F) / 55.29577951308232D)) + riding;
			}
		}
		if (getStagesSeparated() && (this.motionY > -0.7D)) {
			this.motionY = -0.7D;
		}
		if (((this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LANDING.ordinal()) || (getLaunched() && !ConfigManagerCore.disableSpaceshipParticles && hasValidFuel())) && this.world.isRemote) {
			spawnParticles(getLaunched());
		}
		super.onUpdate();
	}

	@Override
	public void onUpdate() {
		this.synchronizedUpdate();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		separateStages(nbt.getBoolean("Separated"));
		setGroundAngle(nbt.getInteger("GroundAngle"));
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

	public void setGroundAngle(int angle) {
		this.ground_angle = angle;
	}

	@Override
	protected boolean shouldCancelExplosion() {
		return getStagesSeparated();
	}

	protected void spawnParticles(boolean launched) {
		if (!this.isDead) {
			double yv = 0.3D;
			double sinPitch = Math.sin(this.rotationPitch / 57.29577951308232D);
			double x1 = 2.0D * Math.cos(this.rotationYaw / 57.29577951308232D) * sinPitch;
			double z1 = 2.0D * Math.sin(this.rotationYaw / 57.29577951308232D) * sinPitch;
			double y1 = yv * Math.cos((this.rotationPitch - 180.0F) / 57.29577951308232D);
			double y = ((((this.prevPosY + this.posY) - this.prevPosY) + y1) - this.motionY) + 1.2D;
			double x2 = (this.posX + x1) - this.motionX;
			double z2 = (this.posZ + z1) - this.motionZ;
			EntityLivingBase riddenByEntity = (!getPassengers().isEmpty() && (getPassengers().get(0) instanceof EntityLivingBase)) ? (EntityLivingBase) getPassengers().get(0) : null;
			if (getLaunched()) {
				Vector3 motionVec = new Vector3(x1, y1, z1);
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 + 0.8D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 + 0.4D) - (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("launchFlameIdle", new Vector3((x2 - 0.8D) + (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 - 0.4D) + (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 + 0.8D) - (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 - 0.8D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("vacuumFlameIdle", new Vector3((x2 + 0.4D) - (this.rand.nextDouble() / 10.0D), y - 1.5D, (z2 - 0.4D) + (this.rand.nextDouble() / 10.0D)), motionVec, new Object[] { riddenByEntity });
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Separated", getStagesSeparated());
		nbt.setInteger("GroundAngle", getGroundAngle());
	}
}
