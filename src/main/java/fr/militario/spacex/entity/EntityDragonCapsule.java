package fr.militario.spacex.entity;

import java.util.ArrayList;
import java.util.List;

import fr.militario.spacex.F9ConfigManager;
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
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.FMLClientHandler;

public class EntityDragonCapsule extends EntityTieredRocket implements IOxygenable {

	public enum EnumF9LaunchPhase {
		DISABLED, ATMOSPHERICENTRY, ENTRYBURN, LANDING, LANDED;
	}

	public static final String NBT_LAUNCHPHASE = "LaunchPhase";
	public static final String NBT_YPAD = "YPad";
	public static final String NBT_STAGES_SEPARATED = "Separated";
	public static final String NBT_HAS_FLY = "HasFly";
	public FluidTank oxygenTank = new FluidTank(getOxygenTankCapacity());
	public boolean legs_deployed;
	public boolean separated;
	public int F9launchPhase;
	public double YPad;
	public boolean hasFly;
	double YM_atmentry = -0.75D;
	double YM_entryburn = -0.4D;

	double YM_landing = -0.15D;

	public EntityDragonCapsule(World par1World) {
		super(par1World);
		setSize(2.8F, 3.5F);
	}

	public EntityDragonCapsule(World par1World, double par2, double par4, double par6, IRocketType.EnumRocketType rocketType, float yaw) {
		super(par1World, par2, par4, par6);
		this.rocketType = rocketType;
		this.stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		setSize(2.8F, 10.5F);
		this.F9launchPhase = EnumF9LaunchPhase.DISABLED.ordinal();
		setYPad(0.0D);
		this.rotationYaw = yaw;
	}

	@Override
	public int addOxygen(FluidStack liquid, boolean doFill) {
		return SpaceXUtils.fillWithOxygen(this.oxygenTank, liquid, doFill);
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (hand != EnumHand.MAIN_HAND)
			return EnumActionResult.PASS;
		if (getPassengers().isEmpty()) {
			if (getPassengers().isEmpty()) {
				if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
					if (player.getHeldItemMainhand().getItem() == GCItems.wrench) {
						this.rotationYaw += 45.0F;
					} else {
						rideAndCheckAdvancement(player);
					}
				} else {
					rideAndCheckAdvancement(player);
				}
			} else {
				player.openGui(SpaceX.instance, 2, player.world, getEntityId(), (int) player.posY, (int) player.posZ);
			}
		} else {
			player.openGui(SpaceX.instance, 2, player.world, getEntityId(), (int) player.posY, (int) player.posZ);
		}
		(FMLClientHandler.instance().getClient()).player.swingArm(EnumHand.MAIN_HAND);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean canBeRidden() {
		return true;
	}

	public void checkBlockValidity(BlockPos pos) {
		Material blockMaterial = this.world.getBlockState(pos).getMaterial();
		if (!blockMaterial.isLiquid() && (blockMaterial != Material.AIR) && (blockMaterial != Material.PLANTS) && (blockMaterial != Material.VINE)) {
			setYPad((pos.getY() + 1));
		}
	}

	@Override
	public void decodePacketdata(ByteBuf buffer) {
		super.decodePacketdata(buffer);
		this.oxygenTank.setFluid(new FluidStack(GCFluids.fluidOxygenGas, buffer.readInt()));
		setF9LaunchPhase(getF9LaunchPhaseInt(buffer.readInt()));
		setYPad(buffer.readDouble());
		separateStages(buffer.readBoolean());
		setHasFly(buffer.readBoolean());
	}

	public void decreaseSpeed(double speed, int factor) {
		if (hasValidFuel())
			if (this.motionY < speed) {
				this.motionY += 0.0001D * factor;
			} else {
				this.motionY = speed;
			}
	}

	@Override
	public boolean defaultThirdPerson() {
		return false;
	}

	public void detectFloor() {
		int x = (int) this.posX + 1;
		int z = (int) this.posZ - 1;
		for (int a = 1; a <= 3; a++) {
			for (int b = 1; b <= 3; b++) {
				checkBlockValidity(new BlockPos(x, this.posY - 20.0D, z));
				z++;
			}
			x--;
			z = (int) this.posZ - 1;
		}
	}

	@Override
	public float getCameraZoom() {
		return 17.0F;
	}

	public EnumF9LaunchPhase getF9LaunchPhaseInt(int value) {
		for (EnumF9LaunchPhase phase : EnumF9LaunchPhase.values()) {
			if (value == phase.ordinal())
				return phase;
		}
		return EnumF9LaunchPhase.DISABLED;
	}

	@Override
	public int getFuelTankCapacity() {
		return F9ConfigManager.rocketFuelCapacityDragon;
	}

	public boolean getHasFlyed() {
		return this.hasFly;
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems) {
		ItemStack rocket;
		super.getItemsDropped(droppedItems);
		if (getStagesSeparated()) {
			rocket = new ItemStack(F9Items.DragonCapsuleItem_used, 1, 0);
		} else {
			rocket = new ItemStack(F9Items.DragonCapsuleItem, 1, this.rocketType.getIndex());
		}
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
		rocket.getTagCompound().setInteger("RocketOxygen", this.oxygenTank.getFluidAmount());
		rocket.getTagCompound().setBoolean("Separated", getStagesSeparated());
		droppedItems.add(rocket);
		return droppedItems;
	}

	private void getLandingSteps() {
		int entryburn_alt = 270;
		int padland_alt = 10;
		if (this.F9launchPhase == EnumF9LaunchPhase.ATMOSPHERICENTRY.ordinal()) {
			this.motionY = this.YM_atmentry;
		}
		if (this.F9launchPhase == EnumF9LaunchPhase.ENTRYBURN.ordinal()) {
			decreaseSpeed(this.YM_entryburn, 10);
		}
		if (this.F9launchPhase == EnumF9LaunchPhase.LANDING.ordinal()) {
			decreaseSpeed(this.YM_landing, 10);
		}
		if (getStagesSeparated() && (this.F9launchPhase != EnumF9LaunchPhase.LANDED.ordinal())) {
			if (getPosition().getY() > entryburn_alt) {
				setF9LaunchPhase(EnumF9LaunchPhase.ATMOSPHERICENTRY);
			} else if ((getPosition().getY() >= (getYPad() + padland_alt)) && (getPosition().getY() <= entryburn_alt)) {
				setF9LaunchPhase(EnumF9LaunchPhase.ENTRYBURN);
			} else if ((getPosition().getY() >= (getYPad() + 1.0D)) && (getPosition().getY() <= (getYPad() + padland_alt))) {
				setF9LaunchPhase(EnumF9LaunchPhase.LANDING);
			} else if ((getPosition().getY() <= getYPad()) && (this.F9launchPhase != EnumF9LaunchPhase.LANDED.ordinal())) {
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

	@Override
	public Vec3d getLookVec() {
		return getLook(1.0F);
	}

	public int getMaxOxygen() {
		return this.oxygenTank.getCapacity();
	}

	@Override
	public double getMountedYOffset() {
		return 0.5D;
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
		list.add(Boolean.valueOf(getHasFlyed()));
	}

	public int getOxygenTankCapacity() {
		return F9ConfigManager.rocketOxygenCapacityDragon;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		ItemStack rocket;
		if (getStagesSeparated()) {
			rocket = new ItemStack(F9Items.DragonCapsuleItem_used, 1, 0);
		} else {
			rocket = new ItemStack(F9Items.DragonCapsuleItem, 1, this.rocketType.getIndex());
		}
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
		rocket.getTagCompound().setInteger("RocketOxygen", this.oxygenTank.getFluidAmount());
		rocket.getTagCompound().setBoolean("Separated", getStagesSeparated());
		return rocket;
	}

	@Override
	public int getPreLaunchWait() {
		return 20;
	}

	@Override
	public int getRocketTier() {
		return F9ConfigManager.rocketTierDragon;
	}

	@Override
	public float getRotateOffset() {
		return -1.5F;
	}

	public int getScaledOxygenLevel(int scale) {
		if (getFuelTankCapacity() <= 0)
			return 0;
		return (this.oxygenTank.getFluidAmount() * scale) / getOxygenTankCapacity() / F9ConfigManager.rocketOxygenFactor;
	}

	@Override
	public int getSizeInventory() {
		return 28;
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
	public boolean isDockValid(IFuelDock dock) {
		return dock instanceof micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public void onLaunch() {
		if (getRidingEntity() != null) {
			setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.UNIGNITED);
			this.timeUntilLaunch = 0;
			if ((getRidingEntity() != null) && (getRidingEntity() instanceof EntityDragonTrunk) && (getRidingEntity().getRidingEntity() != null) && (getRidingEntity().getRidingEntity() instanceof EntityF9SecondStage)
					&& (getRidingEntity().getRidingEntity().getRidingEntity() != null) && (getRidingEntity().getRidingEntity().getRidingEntity() instanceof EntityFalcon9Rocket)) {
				EntityFalcon9Rocket f9 = (EntityFalcon9Rocket) getRidingEntity().getRidingEntity().getRidingEntity();
				if (f9.getStagesSeparated()) {
					if (!this.world.isRemote && (getPassengers().get(0) instanceof EntityPlayerMP)) {
						getPassengers().get(0).sendMessage(new TextComponentString(SpaceXUtils.translate("gui.rocket.warning.f9.used")));
					}
				} else if (f9.hasValidFuel()) {
					f9.igniteCheckingCooldown();
				} else if (!this.world.isRemote && (getPassengers().get(0) instanceof EntityPlayerMP)) {
					getPassengers().get(0).sendMessage(new TextComponentString(GCCoreUtil.translate("gui.rocket.warning.nofuel")));
				}
			}
		} else if (getStagesSeparated()) {
			if (this.world.provider instanceof net.minecraft.world.WorldProviderSurface) {
				setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.UNIGNITED);
				this.timeUntilLaunch = 0;
				if (!this.world.isRemote && (getPassengers().get(0) instanceof EntityPlayerMP)) {
					getPassengers().get(0).sendMessage(new TextComponentString(SpaceXUtils.translate("gui.rocket.warning.dragon.used")));
				}
			} else {
				setF9LaunchPhase(EnumF9LaunchPhase.DISABLED);
				separateStages(false);
			}
		} else {
			setLandingPadsItem();
			super.onLaunch();
		}
	}

	@Override
	public void onTeleport(EntityPlayerMP player) {
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);
		if (playerBase != null) {
			GCPlayerStats stats = GCPlayerStats.get(player);
			stats.setRocketType(this.rocketType.getIndex());
			stats.setRocketItem(F9Items.DragonCapsuleItem);
			stats.setFuelLevel(this.fuelTank.getFluidAmount());
			if ((this.stacks == null) || this.stacks.isEmpty()) {
				stats.setRocketStacks(NonNullList.withSize(getInventoryStackLimit(), ItemStack.EMPTY));
			} else {
				if (this.stacks.get(26) == ItemStack.EMPTY) {
					this.stacks.set(26, new ItemStack(Blocks.BARRIER));
				}
				if (this.stacks.get(27) == ItemStack.EMPTY) {
					this.stacks.set(27, new ItemStack(Blocks.BARRIER));
				}
				stats.setRocketStacks(this.stacks);
			}
		}
	}

	public synchronized void synchronizedUpdate() {
		super.onUpdate();
		int i = 0;
//		if (getRidingEntity() != null) {
//			if ((getRidingEntity()).rotationYaw != this.rotationYaw) {
//				(getRidingEntity()).rotationYaw = this.rotationYaw;
//			}
//			if (getRidingEntity().getRidingEntity() != null) {
//				if ((getRidingEntity().getRidingEntity()).rotationYaw != this.rotationYaw) {
//					(getRidingEntity().getRidingEntity()).rotationYaw = this.rotationYaw;
//				}
//				if ((getRidingEntity().getRidingEntity().getRidingEntity() != null) && ((getRidingEntity().getRidingEntity().getRidingEntity()).rotationYaw != this.rotationYaw)) {
//					(getRidingEntity().getRidingEntity().getRidingEntity()).rotationYaw = this.rotationYaw;
//				}
//			}
//		}
		if (!getHasFlyed() && (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LANDING.ordinal())) {
			setHasFly(true);
		}
		if (this.posY > ((this.world.provider instanceof IExitHeight) ? ((IExitHeight) this.world.provider).getYCoordinateToTeleport() : 270.0D))
			if ((getRidingEntity() == null) && (this.world.provider instanceof net.minecraft.world.WorldProviderSurface)) {
				setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.LANDING);
				separateStages(true);
			}
		if ((this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LANDING.ordinal()) && (this.F9launchPhase == EnumF9LaunchPhase.DISABLED.ordinal())) {
			setF9LaunchPhase(EnumF9LaunchPhase.ATMOSPHERICENTRY);
		}
		getLandingSteps();
		if ((getYPad() == 0.0D) && (this.F9launchPhase >= EnumF9LaunchPhase.ENTRYBURN.ordinal()) && (this.F9launchPhase != EnumF9LaunchPhase.LANDED.ordinal())) {
			detectFloor();
		}
		if (this.timeUntilLaunch >= 100) {
			i = Math.abs(this.timeUntilLaunch / 100);
		} else {
			i = 1;
		}
		if ((getRidingEntity() == null) && ((this.F9launchPhase == EnumF9LaunchPhase.LANDING.ordinal()) || (this.F9launchPhase == EnumF9LaunchPhase.ENTRYBURN.ordinal()) || ((this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.IGNITED.ordinal()) && (this.rand.nextInt(i) == 0))
				|| (getLaunched() && !getStagesSeparated())) && !ConfigManagerCore.disableSpaceshipParticles && hasValidFuel() && this.world.isRemote) {
			spawnParticles();
		}
		if (this.F9launchPhase == EnumF9LaunchPhase.DISABLED.ordinal())
			if ((this.launchPhase >= EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal()) && hasValidFuel()) {
				if (this.launchPhase == EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal()) {
					double riding = this.getRidingEntity().motionY;
					double d = (this.timeSinceLaunch / 150.0F);

					d = Math.min(d, 1.0D);
					if (d != 0.0D) {
						this.motionY = (-d * Math.cos((this.rotationPitch - 180.0F) / 54.29577951308232D)) + riding;
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
	public void onUpdate() {
		this.synchronizedUpdate();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("oxygenTank")) {
			this.oxygenTank.readFromNBT(nbt.getCompoundTag("oxygenTank"));
		}
		setF9LaunchPhase(getF9LaunchPhaseInt(nbt.getInteger("LaunchPhase")));
		setYPad(nbt.getDouble("YPad"));
		separateStages(nbt.getBoolean("Separated"));
		setHasFly(nbt.getBoolean("HasFly"));
	}

	@Override
	public FluidStack removeOxygen(int amount) {
		return this.oxygenTank.drain(amount * F9ConfigManager.rocketOxygenFactor, true);
	}

	public void rideAndCheckAdvancement(EntityPlayer player) {
		Triggers.TAKEASEAT.trigger(player);
		player.startRiding(this);
	}

	public void separateStages(boolean value) {
		this.separated = value;
	}

	@Override
	public void setDead() {
		if (((this.stacks == null) || this.stacks.isEmpty()) && !this.world.isRemote) {
			entityDropItem(this.stacks.get(0), (float) getYOffset());
		}
		if ((this.posY < ((this.world.provider instanceof IExitHeight) ? ((IExitHeight) this.world.provider).getYCoordinateToTeleport() : 1100.0D)) && !this.world.isRemote) {
			for (ItemStack item : this.stacks) {
				if (item != null) {
					entityDropItem(item, (float) getYOffset());
				}
			}
		}
		super.setDead();
	}

	public void setF9LaunchPhase(EnumF9LaunchPhase step) {
		this.F9launchPhase = step.ordinal();
	}

	public void setHasFly(boolean value) {
		this.hasFly = value;
	}

	public void setLandingPadsItem() {
		this.stacks.set(0, new ItemStack(GCBlocks.landingPad, 9, 0));
	}

	public void setYPad(double pos) {
		this.YPad = pos;
	}

	@Override
	protected boolean shouldCancelExplosion() {
		if (this.F9launchPhase > EnumF9LaunchPhase.LANDED.ordinal())
			return true;
		return super.shouldCancelExplosion();
	}

	protected void spawnParticles() {
		if (!this.isDead) {
			double posHeight = 0.9D;
			Vec3d vecs1 = getLookVec().scale(1.5D);
			Vec3d vecs2 = getLookVec().scale(1.5D).rotateYaw(SpaceXUtils.DegToRad(90.0F));
			Vec3d vecs3 = getLookVec().scale(1.5D).rotateYaw(SpaceXUtils.DegToRad(180.0F));
			Vec3d vecs4 = getLookVec().scale(1.5D).rotateYaw(SpaceXUtils.DegToRad(270.0F));
			Vector3 centerPos1 = new Vector3(this.posX + vecs1.x, this.posY + vecs1.y + posHeight, this.posZ + vecs1.z);
			Vector3 centerPos2 = new Vector3(this.posX + vecs2.x, this.posY + vecs2.y + posHeight, this.posZ + vecs2.z);
			Vector3 centerPos3 = new Vector3(this.posX + vecs3.x, this.posY + vecs3.y + posHeight, this.posZ + vecs3.z);
			Vector3 centerPos4 = new Vector3(this.posX + vecs4.x, this.posY + vecs4.y + posHeight, this.posZ + vecs4.z);
			double y2 = vecs1.y - 0.8D;
			Vector3 motionVec1 = new Vector3((vecs1.scale(0.15D)).x, y2, (vecs1.scale(0.15D)).z);
			Vector3 motionVec2 = new Vector3((vecs2.scale(0.15D)).x, y2, (vecs2.scale(0.15D)).z);
			Vector3 motionVec3 = new Vector3((vecs3.scale(0.15D)).x, y2, (vecs3.scale(0.15D)).z);
			Vector3 motionVec4 = new Vector3((vecs4.scale(0.15D)).x, y2, (vecs4.scale(0.15D)).z);
			Vector3 centerPosPost = new Vector3(0.0D, -0.1D, 0.0D);
			EntityLivingBase riddenByEntity = null;
			if (!getPassengers().isEmpty()) {
				Entity entity = getPassengers().get(0);
				if (entity instanceof EntityLivingBase) {
					riddenByEntity = (EntityLivingBase) entity;
				}
			}
			if (riddenByEntity != null) {
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos1, motionVec1, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos2, motionVec2, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos3, motionVec3, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos4, motionVec4, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos1.translate(centerPosPost), motionVec1, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos2.translate(centerPosPost), motionVec2, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos3.translate(centerPosPost), motionVec3, new Object[] { riddenByEntity });
				SpaceX.proxy.spawnParticle("dragonFlameIdle", centerPos4.translate(centerPosPost), motionVec4, new Object[] { riddenByEntity });
			}
		}
	}

	public void updateInventory(EntityPlayerMP player) {
		GCPlayerStats stats = GCPlayerStats.get(player);
		if (stats.getRocketStacks().get(0) != ItemStack.EMPTY) {
			this.stacks = stats.getRocketStacks();
			if ((this.stacks.get(26) != ItemStack.EMPTY) && (this.stacks.get(26).getItem() == (new ItemStack(Blocks.BARRIER)).getItem())) {
				this.stacks.set(26, ItemStack.EMPTY);
			}
			if ((this.stacks.get(27) != ItemStack.EMPTY) && (this.stacks.get(27).getItem() == (new ItemStack(Blocks.BARRIER)).getItem())) {
				this.stacks.set(27, ItemStack.EMPTY);
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
		nbt.setDouble("YPad", getYPad());
		nbt.setBoolean("Separated", getStagesSeparated());
		nbt.setBoolean("HasFly", getHasFlyed());
	}
}
