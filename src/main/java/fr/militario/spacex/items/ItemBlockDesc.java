package fr.militario.spacex.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import fr.militario.spacex.blocks.F9Blocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockDesc extends ItemBlockGC {

	public ItemBlockDesc(Block block) {
		super(block);
	}

	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if ((getBlock() instanceof IShiftDescription) && ((IShiftDescription) getBlock()).showDescription(par1ItemStack.getItemDamage()))
			if (Keyboard.isKeyDown(42)) {
				tooltip.addAll((FMLClientHandler.instance().getClient()).fontRenderer.listFormattedStringToWidth(((IShiftDescription) getBlock()).getShiftDescription(par1ItemStack.getItemDamage()), 150));
			} else {
				if (getBlock() instanceof BlockTileGC) {
					TileEntity te = getBlock().createTileEntity(worldIn, getBlock().getStateFromMeta(par1ItemStack.getItemDamage()));
					if (te instanceof TileBaseElectricBlock) {
						float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
						if (powerDrawn > 0.0F) {
							tooltip.add(TextFormatting.GREEN + GCCoreUtil.translateWithFormat("item_desc.powerdraw.name", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20.0F)));
						}
					}
				} else if (getBlock() instanceof BlockAdvancedTile) {
					TileEntity te = getBlock().createTileEntity(worldIn, getBlock().getStateFromMeta(par1ItemStack.getItemDamage()));
					if (te instanceof TileBaseElectricBlock) {
						float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
						if (powerDrawn > 0.0F) {
							tooltip.add(TextFormatting.GREEN + GCCoreUtil.translateWithFormat("item_desc.powerdraw.name", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20.0F)));
						}
					}
				}
				tooltip.add(GCCoreUtil.translateWithFormat("item_desc.shift.name", new Object[] { GameSettings.getKeyDisplayString((FMLClientHandler.instance().getClient()).gameSettings.keyBindSneak.getKeyCode()) }));
			}
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote)
			return;
		if (player instanceof EntityPlayerSP)
			if ((getBlock() == F9Blocks.oxygenLoader) || (getBlock() == F9Blocks.oxygenUnloader) || (getBlock() == F9Blocks.fuelUnloader)) {
				ClientProxyCore.playerClientHandler.onBuild(4, (EntityPlayerSP) player);
			} else if ((getBlock() == F9Blocks.oxygenLoader) || (getBlock() == F9Blocks.oxygenUnloader) || (getBlock() == F9Blocks.fuelUnloader)) {
				ClientProxyCore.playerClientHandler.onBuild(6, (EntityPlayerSP) player);
			}
	}
}
