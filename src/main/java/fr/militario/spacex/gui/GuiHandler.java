package fr.militario.spacex.gui;

import fr.militario.spacex.containers.ContainerDragonCapsule;
import fr.militario.spacex.containers.ContainerF9SecondStage;
import fr.militario.spacex.containers.ContainerFalcon9Rocket;
import fr.militario.spacex.entity.EntityDragonCapsule;
import fr.militario.spacex.entity.EntityF9SecondStage;
import fr.militario.spacex.entity.EntityFalcon9Rocket;
import fr.militario.spacex.tiles.TileEntityFuelUnloader;
import fr.militario.spacex.tiles.TileEntityOxygenLoader;
import fr.militario.spacex.tiles.TileEntityOxygenUnloader;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
	public static final int SCHEM_FALCON9 = 7;
	public static final int SCHEM_SECONDSTAGE = 8;
	public static final int SCHEM_DRAGON = 9;
	public static final int SCHEM_DRAGON_TRUNK = 10;
	public static final int FALCON9 = 0;
	public static final int SECONDSTAGE = 1;
	public static final int DRAGON = 2;

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			switch (ID) {
			case FALCON9:
				if (player.getRidingEntity() instanceof EntityFalcon9Rocket)
					return new GuiFalcon9RocketInventory(player.inventory, (EntityFalcon9Rocket) player.getRidingEntity());
			case SECONDSTAGE:
				if (player.getRidingEntity() instanceof EntityF9SecondStage)
					return new GuiF9SecondStageInventory(player.inventory, (EntityF9SecondStage) player.getRidingEntity());
			case DRAGON:
				if (player.getRidingEntity() instanceof EntityDragonCapsule)
					return new GuiDragonCapsuleInventory(player.inventory, (EntityDragonCapsule) player.getRidingEntity());
			}
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileEntityOxygenLoader) {
				TileEntityOxygenLoader oxygenLoader = (TileEntityOxygenLoader) tile;
				return new GuiOxygenLoader(player.inventory, oxygenLoader);
			}
			if (tile instanceof TileEntityOxygenUnloader) {
				TileEntityOxygenUnloader oxygenUnloader = (TileEntityOxygenUnloader) tile;
				return new GuiOxygenUnloader(player.inventory, oxygenUnloader);
			}
			if (tile instanceof TileEntityFuelUnloader) {
				TileEntityFuelUnloader fuelUnloader = (TileEntityFuelUnloader) tile;
				return new GuiFuelUnloader(player.inventory, fuelUnloader);
			}
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);
		if (playerBase == null) {
			player.sendMessage(new TextComponentString("Player instance null server-side. This is a bug."));
			return null;
		}
		switch (ID) {
		case FALCON9:
			if (player.getRidingEntity() instanceof EntityFalcon9Rocket)
				return new ContainerFalcon9Rocket(player.inventory, (EntityFalcon9Rocket) player.getRidingEntity());
		case SECONDSTAGE:
			if (player.getRidingEntity() instanceof EntityF9SecondStage)
				return new ContainerF9SecondStage(player.inventory, (EntityF9SecondStage) player.getRidingEntity());
		case DRAGON:
			if (player.getRidingEntity() instanceof EntityDragonCapsule)
				return new ContainerDragonCapsule(player.inventory, (EntityDragonCapsule) player.getRidingEntity());
		}
		if (tile instanceof TileEntityOxygenLoader) {
			TileEntityOxygenLoader oxygenLoader = (TileEntityOxygenLoader) tile;
			return new GuiOxygenLoader(player.inventory, oxygenLoader);
		}
		if (tile instanceof TileEntityOxygenUnloader) {
			TileEntityOxygenUnloader oxygenUnloader = (TileEntityOxygenUnloader) tile;
			return new GuiOxygenUnloader(player.inventory, oxygenUnloader);
		}
		if (tile instanceof TileEntityFuelUnloader) {
			TileEntityFuelUnloader fuelUnloader = (TileEntityFuelUnloader) tile;
			return new GuiFuelUnloader(player.inventory, fuelUnloader);
		}
		return null;
	}
}
