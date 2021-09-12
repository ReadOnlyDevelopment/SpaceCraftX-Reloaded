package fr.militario.spacex.teleports;

import java.util.Random;

import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.items.F9Items;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class OverridingTeleport implements ITeleportType {

	private final ITeleportType type;

	public OverridingTeleport(ITeleportType teleportType) {
		this.type = teleportType;
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity) {
		return type.getEntitySpawnLocation(world, entity);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand) {
		return type.getParaChestSpawnLocation(world, player, rand);
	}

	@Override
	public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player) {
		return type.getPlayerSpawnLocation(world, player);
	}

	@Override
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket) {
		GCPlayerStats stats = GCPlayerStats.get(player);
		if (verifyDragonIsUsed(stats, ridingAutoRocket)) {
			if (!ridingAutoRocket && (stats.getTeleportCooldown() <= 0)) {
				if (player.capabilities.isFlying) {
					player.capabilities.isFlying = false;
				}
//				EntityDragonCapsule lander = new EntityDragonCapsule(newWorld, player.posX, player.posY, player.posZ, SpaceXUtils.getRocketTypeInt(stats.getRocketType()), 0.0F);
//				lander.setPosition(player.posX, player.posY, player.posZ);
//				lander.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, stats.getFuelLevel()));
//				lander.oxygenTank.setFluid(new FluidStack(GCFluids.fluidFuel, stats.getFuelLevel()));
//				if (!newWorld.isRemote) {
//					CompatibilityManager.forceLoadChunks((WorldServer) newWorld);
//					lander.forceSpawn = true;
//					newWorld.spawnEntity(lander);
//					lander.setWorld(newWorld);
//					newWorld.updateEntityWithOptionalForce(lander, true);
//					player.startRiding(lander);
//					CompatibilityManager.forceLoadChunks((WorldServer) newWorld);
//					SpaceX.logger.debug("Entering lander at : " + player.posX + "," + player.posZ + " lander spawn at: " + lander.posX + "," + lander.posZ);
//				}
//				lander.setLaunchPhase(EntitySpaceshipBase.EnumLaunchPhase.LANDING);
//				lander.separateStages(true);
//				lander.updateInventory(player);
//				stats.setTeleportCooldown(10);
				SpaceXUtils.spawnLander(newWorld, player, stats);
			}
		} else {
			type.onSpaceDimensionChanged(newWorld, player, ridingAutoRocket);
		}
	}

	@Override
	public void setupAdventureSpawn(EntityPlayerMP player) {
		type.setupAdventureSpawn(player);
	}

	@Override
	public boolean useParachute() {
		return type.useParachute();
	}

	private boolean verifyDragonIsUsed(GCPlayerStats stats, boolean ridingAutoRocket) {
		return (stats.getRocketItem() == F9Items.DragonCapsuleItem);
	}
}
